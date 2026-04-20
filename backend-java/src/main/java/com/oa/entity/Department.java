package com.oa.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Department {
    private Long id;
    private String name;
    private String description;
    private Long managerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
