package com.hotel.lakeside.service;

import com.hotel.lakeside.exception.UserAlreadyExistsException;
import com.hotel.lakeside.model.Role;
import com.hotel.lakeside.model.User;
import com.hotel.lakeside.repository.RoleRepository;
import com.hotel.lakeside.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user){
        if (repository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + "already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return repository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            repository.deleteByEmail(email);
        }
    }

    @Override
    public User getUser(String email) {
        return repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
