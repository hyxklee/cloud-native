package com.example.demo.domain.todo.service;

import com.example.demo.domain.todo.dto.request.TodoSaveRequest;
import com.example.demo.domain.todo.dto.request.TodoUpdateRequest;
import com.example.demo.domain.todo.dto.response.TodoResponse;
import com.example.demo.domain.todo.entity.Todo;
import com.example.demo.domain.todo.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    @Transactional
    public TodoResponse save(TodoSaveRequest dto, HttpSession session) {
        Long userId = authenticate(session);
        Todo todo = Todo.of(dto.content(), userId);
        todoRepository.save(todo);
        return TodoResponse.of(todo);
    }

    public List<TodoResponse> get(HttpSession session) {
        Long userId = authenticate(session);
        List<Todo> todoList = todoRepository.findByUserId(userId);

        return todoList.stream()
                .map(TodoResponse::of)
                .toList();
    }

    @Transactional
    public void check(Long todoId, TodoUpdateRequest dto, HttpSession session) {
        Long userId = authenticate(session);
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("todo가 존재하지 않습니다."));
        todo.update(dto.check());
    }

    @Transactional
    public void delete(Long todoId, HttpSession session) {
        Long userId = authenticate(session);
        todoRepository.deleteById(todoId);
    }

    @Transactional
    public void delete(Long userId) {
        log.info("유저 삭제 요청 발생. 해당 유저의 todo 삭제, {}", userId);
        todoRepository.deleteByUserId(userId);
    }

    private Long authenticate(HttpSession session) {
        log.info("sesseon{}", session.getId());
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("인증 정보가 존재하지 않습니다.");
        }
        return userId;
    }

    public Long getUserId(HttpSession session) {
        log.info("sesseon{}", session.getId());
        Long userId = (Long) session.getAttribute("userId");
        if(userId == null) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        return userId;
    }
}
