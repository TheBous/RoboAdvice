package com.roboadvice.service;


import com.roboadvice.dto.UserDTO;
import com.roboadvice.model.User;

public interface UserService {

    boolean insert (User u);
    User selectByEmail(String userEmail);
    UserDTO getUser(String userEmail);
    UserDTO updateUser(String userEmail, UserDTO userDTO);



}
