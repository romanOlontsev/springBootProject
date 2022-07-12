package com.viner.site.web.dto;

import com.viner.site.entity.Role;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class AddUserDto {

    @NotNull
    @Size(min = 5, message = "CHTOO")
    private String username;

    @NotNull
    private String password;

    private Set<Role> roles;
}
