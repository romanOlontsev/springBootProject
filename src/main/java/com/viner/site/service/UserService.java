package com.viner.site.service;

import com.viner.site.entity.UserEntity;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.exceptions.UserNotFoundException;
import com.viner.site.mappers.UserMapper;
import com.viner.site.repository.UserRepository;
import com.viner.site.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserEntity addUser(UserEntity user) {
        userRepository.findByUsername(user.getUsername())
                .ifPresent(it -> {
                    throw new UserAlreadyExistsException("User already exists");
                });
        return userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper.INSTANCE::toUserDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        UserEntity foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exists"));
        return UserMapper.INSTANCE.toUserDto(foundUser);
    }
}
