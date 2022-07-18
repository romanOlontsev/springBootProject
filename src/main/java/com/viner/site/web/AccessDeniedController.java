package com.viner.site.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {
    @GetMapping("/accessDenied")
    public String getAccessDenied() {
        return "/errors/403";
    }
}
