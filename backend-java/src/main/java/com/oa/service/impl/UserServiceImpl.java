package com.oa.service.impl;

import com.oa.common.BusinessException;
import com.oa.entity.User;
import com.oa.mapper.UserMapper;
import com.oa.service.UserService;
import com.oa.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public String login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        String md5pwd = DigestUtils.md5DigestAsHex((password).getBytes(StandardCharsets.UTF_8));
        if (!md5pwd.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public void register(User user) {
        if (userMapper.countByUsername(user.getUsername()) > 0) {
            throw new BusinessException("用户名已存在");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setStatus(1);
        userMapper.insert(user);
    }

    @Override
    public User getUserInfo(Long userId) {
        return userMapper.findById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    @Override
    public List<User> getUsersByDepartment(Long departmentId) {
        return userMapper.findByDepartmentId(departmentId);
    }

    @Override
    public void updateUser(User user) {
        userMapper.update(user);
    }

    @Override
    public void updatePassword(Long userId, String oldPassword, String newPassword) {
        User user = userMapper.findById(userId);
        String oldMd5 = DigestUtils.md5DigestAsHex(oldPassword.getBytes(StandardCharsets.UTF_8));
        if (!oldMd5.equals(user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        String newMd5 = DigestUtils.md5DigestAsHex(newPassword.getBytes(StandardCharsets.UTF_8));
        userMapper.updatePassword(userId, newMd5);
    }

    @Override
    public void updateStatus(Long userId, Integer status) {
        userMapper.updateStatus(userId, status);
    }
}
