package com.example.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.whatsapp.entity.MonthlyMessageStats;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthlyMessageStatsRepository extends JpaRepository<MonthlyMessageStats, Long> {
	Optional<MonthlyMessageStats> findByUserIdAndYearAndMonth(String userId, String year, String month);
	List<MonthlyMessageStats> findAllByUserId(String userId);
}