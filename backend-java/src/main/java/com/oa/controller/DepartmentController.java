package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.Department;
import com.oa.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @GetMapping("/list")
    public Result<List<Department>> getAll() {
        return Result.success(departmentService.getAllDepartments());
    }

    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        return Result.success(departmentService.getDepartmentById(id));
    }

    @PostMapping("/add")
    public Result<?> add(@RequestBody Department department) {
        departmentService.addDepartment(department);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Department department) {
        departmentService.updateDepartment(department);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.success("删除成功");
    }
}
