package com.viner.site.service;

import com.viner.site.entity.UserEntity;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity registration(UserEntity user) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException("User already exists");
        }
        return userRepository.save(user);
    }

    public List<UserEntity> getUsers() {
        List<UserEntity> userEntities = new ArrayList<>();
        userRepository.findAll()
                .forEach(userEntities::add);
        return userEntities;
    }
}
