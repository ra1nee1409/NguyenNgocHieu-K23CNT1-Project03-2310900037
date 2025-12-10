package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nnhorders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nnhuser_id", nullable = false)
    private NnhUser nnhUser;

    @Column(name = "nnhtotal_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "nnhstatus", nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "nnhcustomer_name", length = 100)
    private String customerName;

    @Column(name = "nnhcustomer_phone", length = 20)
    private String customerPhone;

    @Column(name = "nnhcustomer_address", columnDefinition = "TEXT")
    private String customerAddress;

    @OneToMany(mappedBy = "nnhOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<NnhOrderItem> nnhOrderItems = new ArrayList<>();

    @Column(name = "nnhcreated_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderStatus {
        PENDING, CONFIRMED, SHIPPING, DELIVERED, COMPLETED, CANCELLED
    }
}