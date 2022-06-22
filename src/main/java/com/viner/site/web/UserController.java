package com.viner.site.web;

import com.viner.site.entity.UserEntity;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.service.UserService;
import com.viner.site.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserEntity addNewUser(@RequestBody UserEntity user) {
        return userService.addUser(user);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable("userId") Long id) {
        return userService.getUserById(id);
    }
}
