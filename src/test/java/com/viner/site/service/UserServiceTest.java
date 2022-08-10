package com.viner.site.service;

import com.viner.site.entity.Role;
import com.viner.site.entity.User;
import com.viner.site.exceptions.UserAlreadyExistsException;
import com.viner.site.exceptions.UserNotFoundException;
import com.viner.site.mappers.UserMapper;
import com.viner.site.repository.UserRepository;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import com.viner.site.web.dto.UpdateUserDto;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private List<User> userList;

    @BeforeEach
    void setUp() {
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
    void addUserTest_shouldAddUser() {
        //given
        AddUserDto addUserDto = UserMapper.INSTANCE.userToAddUserDto(userList.get(0));
        //when
        when(userRepository.save(any()))
                .thenReturn(UserMapper.INSTANCE.addUserDtoToUser(addUserDto));
        UserDto userDto = userService.addUser(addUserDto);
        //then
        assertThat(userDto).isNotNull();
        assertThat(userDto).extracting("username")
                           .isEqualTo(userList.get(0)
                                              .getUsername());
    }

    @Test
    void addUserTest_shouldThrowUserAlreadyExistsException() {
        //given
        AddUserDto addUserDto = UserMapper.INSTANCE.userToAddUserDto(userList.get(0));
        //when
        when(userRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(UserMapper.INSTANCE.addUserDtoToUser(addUserDto)));
        //then
        assertThatThrownBy(() -> userService.addUser(addUserDto))
                .isInstanceOf(UserAlreadyExistsException.class);
    }

    @Test
    void updateUserTest_shouldUpdateAllFields() {
        //given
        User userDb = userList.get(0);
        UpdateUserDto wantToUpdateUser = UserMapper.INSTANCE.userToUpdateUserDto(userList.get(1));
        //when
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(userDb));
        when(userRepository.save(any()))
                .thenReturn(UserMapper.INSTANCE.updateUserDtoToUser(wantToUpdateUser));
        UserDto updatedUser = userService.updateUser(1L, wantToUpdateUser);
        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).extracting("username")
                               .isEqualTo(wantToUpdateUser.getUsername());
        assertThat(updatedUser).extracting("roles")
                               .isEqualTo(wantToUpdateUser.getRoles());
    }

    @Test
    void updateUserTest_shouldUpdateNothing() {
        //given
        User userDb = userList.get(1);

        UpdateUserDto wantToUpdateUser = UpdateUserDto.builder()
                                                      .username("")
                                                      .password("")
                                                      .roles(Collections.emptySet())
                                                      .build();
        //when
        when(userRepository.findById(any()))
                .thenReturn(Optional.of(userDb));
        when(userRepository.save(any()))
                .thenReturn(UserMapper.INSTANCE.updateUserDtoToUser(wantToUpdateUser));
        UserDto updatedUser = userService.updateUser(1L, wantToUpdateUser);
        //then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser).extracting("username")
                               .isEqualTo(userDb.getUsername());
        assertThat(updatedUser).extracting("roles")
                               .isEqualTo(Collections.singleton(Role.ROLE_ADMIN));
    }

    @Test
    void updateUserTest_shouldThrowException() {
        //given
        UpdateUserDto wantToUpdateUser = UserMapper.INSTANCE.userToUpdateUserDto(userList.get(0));
        //when
        when(userRepository.findById(any()))
                .thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> userService.updateUser(1L, wantToUpdateUser))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUsersTest_shouldReturnUserList() {
        //given
        List<User> users = userList;
        //when
        when(userRepository.findAll())
                .thenReturn(users);
        List<UserDto> givenUsers = userService.getUsers();
        //then
        assertThat(givenUsers).isNotNull();
        assertThat(givenUsers.size()).isEqualTo(givenUsers.size());
        assertThat(givenUsers.get(1)
                             .getUsername())
                .isEqualTo(users.get(1)
                                .getUsername());
    }

    @Test
    void getUsersTest_shouldReturnEmptyList() {
        //given
        List<User> userList = List.of();
        //when
        when(userRepository.findAll())
                .thenReturn(userList);
        List<UserDto> users = userService.getUsers();
        //then
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(userList.size());
        assertThat(users).isEmpty();
    }

    @Test
    void getUserByIdTest_shouldReturnUser() {
        //given
        User user = userList.get(1);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userById = userService.getUserById(user.getId());
        //then
        assertThat(userById).isNotNull();
        assertThat(userById).extracting("username")
                            .isEqualTo(user.getUsername());
        assertThat(userById.getRoles())
                .isEqualTo(user.getRoles());
    }


    @Test
    void deleteUserTest_shouldReturnDeletedUser() {
        //given
        User user = userList.get(1);
        //when
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        UserDto deleteUser = userService.deleteUser(user.getId());
        //then
        assertThat(deleteUser).isNotNull();
        assertThat(deleteUser.getRoles())
                .isEqualTo(user.getRoles());
    }
}
