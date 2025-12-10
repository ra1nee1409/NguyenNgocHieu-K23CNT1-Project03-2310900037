package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.Entity.NnhCart;
import com.nnh.ra1neestore.Entity.NnhProduct;
import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Repository.NnhCartRepository;
import com.nnh.ra1neestore.Repository.NnhProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NnhCartService {

    private final NnhCartRepository cartRepository;
    private final NnhProductRepository productRepository;

    @Transactional
    public String addToCart(Long userId, Long productId, Integer quantity) {
        // Check if product already in cart
        var existingCart = cartRepository.findByNnhUserIdAndNnhProductId(userId, productId);

        NnhProduct product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (existingCart.isPresent()) {
            // Update quantity
            NnhCart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            cartRepository.save(cart);
        } else {
            // Create new cart item
            NnhUser user = new NnhUser();
            user.setId(userId);

            NnhCart cart = NnhCart.builder()
                    .nnhUser(user)
                    .nnhProduct(product)
                    .quantity(quantity)
                    .build();

            cartRepository.save(cart);
        }

        return product.getName();
    }

    public List<NnhCart> getCartItems(Long userId) {
        return cartRepository.findByNnhUserId(userId);
    }

    @Transactional
    public void updateQuantity(Long cartId, Integer quantity) {
        NnhCart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (quantity <= 0) {
            cartRepository.delete(cart);
        } else {
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
    }

    @Transactional
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteByNnhUserId(userId);
    }

    public BigDecimal getCartTotal(Long userId) {
        List<NnhCart> cartItems = getCartItems(userId);
        return cartItems.stream()
                .map(item -> item.getNnhProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getCartItemCount(Long userId) {
        return (int) cartRepository.countByNnhUserId(userId);
    }
}
