package com.edu.springboot.friendrequests.repository;

import com.edu.springboot.friendrequests.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 📌 이메일을 통해 사용자 조회
    Optional<User> findByEmail(String email);

    // 📌 사용자 ID로 조회 (명확성을 위해 userId로 변경)
    Optional<User> findByUserId(Long userId);
    
    Optional<User> findByNickname(String nickname);
}
