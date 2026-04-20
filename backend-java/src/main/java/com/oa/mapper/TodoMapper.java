package com.oa.mapper;

import com.oa.entity.Todo;
import org.apache.ibatis.annotations.*;

import java.util.List;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface TodoMapper {

    @Select("SELECT * FROM todo WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<Todo> findByUserId(@Param("userId") Long userId);

    @Select("SELECT * FROM todo WHERE user_id = #{userId} AND status = 0 ORDER BY create_time DESC")
    List<Todo> findUnreadByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO todo(user_id, user_name, type, related_id, title, content, status, create_time) " +
            "VALUES(#{userId}, #{userName}, #{type}, #{relatedId}, #{title}, #{content}, #{status}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Todo todo);

    @Update("UPDATE todo SET status=#{status}, update_time=NOW() WHERE id=#{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    @Delete("DELETE FROM todo WHERE related_id = #{relatedId} AND type = #{type}")
    int deleteByRelatedIdAndType(@Param("relatedId") Long relatedId, @Param("type") Integer type);
}
