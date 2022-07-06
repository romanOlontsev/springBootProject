package com.viner.site.service;

import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.exceptions.UserNotFoundException;
import com.viner.site.mappers.UserMapper;
import com.viner.site.repository.UserRepository;
import com.viner.site.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public User addUser(User user) {
        userRepository.findByUsername(user.getUsername())
                      .ifPresent(it -> {
                          throw new UserAlreadyExistsException("User already exists");
                      });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(Role.USER));
        return userRepository.save(user);
    }

    public List<UserDto> getUsers() {
        return userRepository.findAll()
                             .stream()
                             .map(UserMapper.INSTANCE::toUserDto)
                             .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User foundUser = getUserEntity(id);
        return UserMapper.INSTANCE.toUserDto(foundUser);
    }

    public UserDto deleteUser(Long id) {
        User foundUser = getUserEntity(id);
        userRepository.deleteById(id);
        return UserMapper.INSTANCE.toUserDto(foundUser);
    }

    private User getUserEntity(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exists"));
    }
}
