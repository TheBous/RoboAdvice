package com.roboadvice.controller;

import com.roboadvice.dto.UserDTO;
import com.roboadvice.utils.GenericResponse;
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
    public GenericResponse<UserDTO> getUser(Authentication authentication){
        String email = authentication.getName();
        UserDTO userDTO = userService.getUser(email);
        if(userDTO!=null)
            return new GenericResponse<>(userDTO, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<UserDTO> updateUser(@RequestBody @Valid UserDTO userDTO,
                                               Authentication authentication){
        String email = authentication.getName();
        UserDTO u = userService.updateUser(email, userDTO);
        if(u!=null)
            return new GenericResponse<>(u, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }


}
