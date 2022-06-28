package com.viner.site.mappers;

import com.viner.site.entity.TodoEntity;
import com.viner.site.service.dto.TodoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TodoMapper {
    TodoMapper INSTANCE = Mappers.getMapper(TodoMapper.class);

    TodoDto toTodoDto(TodoEntity todo);
}
