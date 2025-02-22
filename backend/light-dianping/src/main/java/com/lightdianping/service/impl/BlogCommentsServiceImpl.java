package com.lightdianping.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lightdianping.entity.BlogComments;
import com.lightdianping.mapper.BlogCommentsMapper;
import com.lightdianping.service.IBlogCommentsService;
import org.springframework.stereotype.Service;

@Service
public class BlogCommentsServiceImpl extends ServiceImpl<BlogCommentsMapper, BlogComments> implements IBlogCommentsService {

}
