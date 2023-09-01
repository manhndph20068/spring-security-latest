package com.javamanh.repository;

import com.javamanh.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByName(String username);

    @Query("SELECT u FROM Account u JOIN u.role r WHERE u.email = :email")
    Optional<Account> findByEmail(@Param("email") String email);
}
