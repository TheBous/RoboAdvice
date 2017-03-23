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

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE, RequestMethod.PUT })
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<User> signup(@RequestBody @Valid UserDTO userDTO){
        User u = new User(0,userDTO.getName(), userDTO.getSurname(), userDTO.getEmail(), userDTO.getPassword(), "USER");
        if(userService.insert(u))
            return new GenericResponse<>(u, Constant.SUCCES_MSG, Constant.SUCCESS);
        else
            return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
    public GenericResponse<Map<String, Object>> login(@RequestBody @Valid UserDTO userDTO){
        String token = null;
        String secretKey = "mikyfalSone";
        User u = userService.selectByEmail(userDTO.getEmail());
        Map<String, Object> tokenMap = new HashMap<>();
        if(u!= null && u.getPassword().equals(userDTO.getPassword())){
            Date date = new Date();
            long t = date.getTime();
            //scadenza token: 7200000 millisecondi = 2 ore
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
