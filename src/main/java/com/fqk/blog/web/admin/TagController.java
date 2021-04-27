package com.fqk.blog.web.admin;


import com.fqk.blog.pojo.Tag;
import com.fqk.blog.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagsService tagsService;

    //展示tag页面
    @GetMapping("/tags")
    public String tags(Model model, @PageableDefault(size = 5, sort = {"id"},
                        direction = Sort.Direction.DESC)
                        Pageable pageable) {
        model.addAttribute("page", tagsService.tagList(pageable));
        return "admin/tag";
    }

    //跳转到新增页面
    @GetMapping("/tags/input")
    public String pages(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag-input";
    }

    //跳转到编辑页面
    @GetMapping("/tags/input/{id}")
    public String updateTags(@PathVariable("id") Long id, Model model) {
        model.addAttribute("tag", tagsService.getTagById(id));
        return "admin/tag-input";
    }

    //新增提交
    @PostMapping("/tags")
    public String input(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes attributes) {
        if (tagsService.findTagByName(tag.getName()) != null) {
            bindingResult.rejectValue("name", "ErrorName", "分类名重复");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tag-input";
        }
        Tag t = tagsService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "新增失败");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    //编辑提交
    @PostMapping("/tags/{id}")
    public String updateInput(@Valid Tag tag, BindingResult bindingResult, @PathVariable("id") Long id, RedirectAttributes attributes) {
        Tag tag1 = tagsService.findTagByName(tag.getName());
        if (tag1 != null) {
            bindingResult.rejectValue("name", "NameError", "分类名重复");
        }
        if (bindingResult.hasErrors()) {
            return "admin/tag-input";
        }
        Tag t = tagsService.updateTag(tag, id);
        if (t == null) {
            attributes.addFlashAttribute("message", "修改失败");
        } else {
            attributes.addFlashAttribute("message", "修改成功");
        }
        return "redirect:/admin/tags";
    }

    //删除
    @GetMapping("/tags/delete/{id}")
    public String delete(@PathVariable("id")Long id, RedirectAttributes attributes) {
        tagsService.DeleteTag(id);
        attributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/tags";
    }
}
