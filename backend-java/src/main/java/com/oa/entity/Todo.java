package com.oa.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Todo {
    private Long id;
    private Long userId;
    private String userName;
    private Integer type;           // 1=请假待审批, 2=报销待审批, 3=系统通知
    private Long relatedId;         // 关联业务ID
    private String title;
    private String content;
    private Integer status;         // 0=未读, 1=已读, 2=已处理
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
