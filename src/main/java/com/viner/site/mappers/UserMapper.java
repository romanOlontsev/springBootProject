package com.viner.site.mappers;

import com.viner.site.entity.User;
import com.viner.site.service.dto.UserDto;
import com.viner.site.web.dto.AddUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    UserDto addUserDtoToUserDto(AddUserDto addUserDto);

    User addUserDtoToUser(AddUserDto addUserDto);
}
