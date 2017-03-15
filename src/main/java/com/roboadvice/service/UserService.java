package com.roboadvice.service;


import com.roboadvice.model.User;

public interface UserService {

    boolean insert (User u);
    boolean authentication(User u);
    User selectByEmail(String email);
    User selectById(long id);
}
