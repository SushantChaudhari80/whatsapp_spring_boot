package com.example.whatsapp.restcontroller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsapp.service.MonthlyMessageStatsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/stats")
public class MonthlyStatsController {

    @Autowired
    private MonthlyMessageStatsService service;

    @GetMapping("/summary")
    public Map<String, Object> getSummary(HttpSession session) {
        String username = (String) session.getAttribute("username");
        return service.getUserMessageSummary(username);
    }
}
