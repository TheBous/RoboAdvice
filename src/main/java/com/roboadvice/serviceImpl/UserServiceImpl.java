package com.roboadvice.serviceImpl;

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
    public boolean authentication(User u) {
        User user = userRepository.findByEmailAndPassword(u.getEmail(), u.getPassword());
        if(user != null){
            u.setId(user.getId());
            u.setName(user.getName());
            u.setSurname(user.getSurname());
            return true;
        }
        return false;
    }

    @Override
    public User selectByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User selectById(long id) {
        return userRepository.findById(id);
    }
}
