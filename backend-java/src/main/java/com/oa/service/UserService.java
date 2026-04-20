package com.oa.service;

import com.oa.entity.User;

import java.util.List;

public interface UserService {
    String login(String username, String password);
    void register(User user);
    User getUserInfo(Long userId);
    List<User> getAllUsers();
    List<User> getUsersByDepartment(Long departmentId);
    void updateUser(User user);
    void updatePassword(Long userId, String oldPassword, String newPassword);
    void updateStatus(Long userId, Integer status);
}
