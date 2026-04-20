package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.Reimbursement;
import com.oa.entity.User;
import com.oa.mapper.UserMapper;
import com.oa.service.ReimbursementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reimbursement")
@RequiredArgsConstructor
public class ReimbursementController {

    private final ReimbursementService reimbursementService;
    private final UserMapper userMapper;

    @GetMapping("/list")
    public Result<List<Reimbursement>> getMyReimbursements(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(reimbursementService.getMyReimbursements(userId));
    }

    @GetMapping("/pending")
    public Result<List<Reimbursement>> getPendingApprovals(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        User user = userMapper.findById(userId);
        return Result.success(reimbursementService.getPendingApprovals(user.getDepartmentId()));
    }

    @GetMapping("/{id}")
    public Result<Reimbursement> getById(@PathVariable Long id) {
        return Result.success(reimbursementService.getById(id));
    }

    @PostMapping("/submit")
    public Result<?> submit(@RequestBody Reimbursement r, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        r.setUserId(userId);
        reimbursementService.submit(r);
        return Result.success("提交成功");
    }

    @PutMapping("/approve")
    public Result<?> approve(@RequestBody Reimbursement r, HttpServletRequest request) {
        Long approverId = (Long) request.getAttribute("userId");
        String approverName = (String) request.getAttribute("username");
        r.setApproverId(approverId);
        r.setApproverName(approverName);
        reimbursementService.approve(r);
        return Result.success("审批成功");
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reimbursementService.cancel(id, userId);
        return Result.success("撤回成功");
    }
}
