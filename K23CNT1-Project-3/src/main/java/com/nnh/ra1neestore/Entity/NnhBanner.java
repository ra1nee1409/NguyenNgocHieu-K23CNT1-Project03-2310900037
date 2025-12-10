package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nnhbanners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhBanner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @Column(name = "nnhname", nullable = false, length = 255)
    private String name;

    @Column(name = "nnhimage_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "nnhdisplay_order")
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "nnhis_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "nnhcreated_at")
    private LocalDateTime createdAt;

    @Column(name = "nnhupdated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
