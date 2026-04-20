package com.oa.service;

import com.oa.entity.Todo;
import java.util.List;

public interface TodoService {
    List<Todo> getMyTodos(Long userId);
    List<Todo> getUnreadTodos(Long userId);
    void markAsRead(Long id);
    void markAsDone(Long id);
}
