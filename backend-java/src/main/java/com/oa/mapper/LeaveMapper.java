package com.oa.mapper;

import com.oa.entity.Leave;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LeaveMapper {

    @Select("SELECT * FROM leave ORDER BY create_time DESC")
    List<Leave> findAll();

    @Select("SELECT * FROM leave WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Leave> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM leave WHERE department_id = #{departmentId} AND status = 0 ORDER BY create_time DESC")
    List<Leave> findPendingByDepartmentId(@Param("departmentId") Long departmentId);

    @Select("SELECT * FROM leave WHERE id = #{id}")
    Leave findById(@Param("id") Long id);

    @Insert("INSERT INTO leave(user_id, user_name, department_id, department_name, leave_type, start_date, end_date, days, reason, status, create_time) " +
            "VALUES(#{userId}, #{userName}, #{departmentId}, #{departmentName}, #{leaveType}, #{startDate}, #{endDate}, #{days}, #{reason}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Leave leave);

    @Update("UPDATE leave SET status=#{status}, approver_id=#{approverId}, approver_name=#{approverName}, approve_comment=#{approveComment}, update_time=NOW() WHERE id=#{id}")
    int updateStatus(Leave leave);

    @Update("UPDATE leave SET status=-2, update_time=NOW() WHERE id=#{id} AND user_id=#{userId}")
    int cancelByUser(@Param("id") Long id, @Param("userId") Long userId);
}
