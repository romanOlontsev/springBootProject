package com.viner.site.web.dto;

import com.viner.site.entity.Role;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class UpdateUserDto {

    private String username;

    private String password;

    private Set<Role> roles;
}