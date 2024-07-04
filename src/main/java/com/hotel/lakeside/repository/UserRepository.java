package com.hotel.lakeside.repository;

import com.hotel.lakeside.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existByEmail(String email);
}
