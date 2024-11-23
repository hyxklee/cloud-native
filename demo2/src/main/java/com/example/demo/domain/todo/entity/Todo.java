package com.example.demo.domain.todo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "todo_id")
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean completed;

    private Long userId;

    public static Todo of(String content, Long userId) {
        return Todo.builder()
                .completed(false)
                .content(content)
                .userId(userId).build();
    }

    public void update(boolean completed) {
        this.completed = completed;
    }
}
