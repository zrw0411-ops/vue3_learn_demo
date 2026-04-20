package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.Leave;
import com.oa.entity.User;
import com.oa.mapper.UserMapper;
import com.oa.service.LeaveService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveService leaveService;
    private final UserMapper userMapper;

    @GetMapping("/list")
    public Result<List<Leave>> getMyLeaves(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(leaveService.getMyLeaves(userId));
    }

    @GetMapping("/pending")
    public Result<List<Leave>> getPendingApprovals(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userMapper.findById(userId);
        return Result.success(leaveService.getPendingApprovals(user.getDepartmentId()));
    }

    @GetMapping("/{id}")
    public Result<Leave> getById(@PathVariable Long id) {
        return Result.success(leaveService.getLeaveById(id));
    }

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody Leave leave, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        leave.setUserId(userId);
        leaveService.submitLeave(leave);
        return Result.success("提交成功");
    }

    @PutMapping("/approve")
    public Result<?> approve(@RequestBody Leave leave, HttpServletRequest request) {
        Long approverId = (Long) request.getAttribute("userId");
        String approverName = (String) request.getAttribute("username");
        leave.setApproverId(approverId);
        leave.setApproverName(approverName);
        leaveService.approveLeave(leave);
        return Result.success("审批成功");
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        leaveService.cancelLeave(id, userId);
        return Result.success("撤回成功");
    }
}
