package com.viner.site.config;

import com.viner.site.entity.User;
import com.viner.site.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceConfig implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User foundUser = userRepository.findByUsername(username)
                                       .orElseThrow(() -> new UsernameNotFoundException(
                                               "User with name " + username + " does not exists"));
        return new org.springframework.security.core.userdetails.User(
                username,
                foundUser.getPassword(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("user"));
    }
}
