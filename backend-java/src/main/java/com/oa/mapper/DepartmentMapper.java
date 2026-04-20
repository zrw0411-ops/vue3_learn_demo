package com.oa.mapper;

import com.oa.entity.Department;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    @Select("SELECT * FROM department ORDER BY id")
    List<Department> findAll();

    @Select("SELECT * FROM department WHERE id = #{id}")
    Department findById(@Param("id") Long id);

    @Insert("INSERT INTO department(name, description, manager_id, create_time) VALUES(#{name}, #{description}, #{managerId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Department department);

    @Update("UPDATE department SET name=#{name}, description=#{description}, manager_id=#{managerId}, update_time=NOW() WHERE id=#{id}")
    int update(Department department);

    @Delete("DELETE FROM department WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
