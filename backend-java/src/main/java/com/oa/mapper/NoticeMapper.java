package com.oa.mapper;

import com.oa.entity.Notice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoticeMapper {

    @Select("SELECT * FROM notice WHERE status = 1 ORDER BY publish_time DESC")
    List<Notice> findPublished();

    @Select("SELECT * FROM notice ORDER BY create_time DESC")
    List<Notice> findAll();

    @Select("SELECT * FROM notice WHERE id = #{id}")
    Notice findById(@Param("id") Long id);

    @Insert("INSERT INTO notice(title, content, publisher_id, publisher_name, type, status, publish_time, create_time) " +
            "VALUES(#{title}, #{content}, #{publisherId}, #{publisherName}, #{type}, #{status}, #{publishTime}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notice notice);

    @Update("UPDATE notice SET title=#{title}, content=#{content}, type=#{type}, status=#{status}, publish_time=#{publishTime}, update_time=NOW() WHERE id=#{id}")
    int update(Notice notice);

    @Delete("DELETE FROM notice WHERE id = #{id}")
    int deleteById(@Param("id") Long id);
}
