package com.roboadvice.service;


import com.roboadvice.dto.UserDTO;
import com.roboadvice.model.User;

public interface UserService {

    boolean insert (User u);
    User selectByEmail(String email);
    UserDTO getUser(String userEmail);
    UserDTO updateUser(UserDTO userDTO);



}
