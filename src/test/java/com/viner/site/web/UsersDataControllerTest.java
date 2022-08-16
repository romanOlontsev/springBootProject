package com.viner.site.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.exceptions.UserNotFoundException;
import com.viner.site.mappers.UserMapper;
import com.viner.site.service.UserService;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import com.viner.site.web.dto.UpdateUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UsersDataControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @BeforeEach
    void setUp() {
        initMockMvc();
        initUserList();
    }

    private void initMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private void initUserList() {
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
    void getUserByIdTest_shouldReturnUserDto() throws Exception {
        UserDto user = UserMapper.INSTANCE.userToUserDto(userList.get(0));

        when(userService.getUserById(anyLong())).thenReturn(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/data/users/" + user.getId())
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(user.getId()))
               .andExpect(jsonPath("$.username").value(user.getUsername()))
               .andExpect(jsonPath("$.roles").isArray())
               .andExpect(jsonPath("$.roles[0]").value(Objects.requireNonNull(user.getRoles()
                                                                                  .stream()
                                                                                  .findFirst()
                                                                                  .orElse(null))
                                                              .toString()));

        verify(userService, times(1)).getUserById(anyLong());
    }

    @Test
    void getUserByIdTest_shouldThrowException() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(UserNotFoundException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/data/users/" + 322)
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isNotFound())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void getUsersTest_shouldReturnUserDtoList() throws Exception {
        List<UserDto> users = userList.stream()
                                      .map(UserMapper.INSTANCE::userToUserDto)
                                      .collect(Collectors.toList());

        when(userService.getUsers()).thenReturn(users);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/data/users/")
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(users.get(0)
                                                         .getId()))
               .andExpect(jsonPath("$[1].username").value(users.get(1)
                                                               .getUsername()))
               .andExpect(jsonPath("$[0].roles").value(users.get(0)
                                                            .getRoles()
                                                            .stream()
                                                            .findFirst()
                                                            .orElseThrow()
                                                            .toString()))
               .andExpect(jsonPath("$[1].roles").value(users.get(1)
                                                            .getRoles()
                                                            .stream()
                                                            .findFirst()
                                                            .orElseThrow()
                                                            .toString()));
        verify(userService, times(1)).getUsers();
    }

    @Test
    void getUsersTest_shouldReturnEmptyList() throws Exception {
        when(userService.getUsers()).thenReturn(List.of());

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.get("/data/users/")
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isEmpty());

    }

    @Test
    void addNewUserTest_shouldReturnUserDto() throws Exception {
        UserDto user = UserMapper.INSTANCE.userToUserDto(userList.get(0));

        when(userService.addUser(any())).thenReturn(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/data/users/")
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(user.getId()))
               .andExpect(jsonPath("$.username").value(user.getUsername()))
               .andExpect(jsonPath("$.roles[0]").value(Objects.requireNonNull(user.getRoles()
                                                                                  .stream()
                                                                                  .findFirst()
                                                                                  .orElse(null))
                                                              .toString()));

        verify(userService, times(1)).addUser(any());
    }

    @Test
    void addNewUserTest_shouldThrowException() throws Exception {
        when(userService.addUser(any())).thenThrow(UserAlreadyExistsException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/data/users/")
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isConflict())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException));
    }


    @Test
    void updateUserTest_shouldReturnUserDto() throws Exception {
        UserDto user = UserMapper.INSTANCE.userToUserDto(userList.get(1));
        UpdateUserDto updateUser = UserMapper.INSTANCE.userToUpdateUserDto(userList.get(1));

        when(userService.updateUser(anyLong(), any())).thenReturn(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/data/users/" + user.getId())
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(getJsonObject(updateUser));
        mockMvc.perform(builder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(user.getId()))
               .andExpect(jsonPath("$.roles").value(Objects.requireNonNull(user.getRoles()
                                                                               .stream()
                                                                               .findFirst()
                                                                               .orElse(null))
                                                           .toString()));
        verify(userService, times(1)).updateUser(anyLong(), any());
    }

    @Test
    void updateUserTest_shouldThrowException() throws Exception {
        UserDto user = UserMapper.INSTANCE.userToUserDto(userList.get(1));
        UpdateUserDto updateUser = UserMapper.INSTANCE.userToUpdateUserDto(userList.get(1));

        when(userService.updateUser(anyLong(), any())).thenThrow(UserAlreadyExistsException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.patch("/data/users/" + user.getId())
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON)
                                                                      .content(getJsonObject(updateUser));
        mockMvc.perform(builder)
               .andExpect(status().isConflict())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserAlreadyExistsException));
    }

    @Test
    void deleteUserTest_shouldReturnUserDto() throws Exception {
        UserDto user = UserMapper.INSTANCE.userToUserDto(userList.get(1));

        when(userService.deleteUser(anyLong())).thenReturn(user);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/data/users/" + user.getId())
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value(user.getUsername()));
    }

    @Test
    void deleteUserTest_shouldTrowException() throws Exception {
        when(userService.deleteUser(anyLong())).thenThrow(UserNotFoundException.class);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/data/users/" + 322)
                                                                      .with(user("admin").roles("ADMIN"))
                                                                      .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(builder)
               .andExpect(status().isNotFound())
               .andExpect(result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException));
    }

    private String getJsonObject(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
