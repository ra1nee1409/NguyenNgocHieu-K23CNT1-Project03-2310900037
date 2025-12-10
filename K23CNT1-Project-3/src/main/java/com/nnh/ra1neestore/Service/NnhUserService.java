package com.nnh.ra1neestore.Service;

import com.nnh.ra1neestore.DTO.PasswordChangeDTO;
import com.nnh.ra1neestore.DTO.ProfileUpdateDTO;
import com.nnh.ra1neestore.Entity.NnhPasswordResetToken;
import com.nnh.ra1neestore.Entity.NnhUser;
import com.nnh.ra1neestore.Repository.NnhPasswordResetTokenRepository;
import com.nnh.ra1neestore.Repository.NnhUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NnhUserService {

    private final NnhUserRepository nnhUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final NnhPasswordResetTokenRepository tokenRepository;

    @Value("${password.reset.token.expiry.hours:1}")
    private int tokenExpiryHours;

    @Transactional
    public void updateProfile(NnhUser user, ProfileUpdateDTO dto) {
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(dto.getAddress());
        nnhUserRepository.save(user);
    }

    public boolean verifyCurrentPassword(NnhUser user, String currentPassword) {
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    @Transactional
    public void changePassword(NnhUser user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        nnhUserRepository.save(user);
    }

    public NnhUser findByUsername(String username) {
        return nnhUserRepository.findByUsername(username).orElse(null);
    }

    public NnhUser findByEmail(String email) {
        return nnhUserRepository.findByEmail(email).orElse(null);
    }

    @Transactional
    public String createPasswordResetToken(String email) {
        NnhUser user = findByEmail(email);
        if (user == null) {
            return null;
        }

        // Delete old tokens for this user
        tokenRepository.deleteByNnhUser(user);

        // Create new token
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(tokenExpiryHours);

        NnhPasswordResetToken resetToken = NnhPasswordResetToken.builder()
                .nnhUser(user)
                .token(token)
                .expiryDate(expiryDate)
                .build();

        tokenRepository.save(resetToken);
        return token;
    }

    public boolean validatePasswordResetToken(String token) {
        System.out.println("=== VALIDATING TOKEN ===");
        System.out.println("Token to validate: " + token);

        NnhPasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);
        System.out.println("Token found in DB: " + (resetToken != null));

        if (resetToken == null) {
            System.out.println("Validation failed: Token not found");
            return false;
        }

        System.out.println("Token used: " + resetToken.getUsed());
        System.out.println("Token expiry: " + resetToken.getExpiryDate());
        System.out.println("Current time: " + java.time.LocalDateTime.now());
        System.out.println("Is expired: " + resetToken.isExpired());

        if (resetToken.getUsed()) {
            System.out.println("Validation failed: Token already used");
            return false;
        }

        if (resetToken.isExpired()) {
            System.out.println("Validation failed: Token expired");
            return false;
        }

        System.out.println("Validation SUCCESS");
        return true;
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        NnhPasswordResetToken resetToken = tokenRepository.findByToken(token).orElse(null);

        if (resetToken == null || resetToken.getUsed() || resetToken.isExpired()) {
            return false;
        }

        // Update password
        NnhUser user = resetToken.getNnhUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        nnhUserRepository.save(user);

        // Mark token as used
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return true;
    }
}
