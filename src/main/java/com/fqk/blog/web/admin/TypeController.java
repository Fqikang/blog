package com.fqk.blog.web.admin;


import com.fqk.blog.pojo.Type;
import com.fqk.blog.service.TypeService;
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
public class TypeController {

    @Autowired
    TypeService typeService;

    @GetMapping("/types")
    public String type(@PageableDefault(size=5,sort = {"id"},direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        typeService.listType(pageable);
        return "admin/type";
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "admin/type-input";
    }

    @GetMapping("/types/input/{id}")
    public String editInput(@PathVariable("id") Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "admin/type-input";
    }

    //form表单提交,并进行判空验证和相似验证
    //后端校验，@Valid绑定验证的对象，BindingResult存有message
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult bindingResult, RedirectAttributes redirectAttributes){
//        判断是否重复
        Type name = typeService.getTypeByName(type.getName());
        if(name!=null){
            bindingResult.rejectValue("name","nameError","不能添加重复的分类");
        }
//        结合@NotBlank验证使用
        if (bindingResult.hasErrors()) {
            return "admin/type-input";
        }

        Type type1 = typeService.saveType(type);
        if(type1 ==null){
            //
            redirectAttributes.addFlashAttribute("message","操作失败");
        }else{
           //
            redirectAttributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/types";
    }

    //修改type,判断修改的type是否存在
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type, BindingResult bindingResult, @PathVariable("id") Long id, RedirectAttributes redirectAttributes){
//        判断是否重复
        Type name = typeService.getTypeByName(type.getName());
        if(name!=null){
            bindingResult.rejectValue("name","nameError","不能添加重复的分类");
        }
//        结合@NotBlank验证使用
        if (bindingResult.hasErrors()) {
            return "admin/type-input";
        }

        Type type1 = typeService.updateType(id,type);
        if(type1 ==null){
            //
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else{
            //
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    @GetMapping("/types/delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }
}







