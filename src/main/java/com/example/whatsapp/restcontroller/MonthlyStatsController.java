package com.example.whatsapp.restcontroller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.whatsapp.service.MonthlyMessageStatsService;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class MonthlyStatsController {

    @Autowired
    private MonthlyMessageStatsService service;

    @GetMapping("/summary")
    public Map<String, Object> getSummary() {
        return service.getUserMessageSummary();
    }
}
