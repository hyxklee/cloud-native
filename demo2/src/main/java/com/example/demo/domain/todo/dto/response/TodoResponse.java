package com.example.demo.domain.todo.dto.response;

import com.example.demo.domain.todo.entity.Todo;

public record TodoResponse(
        Long id,
        String content,
        boolean completed
) {
    public static TodoResponse of(Todo todo) {
        return new TodoResponse(todo.getId(), todo.getContent(), todo.isCompleted());
    }
}
