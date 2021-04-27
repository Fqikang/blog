package com.fqk.blog.service;


import com.fqk.blog.pojo.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.*;


public interface TagsService {

    Tag saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag updateTag(Tag tag, Long id);

    void DeleteTag(Long id);

    List<Tag> listTag(String ids);

    //前端首页展示
    List<Tag> listTag(Integer size);

    List<Tag> listTag();

    Page<Tag> tagList(Pageable pageable);

    Tag findTagByName(String name);

    List<Tag> listTagTop(Integer size);
}
