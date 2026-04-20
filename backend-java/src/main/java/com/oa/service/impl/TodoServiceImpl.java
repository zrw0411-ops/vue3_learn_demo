package com.oa.service.impl;

import com.oa.entity.Todo;
import com.oa.mapper.TodoMapper;
import com.oa.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

    private final TodoMapper todoMapper;

    @Override
    public List<Todo> getMyTodos(Long userId) {
        return todoMapper.findByUserId(userId);
    }

    @Override
    public List<Todo> getUnreadTodos(Long userId) {
        return todoMapper.findUnreadByUserId(userId);
    }

    @Override
    public void markAsRead(Long id) {
        todoMapper.updateStatus(id, 1);
    }

    @Override
    public void markAsDone(Long id) {
        todoMapper.updateStatus(id, 2);
    }
}
