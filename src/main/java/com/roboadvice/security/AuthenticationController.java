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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(value = "*")
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    //public GenericResponse<User> signup(@RequestBody @Validate UserDTO userDTO){
    public GenericResponse<User> signup(@RequestParam(value="name", defaultValue = "undefined") String name,
                                        @RequestParam(value="surname", defaultValue = "undefined") String surname,
                                        @RequestParam(value="email", defaultValue = "undefined") String email,
                                        @RequestParam(value="password", defaultValue = "undefined") String pass){

        if(!name.equals("undefined") && !surname.equals("undefined") && !email.equals("undefined") && !pass.equals("undefined")){
            User u = new User(0,name, surname, email, pass, "USER");

            if(userService.insert(u)){
                return new GenericResponse<>(u, Constant.SUCCES_MSG, Constant.SUCCESS);
            }
        }
        return new GenericResponse<>(null, Constant.ERROR_MSG, Constant.ERROR);

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    //public GenericResponse<Map<String, Object>> login(@RequestBody @Validate UserDTO userDTO){
    public GenericResponse<Map<String, Object>> login(@RequestParam(value = "email", defaultValue = "undefined") String email,
                                                      @RequestParam(value = "password", defaultValue = "undefined") String pass,
                                                      HttpServletResponse response) throws IOException{
        String token = null;
        String secretKey = "mikyfalSone";
        User u = userService.selectByEmail(email);
        Map<String, Object> tokenMap = new HashMap<>();
        if(u!= null && u.getPassword().equals(pass)){
            token = Jwts.builder().setSubject(email).claim("role", u.getRole()).setIssuedAt(new Date())
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
