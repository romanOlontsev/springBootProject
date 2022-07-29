package com.viner.site.web;

import com.viner.site.service.UserService;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import com.viner.site.web.dto.UpdateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data/users")
@RequiredArgsConstructor
public class UsersDataController {

    private final UserService userService;

    @PostMapping
    public UserDto addNewUser(AddUserDto user) {
        return userService.addUser(user);
    }

    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable("userId") Long userId,
                              @RequestBody UpdateUserDto updateUserDto) {
        return userService.updateUser(userId, updateUserDto);
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
