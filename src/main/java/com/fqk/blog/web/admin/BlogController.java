package com.fqk.blog.web.admin;

import com.fqk.blog.pojo.Blog;
import com.fqk.blog.pojo.Type;
import com.fqk.blog.pojo.User;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagsService tagsService;

    //blog管理详情界面
    @GetMapping("/blogs")
    public String blogs(Model model,
                        @PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blogQuery){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));

        return "admin/blog";
    }

    //搜索
    @PostMapping("/blogs/search")
    public String search(Model model,
                        @PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blogQuery){
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));

        return "admin/blog :: blogList";
    }

    //跳转到新增

    @GetMapping("/blogs/input")
    public String blogInput(Model model){
        model.addAttribute("blog",new Blog());
        //返回分类列表
        model.addAttribute("types",typeService.listType());
        //返回标签列表
        model.addAttribute("tags",tagsService.listTag());
        return "admin/blog-input";
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagsService.listTag());
    }

    //跳转到编辑
    @GetMapping("/blogs/input/{id}")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        //对标签进行处理，转换成字符串，类似1，2，3
        blog.init();
        model.addAttribute("blog", blog);
        return "admin/blog-input";
    }

    //新增
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes redirectAttributes, HttpSession session){
        blog.setTags(tagsService.listTag(blog.getTagIds()));
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));

        Blog blog1 = blogService.saveBlog(blog);
        if(blog1 ==null){
            redirectAttributes.addFlashAttribute("message","新增失败");
        }else{
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/blogs";
    }


    @GetMapping("/blogs/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/blogs";
    }


}
