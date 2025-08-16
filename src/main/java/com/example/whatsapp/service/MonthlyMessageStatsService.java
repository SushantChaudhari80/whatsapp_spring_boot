package com.example.whatsapp.service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.whatsapp.entity.MonthlyMessageStats;
import com.example.whatsapp.entity.User;
import com.example.whatsapp.entity.UserPlan;
import com.example.whatsapp.repository.MonthlyMessageStatsRepository;
import com.example.whatsapp.repository.UserPlanRepository;
import com.example.whatsapp.repository.UserRepository;


@Service
public class MonthlyMessageStatsService {

    @Autowired
    private MonthlyMessageStatsRepository repository;

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
	private UserPlanRepository planRepo;
    
    
    public void incrementMessageCount(String username) {
        LocalDate now = LocalDate.now();
        String month = now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // e.g., August
        String year = String.valueOf(now.getYear()); // e.g., 2025

        MonthlyMessageStats stats = repository.findByUserIdAndYearAndMonth(username, year, month)
                .map(existing -> {
                    existing.setSendMessagesCount(existing.getSendMessagesCount() + 1);
                    return existing;
                })
                .orElseGet(() -> {
                    MonthlyMessageStats newStat = new MonthlyMessageStats();
                    newStat.setUserId(username);
                    newStat.setYear(year);
                    newStat.setMonth(month);
                    newStat.setSendMessagesCount(1);
                    return newStat;
                });

        repository.save(stats);
    }
    
    public Map<String, Object> getUserMessageSummary(String username) {
        List<MonthlyMessageStats> allStats = repository.findAllByUserId(username);

        int totalCount = 0;
        int currentMonthCount = 0;

        LocalDate now = LocalDate.now();
        String currentMonth = now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String currentYear = String.valueOf(now.getYear());

        for (MonthlyMessageStats stat : allStats) {
            totalCount += stat.getSendMessagesCount();

            if (stat.getMonth().equalsIgnoreCase(currentMonth)
                    && stat.getYear().equals(currentYear)) {
                currentMonthCount = stat.getSendMessagesCount();
            }
        }
        
        User user = userRepo.getUserDetails(username);
	    UserPlan plan = planRepo.getPlan(user.getPlanType().name());

        Map<String, Object> response = new HashMap<>();
        response.put("count",plan.getMessageCount());
        response.put("total_count", totalCount);
        response.put("current_month", currentMonth);
        response.put("current_month_count", currentMonthCount);

        return response;
        
    }
}
