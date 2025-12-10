package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nnhreviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhuser_id", nullable = false)
    private NnhUser nnhUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhproduct_id", nullable = false)
    private NnhProduct nnhProduct;

    @Column(name = "nnhrating", nullable = false)
    private Integer rating;

    @Column(name = "nnhcomment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "nnhcreated_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
