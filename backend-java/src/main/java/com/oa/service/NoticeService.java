package com.oa.service;

import com.oa.entity.Notice;
import java.util.List;

public interface NoticeService {
    List<Notice> getPublishedNotices();
    List<Notice> getAllNotices();
    Notice getById(Long id);
    void publish(Notice notice);
    void update(Notice notice);
    void delete(Long id);
}
