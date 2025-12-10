package com.nnh.ra1neestore.Config;

import com.nnh.ra1neestore.Entity.NnhUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Custom UserDetails implementation để wrap NnhUser entity
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final NnhUser nnhUser;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert nnhUser role to Spring Security GrantedAuthority
        // Database có 'ADMIN' và 'CUSTOMER', cần convert sang 'ROLE_ADMIN' và
        // 'ROLE_CUSTOMER'
        String roleName = "ROLE_" + nnhUser.getRole().name().toUpperCase();
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return nnhUser.getPassword();
    }

    @Override
    public String getUsername() {
        return nnhUser.getUsername();
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
        return nnhUser.getFullName();
    }

    /**
     * Helper method để lấy role
     */
    public NnhUser.UserRole getRole() {
        return nnhUser.getRole();
    }

    /**
     * Helper method để lấy user ID
     */
    public Long getUserId() {
        return nnhUser.getId();
    }
}
