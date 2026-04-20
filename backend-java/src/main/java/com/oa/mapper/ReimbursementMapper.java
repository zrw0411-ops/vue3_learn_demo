package com.oa.mapper;

import com.oa.entity.Reimbursement;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReimbursementMapper {

    @Select("SELECT * FROM reimbursement ORDER BY create_time DESC")
    List<Reimbursement> findAll();

    @Select("SELECT * FROM reimbursement WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Reimbursement> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM reimbursement WHERE department_id = #{departmentId} AND status = 0 ORDER BY create_time DESC")
    List<Reimbursement> findPendingByDepartmentId(@Param("departmentId") Long departmentId);

    @Select("SELECT * FROM reimbursement WHERE id = #{id}")
    Reimbursement findById(@Param("id") Long id);

    @Insert("INSERT INTO reimbursement(user_id, user_name, department_id, department_name, title, amount, type, description, attachment, status, create_time) " +
            "VALUES(#{userId}, #{userName}, #{departmentId}, #{departmentName}, #{title}, #{amount}, #{type}, #{description}, #{attachment}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reimbursement r);

    @Update("UPDATE reimbursement SET status=#{status}, approver_id=#{approverId}, approver_name=#{approverName}, approve_comment=#{approveComment}, update_time=NOW() WHERE id=#{id}")
    int updateStatus(Reimbursement r);

    @Update("UPDATE reimbursement SET status=-2, update_time=NOW() WHERE id=#{id} AND user_id=#{userId}")
    int cancelByUser(@Param("id") Long id, @Param("userId") Long userId);
}
