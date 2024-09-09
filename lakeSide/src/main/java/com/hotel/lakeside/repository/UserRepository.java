package com.hotel.lakeside.repository;

import com.hotel.lakeside.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    void deleteByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
