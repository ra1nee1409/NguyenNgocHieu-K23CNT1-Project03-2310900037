package com.nnh.ra1neestore.Repository;

import com.nnh.ra1neestore.Entity.NnhUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NnhUserRepository extends JpaRepository<NnhUser, Long> {

    /**
     * Tìm nnhUser theo username (dùng cho login)
     * 
     * @param username Username
     * @return Optional chứa NnhUser nếu tìm thấy
     */
    Optional<NnhUser> findByUsername(String username);

    // Search users by username or email
    List<NnhUser> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);

    // Search users by username or full name
    List<NnhUser> findByUsernameContainingOrFullNameContaining(String username, String fullName);

    /**
     * Tìm nnhUser theo email
     * 
     * @param email Email
     * @return Optional chứa NnhUser nếu tìm thấy
     */
    Optional<NnhUser> findByEmail(String email);

    /**
     * Kiểm tra username đã tồn tại chưa
     * 
     * @param username Username cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean existsByUsername(String username);

    /**
     * Kiểm tra email đã tồn tại chưa
     * 
     * @param email Email cần kiểm tra
     * @return true nếu đã tồn tại
     */
    boolean existsByEmail(String email);
}
