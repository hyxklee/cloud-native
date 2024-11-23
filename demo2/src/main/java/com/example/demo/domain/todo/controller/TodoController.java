package com.example.demo.domain.todo.controller;

import com.example.demo.domain.todo.dto.request.TodoSaveRequest;
import com.example.demo.domain.todo.dto.request.TodoUpdateRequest;
import com.example.demo.domain.todo.dto.response.TodoResponse;
import com.example.demo.domain.todo.service.TodoService;
import com.example.demo.global.config.response.CommonResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "TODO")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    @PostMapping
    public CommonResponse<TodoResponse> addTodo(@RequestBody TodoSaveRequest dto, HttpSession session) {
        TodoResponse response = todoService.save(dto, session);
        return CommonResponse.createSuccess("todo 추가에 성공했습니다", response);
    }

    @GetMapping
    public CommonResponse<List<TodoResponse>> getTodos(HttpSession session) {
        List<TodoResponse> response = todoService.get(session);
        return CommonResponse.createSuccess("todo list 조회에 성공했습니다.", response);
    }

    @PatchMapping("/{todoId}")
    public CommonResponse<String> updateTodo(@PathVariable Long todoId, @RequestBody TodoUpdateRequest dto, HttpSession session) {
        todoService.check(todoId, dto, session);
        return CommonResponse.createSuccess("todo 변경에 성공했습니다.");
    }

    @DeleteMapping("/{todoId}")
    public CommonResponse<String> deleteTodo(@PathVariable Long todoId, HttpSession session) {
        todoService.delete(todoId, session);
        return CommonResponse.createSuccess("todo 삭제에 성공했습니다.");
    }

    @DeleteMapping("/user/{userId}")
    public CommonResponse<String> deleteTodo(@PathVariable Long userId) {
        todoService.delete(userId);
        return CommonResponse.createSuccess("해당 유저의 todo 삭제에 성공했습니다.");
    }

    @GetMapping("/auth")
    public CommonResponse<Long> auth(HttpSession session) {
        return CommonResponse.createSuccess("로그인된 사용자 ID 조회에 성공했습니다.", todoService.getUserId(session));
    }

}
