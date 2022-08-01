package com.viner.site.service;

import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.exceptions.UserNotFoundException;
import com.viner.site.mappers.UserMapper;
import com.viner.site.repository.UserRepository;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import com.viner.site.web.dto.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Transactional
    public UserDto addUser(AddUserDto addedUser) {
        getExceptionIfUserFound(addedUser.getUsername());
        User user = UserMapper.INSTANCE.addUserDtoToUser(addedUser);
        user.setPassword(passwordEncoder.encode(addedUser.getPassword()));
        user.setRoles(Collections.singleton(Role.ROLE_USER));
        userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserDto updateUserDto) {
        getExceptionIfUserFound(updateUserDto.getUsername());
        User user = getUserEntity(userId);
        if (!updateUserDto.getUsername()
                          .equals("")) {
            user.setUsername(updateUserDto.getUsername());
        }
        if (!updateUserDto.getPassword()
                          .equals("")) {
            user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
        }
        if (!updateUserDto.getRoles()
                          .isEmpty()) {
            user.setRoles(updateUserDto.getRoles());
        }
        userRepository.save(user);
        return UserMapper.INSTANCE.userToUserDto(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(UserMapper.INSTANCE::userToUserDto)
                             .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User foundUser = getUserEntity(id);
        return UserMapper.INSTANCE.userToUserDto(foundUser);
    }

    public UserDto deleteUser(Long id) {
        User foundUser = getUserEntity(id);
        userRepository.deleteById(id);
        return UserMapper.INSTANCE.userToUserDto(foundUser);
    }

    private User getUserEntity(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exists"));
    }

    private void getExceptionIfUserFound(String username) {
        if (username.equals(adminUserName) || userRepository.findByUsername(username)
                                                            .isPresent()) {
            throw new UserAlreadyExistsException("User with name " + username + " already exists");
        }
    }
}
