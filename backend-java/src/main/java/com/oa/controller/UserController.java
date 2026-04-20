package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.User;
import com.oa.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody Map<String, String> params) {
        String token = userService.login(params.get("username"), params.get("password"));
        return Result.success("登录成功", Map.of("token", token));
    }

    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        userService.register(user);
        return Result.success("注册成功");
    }

    @GetMapping("/info")
    public Result<User> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getUserInfo(userId));
    }

    @GetMapping("/list")
    public Result<List<User>> getAllUsers() {
        return Result.success(userService.getAllUsers());
    }

    @GetMapping("/department/{departmentId}")
    public Result<List<User>> getUsersByDepartment(@PathVariable Long departmentId) {
        return Result.success(userService.getUsersByDepartment(departmentId));
    }

    @PutMapping("/update")
    public Result<?> updateUser(@RequestBody User user, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        user.setId(userId);
        userService.updateUser(user);
        return Result.success("更新成功");
    }

    @PutMapping("/password")
    public Result<?> updatePassword(@RequestBody Map<String, String> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updatePassword(userId, params.get("oldPassword"), params.get("newPassword"));
        return Result.success("密码修改成功");
    }

    @PutMapping("/status/{userId}")
    public Result<?> updateStatus(@PathVariable Long userId, @RequestParam Integer status) {
        userService.updateStatus(userId, status);
        return Result.success("状态更新成功");
    }
}
