package com.roboadvice.controllerTest;

import com.roboadvice.RoboadviceApplication;
import com.roboadvice.controller.UserController;
import com.roboadvice.dto.UserDTO;
import com.roboadvice.model.User;
import com.roboadvice.security.AuthenticationController;
import com.roboadvice.utils.GenericResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RoboadviceApplication.class)
public class UserControllerTest {

    @Autowired
    private AuthenticationController authenticationController;


    @Test
    public void loginTestOK() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("leo@galati.com");
        userDTO.setPassword("ef797c8118f02dfb649607dd5d3f8c7623048c9c063d532cc95c5ed7a898a64f");
        try {
            GenericResponse<Map<String, Object>> response = authenticationController.login(userDTO);
            assertEquals(0, response.getStatusCode());
            assertTrue(response.getData() instanceof Map<?, ?>);
        }catch(Exception e ){

        }
    }

    @Test
    public void loginTestFAILURE() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("leo@galati.com");
        userDTO.setPassword("rewfdwqwefd");
        try {
            GenericResponse<Map<String, Object>> response = authenticationController.login(userDTO);
            assertNotEquals(0, response.getStatusCode());
            assertTrue(response.getData() instanceof Map<?, ?>);
        }catch(Exception e ){

        }
    }

    @Test
    public void registrationTestFAILURE() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Pinco");
        userDTO.setSurname("Pallino");
        userDTO.setEmail("pinco@pallino.com");
        userDTO.setPassword("rewfdwqwefd");
        try {
            GenericResponse<User> response = authenticationController.signup(userDTO);
            assertEquals(0, response.getStatusCode());
            assertTrue(response.getData() instanceof User);
        }catch(Exception e ){

        }
    }

}
