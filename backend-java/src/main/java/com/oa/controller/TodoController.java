package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.Todo;
import com.oa.service.TodoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/list")
    public Result<List<Todo>> getMyTodos(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(todoService.getMyTodos(userId));
    }

    @GetMapping("/unread")
    public Result<List<Todo>> getUnread(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(todoService.getUnreadTodos(userId));
    }

    @PutMapping("/read/{id}")
    public Result<?> markAsRead(@PathVariable Long id) {
        todoService.markAsRead(id);
        return Result.success("已标记为已读");
    }

    @PutMapping("/done/{id}")
    public Result<?> markAsDone(@PathVariable Long id) {
        todoService.markAsDone(id);
        return Result.success("已处理");
    }
}
