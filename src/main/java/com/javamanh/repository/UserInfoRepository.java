package com.javamanh.repository;

import com.javamanh.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

    @Query("SELECT u FROM UserInfo u JOIN u.role r WHERE u.email = :email")
    Optional<UserInfo> findByEmail(@Param("email") String email);
}
