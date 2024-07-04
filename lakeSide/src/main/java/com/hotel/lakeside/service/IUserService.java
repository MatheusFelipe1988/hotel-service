package com.hotel.lakeside.service;

import com.hotel.lakeside.exception.UserAlreadyExistsException;
import com.hotel.lakeside.model.User;

import java.util.List;

public interface IUserService {
    User registerUser(User user) throws UserAlreadyExistsException;
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

}
