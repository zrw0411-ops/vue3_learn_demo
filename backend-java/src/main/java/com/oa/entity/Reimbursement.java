package com.oa.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Reimbursement {
    private Long id;
    private Long userId;
    private String userName;
    private Long departmentId;
    private String departmentName;
    private String title;
    private BigDecimal amount;
    private Integer type;          // 1=差旅, 2=交通, 3=餐饮, 4=办公, 5=其他
    private String description;
    private String attachment;       // 发票图片路径，多个用逗号分隔
    private Integer status;         // 0=待审批, 1=主管通过, 2=财务通过, 3=老板通过, -1=驳回, -2=已撤回
    private Long approverId;
    private String approverName;
    private String approveComment;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
