package com.oa.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Leave {
    private Long id;
    private Long userId;
    private String userName;       // 冗余字段，方便显示
    private Long departmentId;
    private String departmentName;
    private Integer leaveType;     // 1=事假, 2=病假, 3=年假, 4=婚假, 5=丧假
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer days;
    private String reason;
    private Integer status;         // 0=待审批, 1=主管通过, 2=HR通过, -1=驳回, -2=已撤回
    private Long approverId;        // 当前审批人
    private String approverName;
    private String approveComment; // 审批意见
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
