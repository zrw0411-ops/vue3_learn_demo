package com.oa.service;

import com.oa.entity.Leave;
import java.util.List;

public interface LeaveService {
    List<Leave> getAllLeaves();
    List<Leave> getMyLeaves(Long userId);
    List<Leave> getPendingApprovals(Long departmentId);
    Leave getLeaveById(Long id);
    void submitLeave(Leave leave);
    void approveLeave(Leave leave);
    void cancelLeave(Long id, Long userId);
}
