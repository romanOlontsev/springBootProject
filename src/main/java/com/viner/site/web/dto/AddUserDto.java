package com.viner.site.web.dto;

import com.viner.site.entity.Role;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AddUserDto {

    @Size(min = 3, max = 25, message = "Your username must be between 3 and 25 characters long")
    private String username;

    @Size(min = 5, max = 25, message = "Your password must be between 5 and 25 characters long")
    private String password;

    private Set<Role> roles;
}
