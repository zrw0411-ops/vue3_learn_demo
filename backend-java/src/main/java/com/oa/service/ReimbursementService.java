package com.oa.service;

import com.oa.entity.Reimbursement;
import java.util.List;

public interface ReimbursementService {
    List<Reimbursement> getAllReimbursements();
    List<Reimbursement> getMyReimbursements(Long userId);
    List<Reimbursement> getPendingApprovals(Long departmentId);
    Reimbursement getById(Long id);
    void submit(Reimbursement r);
    void approve(Reimbursement r);
    void cancel(Long id, Long userId);
}
