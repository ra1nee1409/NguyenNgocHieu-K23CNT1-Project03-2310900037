package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nnhcart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhuser_id", nullable = false)
    @ToString.Exclude
    private NnhUser nnhUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhproduct_id", nullable = false)
    private NnhProduct nnhProduct;

    @Column(name = "nnhquantity", nullable = false)
    @Builder.Default
    private Integer quantity = 1;

    @Column(name = "nnhadded_at")
    @Builder.Default
    private LocalDateTime addedAt = LocalDateTime.now();
}