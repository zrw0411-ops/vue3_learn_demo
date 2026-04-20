package com.oa.controller;

import com.oa.common.Result;
import com.oa.entity.Notice;
import com.oa.entity.User;
import com.oa.mapper.UserMapper;
import com.oa.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final UserMapper userMapper;

    @GetMapping("/public/list")
    public Result<List<Notice>> getPublished() {
        return Result.success(noticeService.getPublishedNotices());
    }

    @GetMapping("/list")
    public Result<List<Notice>> getAll(HttpServletRequest request) {
        Integer role = (Integer) request.getAttribute("role");
        if (role != null && role >= 3) {
            return Result.success(noticeService.getAllNotices());
        }
        return Result.success(noticeService.getPublishedNotices());
    }

    @GetMapping("/{id}")
    public Result<Notice> getById(@PathVariable Long id) {
        return Result.success(noticeService.getById(id));
    }

    @PostMapping("/publish")
    public Result<?> publish(@RequestBody Notice notice, HttpServletRequest request) {
        Long publisherId = (Long) request.getAttribute("userId");
        String publisherName = (String) request.getAttribute("username");
        notice.setPublisherId(publisherId);
        notice.setPublisherName(publisherName);
        noticeService.publish(notice);
        return Result.success("发布成功");
    }

    @PutMapping("/update")
    public Result<?> update(@RequestBody Notice notice) {
        noticeService.update(notice);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        noticeService.delete(id);
        return Result.success("删除成功");
    }
}
