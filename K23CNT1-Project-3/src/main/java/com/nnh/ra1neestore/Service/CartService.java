package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.Cart;
import com.nnh.ra1neestore.Entity.Product;
import com.nnh.ra1neestore.Entity.User;
import com.nnh.ra1neestore.Repository.CartRepository;
import com.nnh.ra1neestore.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    @Transactional
    public Cart addToCart(User user, Long productId, Integer quantity) {
        // Kiểm tra sản phẩm có tồn tại không
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));

        // Kiểm tra số lượng tồn kho
        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Sản phẩm không đủ số lượng trong kho!");
        }

        // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
        Optional<Cart> existingCart = cartRepository.findByUserIdAndProductId(user.getId(), productId);

        if (existingCart.isPresent()) {
            // Nếu đã có, cập nhật số lượng
            Cart cart = existingCart.get();
            int newQuantity = cart.getQuantity() + quantity;
            
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Sản phẩm không đủ số lượng trong kho!");
            }
            
            cart.setQuantity(newQuantity);
            return cartRepository.save(cart);
        } else {
            // Nếu chưa có, tạo mới
            Cart cart = Cart.builder()
                    .user(user)
                    .product(product)
                    .quantity(quantity)
                    .addedAt(LocalDateTime.now())
                    .build();
            return cartRepository.save(cart);
        }
    }

    /**
     * Lấy danh sách giỏ hàng của user
     */
    @Transactional(readOnly = true)
    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUserId(user.getId());
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @Transactional
    public Cart updateQuantity(Long cartId, Integer quantity, User user) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!"));

        // Kiểm tra quyền sở hữu
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền thực hiện thao tác này!");
        }

        // Kiểm tra số lượng tồn kho
        if (cart.getProduct().getStockQuantity() < quantity) {
            throw new RuntimeException("Sản phẩm không đủ số lượng trong kho!");
        }

        cart.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    @Transactional
    public void removeFromCart(Long cartId, User user) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm trong giỏ hàng!"));

        // Kiểm tra quyền sở hữu
        if (!cart.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bạn không có quyền thực hiện thao tác này!");
        }

        cartRepository.delete(cart);
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @Transactional
    public void clearCart(User user) {
        cartRepository.deleteByUserId(user.getId());
    }

    /**
     * Tính tổng tiền trong giỏ hàng
     */
    @Transactional(readOnly = true)
    public BigDecimal getCartTotal(User user) {
        List<Cart> cartItems = cartRepository.findByUserId(user.getId());
        return cartItems.stream()
                .map(item -> {
                    BigDecimal price = item.getProduct().getSalePrice() != null 
                            ? item.getProduct().getSalePrice() 
                            : item.getProduct().getPrice();
                    return price.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Đếm số lượng items trong giỏ hàng
     */
    @Transactional(readOnly = true)
    public long getCartItemCount(User user) {
        return cartRepository.countByUserId(user.getId());
    }
}
