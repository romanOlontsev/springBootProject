package com.viner.site.web;

import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.service.UserService;
import com.viner.site.web.dto.AddUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute(new AddUserDto());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid AddUserDto addUserDto,
                          BindingResult result) {
        if (result.hasErrors()) {
            return "registration";
        }
        try {
            userService.addUser(addUserDto);
        } catch (UserAlreadyExistsException e) {
            ObjectError objectError = new ObjectError("userNotFound", e.getMessage());
            result.addError(objectError);
            return "registration";
        }
        return "redirect:/login";
    }
}
