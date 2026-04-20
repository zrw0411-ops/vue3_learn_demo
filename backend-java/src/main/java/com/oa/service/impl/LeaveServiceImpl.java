package com.oa.service.impl;

import com.oa.common.BusinessException;
import com.oa.entity.Department;
import com.oa.entity.Leave;
import com.oa.entity.Todo;
import com.oa.entity.User;
import com.oa.mapper.DepartmentMapper;
import com.oa.mapper.LeaveMapper;
import com.oa.mapper.TodoMapper;
import com.oa.mapper.UserMapper;
import com.oa.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveMapper leaveMapper;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;
    private final TodoMapper todoMapper;

    @Override
    public List<Leave> getAllLeaves() {
        return leaveMapper.findAll();
    }

    @Override
    public List<Leave> getMyLeaves(Long userId) {
        return leaveMapper.findByUserId(userId);
    }

    @Override
    public List<Leave> getPendingApprovals(Long departmentId) {
        return leaveMapper.findPendingByDepartmentId(departmentId);
    }

    @Override
    public Leave getLeaveById(Long id) {
        return leaveMapper.findById(id);
    }

    @Override
    @Transactional
    public void submitLeave(Leave leave) {
        User user = userMapper.findById(leave.getUserId());
        Department dept = departmentMapper.findById(user.getDepartmentId());
        leave.setUserName(user.getRealName());
        leave.setDepartmentId(user.getDepartmentId());
        leave.setDepartmentName(dept.getName());
        leave.setStatus(0);
        leaveMapper.insert(leave);

        // 创建待办事项通知主管
        Todo todo = new Todo();
        todo.setUserId(dept.getManagerId());
        todo.setType(1);
        todo.setRelatedId(leave.getId());
        todo.setTitle("请假申请待审批");
        todo.setContent(user.getRealName() + " 提交了 " + leave.getDays() + " 天请假申请");
        todo.setStatus(0);
        todoMapper.insert(todo);
    }

    @Override
    @Transactional
    public void approveLeave(Leave leave) {
        Leave existing = leaveMapper.findById(leave.getId());
        if (existing.getStatus() != 0) {
            throw new BusinessException("该申请已被处理");
        }
        // 如果是主管审批（status=1），查找HR继续审批
        if (leave.getStatus() == 1) {
            leave.setApproverId(leave.getApproverId());
            leaveMapper.updateStatus(leave);
            // 通知HR
            List<User> hrs = userMapper.findAll().stream()
                    .filter(u -> u.getRole() == 3 && u.getStatus() == 1)
                    .toList();
            for (User hr : hrs) {
                Todo todo = new Todo();
                todo.setUserId(hr.getId());
                todo.setType(1);
                todo.setRelatedId(leave.getId());
                todo.setTitle("请假申请待HR审批");
                todo.setContent(existing.getUserName() + " 的请假申请主管已通过");
                todo.setStatus(0);
                todoMapper.insert(todo);
            }
        } else {
            leaveMapper.updateStatus(leave);
            // 通知申请人
            Todo todo = new Todo();
            todo.setUserId(existing.getUserId());
            todo.setType(1);
            todo.setRelatedId(leave.getId());
            todo.setTitle("请假申请" + (leave.getStatus() == 2 ? "已通过" : "已驳回"));
            todo.setContent(leave.getApproveComment());
            todo.setStatus(0);
            todoMapper.insert(todo);
        }
    }

    @Override
    public void cancelLeave(Long id, Long userId) {
        Leave leave = leaveMapper.findById(id);
        if (!leave.getUserId().equals(userId)) {
            throw new BusinessException("只能撤回自己的申请");
        }
        if (leave.getStatus() != 0) {
            throw new BusinessException("只能撤回待审批的申请");
        }
        leaveMapper.cancelByUser(id, userId);
    }
}
