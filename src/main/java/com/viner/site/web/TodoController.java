//package com.viner.site.web;
//
//import com.viner.site.entity.TodoEntity;
//import com.viner.site.service.TodoService;
//import com.viner.site.service.dto.TodoDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/todos")
//@RequiredArgsConstructor
//public class TodoController {
//
//    private final TodoService todoService;
//
//    @PostMapping("/{userId}")
//    public TodoDto createTodo(@RequestBody TodoEntity todo,
//                              @PathVariable Long userId) {
//        return todoService.createTodo(todo, userId);
//    }
//
//    @PutMapping
//    public TodoDto completeTodo(@RequestParam Long todoId) {
//        return todoService.completeTodo(todoId);
//    }
//}
