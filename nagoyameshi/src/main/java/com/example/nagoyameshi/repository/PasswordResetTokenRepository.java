package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Memberinfo;
import com.example.nagoyameshi.entity.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("DELETE FROM PasswordResetToken p WHERE p.user = :user")
    void deleteByUser(Memberinfo user);
}