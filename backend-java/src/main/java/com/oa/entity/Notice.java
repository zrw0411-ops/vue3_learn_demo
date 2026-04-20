package com.oa.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notice {
    private Long id;
    private String title;
    private String content;
    private Long publisherId;
    private String publisherName;
    private Integer type;           // 1=系统通知, 2=公司公告, 3=人事通知
    private Integer status;         // 1=发布, 0=草稿
    private LocalDateTime publishTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
