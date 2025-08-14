package com.example.whatsapp.utils;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.whatsapp.entity.MonthlyMessageStats;
import com.example.whatsapp.entity.User;
import com.example.whatsapp.entity.UserPlan;
import com.example.whatsapp.repository.MonthlyMessageStatsRepository;
import com.example.whatsapp.repository.UserPlanRepository;
import com.example.whatsapp.repository.UserRepository;

@Service
public class UserPlanChecker {
	
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserPlanRepository planRepo;
	
	@Autowired
	private MonthlyMessageStatsRepository statRepo;
	
	public boolean validateRequest(String clientId) {

	    User user = userRepo.getUserDetails(clientId);
	   
	    UserPlan plan = planRepo.getPlan(user.getPlanType().name());
	  
	    List<MonthlyMessageStats> stats = statRepo.findAllByUserId(clientId);
	    if (stats == null || stats.isEmpty()) {
	        return true; // No stats yet, allow first message
	    }
	    String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

	    // Find current month's stats
	    MonthlyMessageStats currentMonthStats = stats.stream()
	            .filter(stat -> currentMonth.equals(stat.getMonth()))
	            .findFirst()
	            .orElse(null);

	    // If the plan has expired
	    if (user.getPlanEndDate() != null && user.getPlanEndDate().isBefore(LocalDate.now())) {
	        return false;
	    }

	    // If the message limit for the current month has been reached
	    if (currentMonthStats != null 
	            && plan.getMessageCount() != null 
	            && currentMonthStats.getSendMessagesCount() >= plan.getMessageCount()) {
	        return false;
	    }

	    return true; // Valid request
	}


}
