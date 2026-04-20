package com.oa.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    private Long departmentId;
    private Integer role;        // 1=普通员工, 2=主管, 3=HR, 4=管理员
    private Integer status;      // 1=正常, 0=禁用
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
