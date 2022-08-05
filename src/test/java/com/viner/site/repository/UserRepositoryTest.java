package com.viner.site.repository;


import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_shouldReturnUser() {
        //given
        User user = User.builder()
                        .username("Alex")
                        .password("3221")
                        .roles(Collections.singleton(Role.ROLE_USER))
                        .build();
        userRepository.save(user);

        //when
        User foundUser = userRepository.findByUsername("Alex")
                                       .orElse(null);

        //then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
    }

    @Test
    public void findByUsername_shouldReturnNull() {
        //given
        User user = User.builder()
                        .username("Marat")
                        .password("1233")
                        .roles(Collections.singleton(Role.ROLE_USER))
                        .build();
        userRepository.save(user);

        //when
        User foundUser = userRepository.findByUsername("Alex")
                                       .orElse(null);

        //then
        assertThat(foundUser).isNull();
    }

    @Test
    public void findById_shouldReturnUser() {
        //given
        User user = User.builder()
                        .username("Oleg")
                        .password("442211")
                        .roles(Collections.singleton(Role.ROLE_ADMIN))
                        .build();
        userRepository.save(user);

        //when
        User foundUser = userRepository.findById(user.getId())
                                       .orElse(null);

        //then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser).extracting("username").isEqualTo("Oleg");
    }

    @Test
    public void findById_shouldReturnNull() {
        //given
        User user = User.builder()
                        .username("Marat")
                        .password("1233")
                        .roles(Collections.singleton(Role.ROLE_USER))
                        .build();
        userRepository.save(user);

        //when
        User foundUser = userRepository.findById(12L)
                                       .orElse(null);

        //then
        assertThat(foundUser).isNull();
    }
}
