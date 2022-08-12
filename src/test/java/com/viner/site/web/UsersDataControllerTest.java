package com.viner.site.web;

import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import com.viner.site.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UsersDataControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        User userFirst = User.builder()
                             .id(1L)
                             .username("Andrey")
                             .password("1122")
                             .roles(Collections.singleton(Role.ROLE_USER))
                             .build();
        User userSecond = User.builder()
                              .id(2L)
                              .username("Maria")
                              .password("2132")
                              .roles(Collections.singleton(Role.ROLE_ADMIN))
                              .build();
        userList = List.of(userFirst, userSecond);
    }

    @Test
    void getUserByIdTest() throws Exception {
        User user = userList.get(0);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/data/users/1");
        mockMvc.perform(builder
                .with(user("admin").roles("ADMIN")))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1));
//                .andExpect(jsonPath("$.tickets[0].id").value(0));

        verify(userRepository, times(1)).findById(any());
    }
}
