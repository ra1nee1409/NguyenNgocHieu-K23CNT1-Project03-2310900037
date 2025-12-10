package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhPasswordResetToken;
import com.nnh.ra1neestore.Entity.NnhUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface NnhPasswordResetTokenRepository extends JpaRepository<NnhPasswordResetToken, Long> {

    Optional<NnhPasswordResetToken> findByToken(String token);

    @Modifying
    @Transactional
    void deleteByNnhUser(NnhUser user);

    @Modifying
    @Transactional
    void deleteByExpiryDateBefore(LocalDateTime date);
}
