package com.oa.service.impl;

import com.oa.entity.Department;
import com.oa.mapper.DepartmentMapper;
import com.oa.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentMapper departmentMapper;

    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentMapper.findById(id);
    }

    @Override
    public void addDepartment(Department department) {
        departmentMapper.insert(department);
    }

    @Override
    public void updateDepartment(Department department) {
        departmentMapper.update(department);
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentMapper.deleteById(id);
    }
}
