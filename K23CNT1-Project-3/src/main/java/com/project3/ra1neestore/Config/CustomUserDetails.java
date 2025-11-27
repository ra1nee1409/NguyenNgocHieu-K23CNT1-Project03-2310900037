package com.project3.ra1neestore.Config;

import com.project3.ra1neestore.Entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails implementation để wrap User entity
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert user role to Spring Security GrantedAuthority
        // Database có 'admin' và 'customer', cần convert sang 'ROLE_ADMIN' và
        // 'ROLE_CUSTOMER'
        String roleName = "ROLE_" + user.getRole().name().toUpperCase();
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Helper method để lấy full name
     */
    public String getFullName() {
        return user.getFullName();
    }

    /**
     * Helper method để lấy role
     */
    public User.UserRole getRole() {
        return user.getRole();
    }
}
