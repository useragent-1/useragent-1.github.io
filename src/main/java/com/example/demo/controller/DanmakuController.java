package com.example.demo.controller;

import com.example.demo.model.Danmaku;
import com.example.demo.model.User;
import com.example.demo.service.DanmakuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/danmaku")
public class DanmakuController {

    private final DanmakuService danmakuService;
    private final Random random = new Random();

    @Autowired
    public DanmakuController(DanmakuService danmakuService) {
        this.danmakuService = danmakuService;
    }

    @GetMapping
    public String danmakuPage(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "danmaku/wall";
    }

    @GetMapping("/api/list")
    @ResponseBody
    public List<Danmaku> listDanmakus() {
        return danmakuService.findRecentDanmakus();
    }

    @PostMapping("/api/send")
    @ResponseBody
    public ResponseEntity<?> sendDanmaku(
            @RequestParam String content,
            @RequestParam(required = false) Integer colorType,
            @AuthenticationPrincipal User user
    ) {
        if (content == null || content.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "弹幕内容不能为空");
            return ResponseEntity.badRequest().body(error);
        }

        if (content.length() > 200) {
            content = content.substring(0, 200);
        }

        if (colorType == null) {
            colorType = random.nextInt(3) + 1;
        }

        Danmaku danmaku = danmakuService.save(content, user, colorType);

        return ResponseEntity.ok(danmaku);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteDanmaku(@PathVariable Long id) {
        danmakuService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 