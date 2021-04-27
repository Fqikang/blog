package com.fqk.blog.service.Impl;


import com.fqk.blog.NotFindException;
import com.fqk.blog.dao.TagRepository;
import com.fqk.blog.pojo.Tag;
import com.fqk.blog.service.TagsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagsService {


    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        tagRepository.save(tag);
        return tag;
    }

    @Transactional
    @Override
    public Tag getTagById(Long id) {
        return tagRepository.getOne(id);
    }

    @Transactional
    @Override
    public Tag updateTag(Tag tag, Long id) {
        Tag t = tagRepository.getOne(id);
        if(t ==null)
        {
            throw new NotFindException("不存在该类型");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public void DeleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAllById(converToList(ids));
    }

    @Override
    public List<Tag> listTag(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable =  PageRequest.of(0,size,sort);
        return tagRepository.findTop(pageable);
    }

    private List<Long> converToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i = 0; i < idarray.length; i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Transactional
    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }


    @Transactional
    @Override
    public Page<Tag> tagList(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Tag findTagByName(String name) {
        return tagRepository.findTagByName(name);
    }

    @Transactional
    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return tagRepository.findTop(pageable);
    }
}
