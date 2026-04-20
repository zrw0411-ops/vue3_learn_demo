package com.oa.service;

import com.oa.entity.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    void addDepartment(Department department);
    void updateDepartment(Department department);
    void deleteDepartment(Long id);
}
