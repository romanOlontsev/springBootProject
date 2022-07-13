package com.viner.site.web.dto;

import com.viner.site.entity.Role;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AddUserDto {

    @Size(min = 5, max = 25, message = "Your username must be between 5 and 25 characters long")
    private String username;

    @NotNull
    private String password;

    private Set<Role> roles;

    public String getUsername() {
        return username;
    }
}
