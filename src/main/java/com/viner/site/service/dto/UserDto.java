package com.viner.site.service.dto;

import com.viner.site.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String username;
    private Set<Role> roles;
}
