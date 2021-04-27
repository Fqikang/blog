package com.fqk.blog.web;

import com.fqk.blog.NotFindException;
import com.fqk.blog.service.BlogService;
import com.fqk.blog.service.TagsService;
import com.fqk.blog.service.TypeService;
import com.fqk.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TagsService tagsService;
    @Autowired
    private TypeService typeService;


    @GetMapping("/")
    public String index(@PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,Model model
                       ){
        model.addAttribute("page",blogService.listBlog(pageable));

        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagsService.listTagTop(10));
        model.addAttribute("recommend",blogService.listRecommendTop(8));

        return "index";
    }

    @PostMapping("/search")
    public String search(
                         @PageableDefault(size = 6,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query,Model model){

        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));

        model.addAttribute("query",query);
        return "search";
    }


    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){

        model.addAttribute("blog",blogService.getBlogAndConvert(id));

        return "blog";
    }
}
