package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nnhcategories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @Column(name = "nnhname", nullable = false, length = 100)
    private String name;

    @Column(name = "nnhdescription", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "nnhCategory", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private List<NnhProduct> nnhProducts = new ArrayList<>();

    @Formula("(SELECT COUNT(*) FROM nnhproducts p WHERE p.nnhcategory_id = nnhid)")
    private Long nnhProductCount;

    @Column(name = "nnhcreated_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}