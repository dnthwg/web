package com.ngocthuong.example303.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ngocthuong.example303.model.Role;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
