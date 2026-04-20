package com.oa.service.impl;

import com.oa.entity.Notice;
import com.oa.mapper.NoticeMapper;
import com.oa.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeMapper noticeMapper;

    @Override
    public List<Notice> getPublishedNotices() {
        return noticeMapper.findPublished();
    }

    @Override
    public List<Notice> getAllNotices() {
        return noticeMapper.findAll();
    }

    @Override
    public Notice getById(Long id) {
        return noticeMapper.findById(id);
    }

    @Override
    public void publish(Notice notice) {
        if (notice.getStatus() == 1) {
            notice.setPublishTime(LocalDateTime.now());
        }
        if (notice.getId() == null) {
            noticeMapper.insert(notice);
        } else {
            noticeMapper.update(notice);
        }
    }

    @Override
    public void update(Notice notice) {
        if (notice.getStatus() == 1 && notice.getPublishTime() == null) {
            notice.setPublishTime(LocalDateTime.now());
        }
        noticeMapper.update(notice);
    }

    @Override
    public void delete(Long id) {
        noticeMapper.deleteById(id);
    }
}
