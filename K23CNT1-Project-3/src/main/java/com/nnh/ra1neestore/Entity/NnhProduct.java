package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "nnhproducts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @Column(name = "nnhname", nullable = false, length = 255)
    private String name;

    @Column(name = "nnhdescription", columnDefinition = "TEXT")
    private String description;

    @Column(name = "nnhprice", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "nnhsale_price", precision = 15, scale = 2)
    private BigDecimal salePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhcategory_id", nullable = false)
    private NnhCategory nnhCategory;

    @Column(name = "nnhimage_url", length = 255)
    private String imageUrl;

    @Column(name = "nnhstock_quantity")
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(name = "nnhis_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "nnhcreated_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}