package com.roboadvice.security;

import com.roboadvice.dto.UserDTO;
import com.roboadvice.model.User;
import com.roboadvice.service.UserService;
import com.roboadvice.utils.Constant;
import com.roboadvice.utils.GenericResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Method used for user signup. User's password is encrypted using class BCryptPasswordEncoder and
     * then stored in the database.
     *
     * @param userDTO DTO object that contains user's info.
     * @return return user object.
     */
    @RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<User> signup(@RequestBody @Valid UserDTO userDTO){
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String password = bcrypt.encode(userDTO.getPassword());
        User u = new User(0,userDTO.getName(), userDTO.getSurname(), userDTO.getEmail(), password, "USER");
        if(userService.insert(u))
            return new GenericResponse<>(u, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    /**
     * Method used for user's login.
     *
     * @param userDTO DTO object that contains user's email and password.
     * @return return Map<> object that contains user's info and token JWT.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<Map<String, Object>> login(@RequestBody @Valid UserDTO userDTO){
        String token = null;
        String secretKey = "mikyfalSone";
        User u = userService.selectByEmail(userDTO.getEmail());
        Map<String, Object> tokenMap = new HashMap<>();
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String password = bcrypt.encode(userDTO.getPassword());
        if(u!= null && bcrypt.matches(userDTO.getPassword(), u.getPassword())){
            Date date = new Date();
            long t = date.getTime();
            //token expires in 7200000 millis = 2h
            token = Jwts.builder().setSubject(userDTO.getEmail()).claim("role", u.getRole()).setIssuedAt(date).setExpiration(new Date(t+7200000))
                    .signWith(SignatureAlgorithm.HS256, secretKey).compact();
            tokenMap.put("token", token);
            tokenMap.put("user", u);
            return new GenericResponse<>(tokenMap, "AUTHORIZED", Constant.SUCCESS);
        }
        else{
            tokenMap.put("token", null);
            return new GenericResponse<>(tokenMap, "UNAUTHORIZED", Constant.ERROR);
        }
    }


}
