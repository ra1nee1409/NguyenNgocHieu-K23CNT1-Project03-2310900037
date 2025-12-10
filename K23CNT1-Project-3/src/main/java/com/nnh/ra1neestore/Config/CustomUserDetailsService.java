package com.nnh.ra1neestore.Config;

import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Repository.NnhUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService để load nnhUser từ database
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final NnhUserRepository nnhUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        NnhUser nnhUser = nnhUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Không tìm thấy nnhUser với username: " + username));

        return new CustomUserDetails(nnhUser);
    }
}
