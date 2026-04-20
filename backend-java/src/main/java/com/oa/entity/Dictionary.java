package com.oa.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Dictionary {
    private Long id;
    private String category;       // 字典分类
    private String label;          // 标签名
    private String value;          // 值
    private Integer sort;
    private Integer status;         // 1=启用, 0=禁用
    private LocalDateTime createTime;
}
