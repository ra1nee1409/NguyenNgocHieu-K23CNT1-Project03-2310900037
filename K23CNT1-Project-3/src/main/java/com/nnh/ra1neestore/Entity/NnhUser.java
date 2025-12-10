package com.nnh.ra1neestore.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "nnhusers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NnhUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nnhid")
    private Long id;

    @Column(name = "nnhusername", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "nnhemail", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "nnhpassword", nullable = false, length = 255)
    private String password;

    @Column(name = "nnhfull_name", length = 100)
    private String fullName;

    @Column(name = "nnhphone", length = 20)
    private String phone;

    @Column(name = "nnhaddress", columnDefinition = "TEXT")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "nnhrole", nullable = false)
    @Builder.Default
    private UserRole role = UserRole.CUSTOMER;

    @Enumerated(EnumType.STRING)
    @Column(name = "nnhstatus", nullable = false)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "nnhcreated_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum UserRole {
        CUSTOMER, ADMIN
    }

    public enum UserStatus {
        ACTIVE, INACTIVE, PENDING
    }
}