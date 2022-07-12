package com.viner.site.web;

import com.viner.site.entity.User;
import com.viner.site.service.UserService;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(@RequestBody AddUserDto user) {
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

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable("userId") Long id) {
        return userService.deleteUser(id);
    }
}
