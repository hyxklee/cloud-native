package com.example.demo.domain.todo.repository;

import com.example.demo.domain.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}