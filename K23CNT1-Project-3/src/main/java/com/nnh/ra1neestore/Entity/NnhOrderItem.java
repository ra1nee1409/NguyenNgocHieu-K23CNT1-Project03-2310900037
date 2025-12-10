package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "nnhorder_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhOrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhorder_id", nullable = false)
    @ToString.Exclude
    private NnhOrder nnhOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhproduct_id", nullable = false)
    private NnhProduct nnhProduct;

    @Column(name = "nnhquantity", nullable = false)
    private Integer quantity;

    @Column(name = "nnhunit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    public BigDecimal getTotalPrice() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}