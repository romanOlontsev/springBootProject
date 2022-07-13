package com.viner.site.web;

import com.viner.site.entity.User;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.service.UserService;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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
                          BindingResult result,
                          Model model) {

        if (result.hasErrors()) {
//            model.addAttribute("errorMessage","ERROR");
            return "registration";
        }
//        model.addAttribute("user", addUserDto);
        userService.addUser(addUserDto);
        return "redirect:/login";

    }
}
