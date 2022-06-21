package com.viner.site.controller;

import com.viner.site.entity.UserEntity;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok("user saved");
        } catch (UserAlreadyExistsException exception) {
            return ResponseEntity.badRequest()
                    .body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.badRequest()
                    .body("Error");
        }
    }

    @GetMapping
    public List<UserEntity> getUsers() {
        return userService.getUsers();
    }
}
