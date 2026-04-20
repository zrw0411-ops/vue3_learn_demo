package com.oa.mapper;

import com.oa.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM user WHERE username = #{username} AND status = 1 LIMIT 1")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") Long id);

    @Select("SELECT * FROM user WHERE status = 1 ORDER BY create_time DESC")
    @Results({
        @Result(column = "department_id", property = "departmentId")
    })
    List<User> findAll();

    @Select("SELECT * FROM user WHERE department_id = #{departmentId} AND status = 1")
    List<User> findByDepartmentId(@Param("departmentId") Long departmentId);

    @Insert("INSERT INTO user(username, password, real_name, phone, email, department_id, role, status, create_time) " +
            "VALUES(#{username}, #{password}, #{realName}, #{phone}, #{email}, #{departmentId}, #{role}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Update("UPDATE user SET real_name=#{realName}, phone=#{phone}, email=#{email}, department_id=#{departmentId}, update_time=NOW() WHERE id=#{id}")
    int update(User user);

    @Update("UPDATE user SET password=#{password}, update_time=NOW() WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    @Update("UPDATE user SET status=#{status}, update_time=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int countByUsername(@Param("username") String username);
}
