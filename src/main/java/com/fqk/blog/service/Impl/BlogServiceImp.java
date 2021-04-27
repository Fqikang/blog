package com.fqk.blog.service.Impl;

import com.fqk.blog.NotFindException;
import com.fqk.blog.dao.BlogRepository;
import com.fqk.blog.pojo.Blog;
import com.fqk.blog.pojo.Type;
import com.fqk.blog.service.BlogService;
import com.fqk.blog.utils.MarkDownUtils;
import com.fqk.blog.utils.MyBeanUtils;
import com.fqk.blog.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class BlogServiceImp implements BlogService {

    @Autowired
    BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);

    }

    @Override
    public Blog getBlogAndConvert(Long id) {
        Blog blog = blogRepository.getOne(id);
        if(blog == null) {
            throw  new NotFindException("blog不存在");
        }
        Blog b =new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent( MarkDownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(blogQuery.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blogQuery.getTitle()+"%"));
                }
                if(blogQuery.getTypeId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
                }
                if(blogQuery.isRecomment()){
                    predicates.add(cb.equal(root.<Boolean>get("recomment"),blogQuery.isRecomment()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query,Pageable pageable) {

        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public List<Blog> listRecommendTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable =  PageRequest.of(0,size,sort);

        return blogRepository.findTop(pageable);
    }

    @Override
    public Blog saveBlog(Blog blog) {
        if(blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setCreateTime(new Date());
        }

        return blogRepository.save(blog);

    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.getOne(id);
        if(blog1==null){
            throw new NotFindException("该博客不存在！");
        }
        BeanUtils.copyProperties(blog1,blog, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);

    }
}
