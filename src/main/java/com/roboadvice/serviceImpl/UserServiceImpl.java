package com.roboadvice.serviceImpl;

import com.roboadvice.dto.UserDTO;
import com.roboadvice.repository.UserRepository;
import com.roboadvice.model.User;
import com.roboadvice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean insert(User u) {
        if(userRepository.findByEmail(u.getEmail()) == null){
            u = userRepository.save(u);
            if(u != null){
                return true;
            }
            else return false;
        }
        else return false;
    }

    @Override
    public User selectByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDTO getUser(String userEmail) {
        User u= userRepository.findByEmail(userEmail);
        if(u==null)
            return null;

        UserDTO userDTO = new UserDTO(u.getName(), u.getSurname(), u.getEmail(), u.getPassword());
        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User u = userRepository.findByEmail(userDTO.getEmail());
        if(u== null)
            return null;

        u.setName(userDTO.getName());
        u.setSurname(userDTO.getSurname());
        userRepository.save(u);
        return userDTO;

    }

}
