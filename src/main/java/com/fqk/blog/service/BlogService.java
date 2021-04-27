package com.fqk.blog.service;

import com.fqk.blog.pojo.Blog;
import com.fqk.blog.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BlogService {

    Blog getBlog(Long id);

    //前端将content转成html
    Blog getBlogAndConvert(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    //前端页面blog展示
    Page<Blog> listBlog(Pageable pageable);

    //前端搜索
    Page<Blog> listBlog(String query,Pageable pageable);

    //前端页面推荐展示
    List<Blog> listRecommendTop(Integer size);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

}
