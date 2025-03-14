package com.example.demo.controller;

import com.example.demo.model.Website;
import com.example.demo.model.User;
import com.example.demo.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/dashboard/websites")
public class WebsiteController {

    private final WebsiteService websiteService;

    @Autowired
    public WebsiteController(WebsiteService websiteService) {
        this.websiteService = websiteService;
    }

    @GetMapping
    public String listWebsites(Model model, @AuthenticationPrincipal User user) {
        List<Website> websites = websiteService.getAllWebsitesByUser(user);
        model.addAttribute("websites", websites);
        model.addAttribute("websiteForm", new Website());
        return "dashboard/websites/list";
    }

    @PostMapping("/save")
    public String saveWebsite(@ModelAttribute Website website, 
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        website.setUser(user);
        websiteService.saveWebsite(website);
        redirectAttributes.addFlashAttribute("successMessage", "网站链接已成功保存！");
        return "redirect:/dashboard/websites";
    }

    @GetMapping("/edit/{id}")
    public String editWebsiteForm(@PathVariable Long id, 
                                 Model model, 
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        if (!websiteService.isOwner(id, user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "您没有权限编辑此网站链接！");
            return "redirect:/dashboard/websites";
        }
        
        Website website = websiteService.getWebsiteById(id);
        model.addAttribute("website", website);
        return "dashboard/websites/edit";
    }

    @PostMapping("/update")
    public String updateWebsite(@ModelAttribute Website website,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes redirectAttributes) {
        if (!websiteService.isOwner(website.getId(), user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "您没有权限更新此网站链接！");
            return "redirect:/dashboard/websites";
        }
        
        Website existingWebsite = websiteService.getWebsiteById(website.getId());
        existingWebsite.setTitle(website.getTitle());
        existingWebsite.setUrl(website.getUrl());
        existingWebsite.setDescription(website.getDescription());
        
        websiteService.saveWebsite(existingWebsite);
        redirectAttributes.addFlashAttribute("successMessage", "网站链接已成功更新！");
        return "redirect:/dashboard/websites";
    }

    @GetMapping("/delete/{id}")
    public String deleteWebsite(@PathVariable Long id,
                               @AuthenticationPrincipal User user,
                               RedirectAttributes redirectAttributes) {
        if (!websiteService.isOwner(id, user)) {
            redirectAttributes.addFlashAttribute("errorMessage", "您没有权限删除此网站链接！");
            return "redirect:/dashboard/websites";
        }
        
        websiteService.deleteWebsite(id);
        redirectAttributes.addFlashAttribute("successMessage", "网站链接已成功删除！");
        return "redirect:/dashboard/websites";
    }
} 