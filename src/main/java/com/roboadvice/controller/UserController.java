package com.roboadvice.controller;

import com.roboadvice.dto.UserDTO;
import com.roboadvice.utils.GenericResponse;
import com.roboadvice.model.User;
import com.roboadvice.service.UserService;
import com.roboadvice.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public GenericResponse getUser(Authentication authentication){
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if(u!= null){
            return new GenericResponse<>(u, Constant.SUCCES_MSG,Constant.SUCCESS);
        }
        return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse updateUser(@RequestBody @Valid UserDTO userDTO,
                                      Authentication authentication){
        String email = authentication.getName();
        User u = userService.selectByEmail(email);
        if(u!= null){
            u.setName(userDTO.getName());
            u.setSurname(userDTO.getSurname());
            userService.insert(u);
            return new GenericResponse<>(u, Constant.SUCCES_MSG,Constant.SUCCESS);
        }
        return new GenericResponse<>(null, "USER NOT FOUND!", Constant.ERROR);

    }


}
