package com.viner.site.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ConsoleController {

    @GetMapping("/console")
    public ModelAndView console() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("console");
        return modelAndView;
    }

    @GetMapping("/console/users")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @GetMapping("/console/users/{userId}")
    public ModelAndView getSelectedUser(@PathVariable String userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userId", userId);
        modelAndView.setViewName("chosenUser");
        return modelAndView;
    }
}
