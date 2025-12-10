package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "nnh_password_reset_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhPasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhuser_id", nullable = false)
    private NnhUser nnhUser;

    @Column(name = "nnhtoken", nullable = false, unique = true)
    private String token;

    @Column(name = "nnhexpiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "nnhused")
    @Builder.Default
    private Boolean used = false;

    @Column(name = "nnhcreated_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
