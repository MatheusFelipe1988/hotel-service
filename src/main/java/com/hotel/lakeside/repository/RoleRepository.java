package com.hotel.lakeside.repository;

import com.hotel.lakeside.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Optional<Role> findByName(String role);

    boolean existsByName(Role role);
}
