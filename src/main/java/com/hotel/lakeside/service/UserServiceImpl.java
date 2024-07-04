package com.hotel.lakeside.service;

import com.hotel.lakeside.model.User;
import com.hotel.lakeside.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService{

    private final UserRepository repository;

    @Override
    public User registerUser(User user) {
        if (repository.existByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + "already exists");
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return List.of();
    }

    @Override
    public void deleteUser(String email) {

    }

    @Override
    public User getUser(String email) {
        return null;
    }
}
