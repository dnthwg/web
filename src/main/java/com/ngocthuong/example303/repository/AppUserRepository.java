package com.ngocthuong.example303.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ngocthuong.example303.model.AppUser;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByUsername(String username);
    Boolean existsByUsername(String username);
}
