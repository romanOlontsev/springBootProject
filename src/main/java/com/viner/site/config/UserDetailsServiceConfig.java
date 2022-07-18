package com.viner.site.config;

import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import com.viner.site.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceConfig implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String adminUserName;

    @Value("${spring.security.user.password}")
    private String adminPassword;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals(adminUserName)) {
            return new org.springframework.security.core.userdetails.User(
                    adminUserName,
                    adminPassword,
                    getAuthority(Set.of(Role.ROLE_ADMIN))
            );
        }

        User foundUser = userRepository.findByUsername(username)
                                       .orElseThrow(() -> new UsernameNotFoundException(
                                               "User with name " + username + " does not exists"));
        return new org.springframework.security.core.userdetails.User(
                username,
                foundUser.getPassword(),
                getAuthority(foundUser.getRoles()));
    }

    private List<GrantedAuthority> getAuthority(Set<Role> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }
}
