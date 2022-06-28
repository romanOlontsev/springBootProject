package com.viner.site.service;

import com.viner.site.entity.TodoEntity;
import com.viner.site.entity.UserEntity;
import com.viner.site.exceptions.UserNotFoundException;
import com.viner.site.mappers.TodoMapper;
import com.viner.site.repository.TodoRepository;
import com.viner.site.repository.UserRepository;
import com.viner.site.service.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoDto createTodo(TodoEntity todo, Long userId) {
        UserEntity foundUser = getUserEntity(userId);
        todo.setUser(foundUser);
        todoRepository.save(todo);
        return TodoMapper.INSTANCE.toTodoDto(todo);
    }

    public TodoDto completeTodo(Long todoId) {
        TodoEntity foundTodo = getTodoEntity(todoId);
        foundTodo.setCompleted(!foundTodo.getCompleted());
        todoRepository.save(foundTodo);
        return TodoMapper.INSTANCE.toTodoDto(foundTodo);
    }

    private UserEntity getUserEntity(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exists"));
    }

    private TodoEntity getTodoEntity(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " does not exists"));
    }
}
