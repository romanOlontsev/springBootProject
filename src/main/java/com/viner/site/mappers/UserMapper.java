package com.viner.site.mappers;

import com.viner.site.entity.UserEntity;
import com.viner.site.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto toUserDto(UserEntity user);
}