package com.oa.service.impl;

import com.oa.common.BusinessException;
import com.oa.entity.Department;
import com.oa.entity.Reimbursement;
import com.oa.entity.Todo;
import com.oa.entity.User;
import com.oa.mapper.DepartmentMapper;
import com.oa.mapper.ReimbursementMapper;
import com.oa.mapper.TodoMapper;
import com.oa.mapper.UserMapper;
import com.oa.service.ReimbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReimbursementServiceImpl implements ReimbursementService {

    private final ReimbursementMapper reimbursementMapper;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;
    private final TodoMapper todoMapper;

    @Override
    public List<Reimbursement> getAllReimbursements() {
        return reimbursementMapper.findAll();
    }

    @Override
    public List<Reimbursement> getMyReimbursements(Long userId) {
        return reimbursementMapper.findByUserId(userId);
    }

    @Override
    public List<Reimbursement> getPendingApprovals(Long departmentId) {
        return reimbursementMapper.findPendingByDepartmentId(departmentId);
    }

    @Override
    public Reimbursement getById(Long id) {
        return reimbursementMapper.findById(id);
    }

    @Override
    @Transactional
    public void submit(Reimbursement r) {
        User user = userMapper.findById(r.getUserId());
        Department dept = departmentMapper.findById(user.getDepartmentId());
        r.setUserName(user.getRealName());
        r.setDepartmentId(user.getDepartmentId());
        r.setDepartmentName(dept.getName());
        r.setStatus(0);
        reimbursementMapper.insert(r);

        Todo todo = new Todo();
        todo.setUserId(dept.getManagerId());
        todo.setType(2);
        todo.setRelatedId(r.getId());
        todo.setTitle("报销申请待审批");
        todo.setContent(user.getRealName() + " 提交了 ￥" + r.getAmount() + " 报销申请");
        todo.setStatus(0);
        todoMapper.insert(todo);
    }

    @Override
    @Transactional
    public void approve(Reimbursement r) {
        Reimbursement existing = reimbursementMapper.findById(r.getId());
        if (existing.getStatus() != 0) {
            throw new BusinessException("该申请已被处理");
        }
        reimbursementMapper.updateStatus(r);
        // 通知申请人
        Todo todo = new Todo();
        todo.setUserId(existing.getUserId());
        todo.setType(2);
        todo.setRelatedId(r.getId());
        todo.setTitle("报销申请" + (r.getStatus() == 2 ? "已通过" : "已驳回"));
        todo.setContent(r.getApproveComment());
        todo.setStatus(0);
        todoMapper.insert(todo);
    }

    @Override
    public void cancel(Long id, Long userId) {
        Reimbursement r = reimbursementMapper.findById(id);
        if (!r.getUserId().equals(userId)) {
            throw new BusinessException("只能撤回自己的申请");
        }
        if (r.getStatus() != 0) {
            throw new BusinessException("只能撤回待审批的申请");
        }
        reimbursementMapper.cancelByUser(id, userId);
    }
}
