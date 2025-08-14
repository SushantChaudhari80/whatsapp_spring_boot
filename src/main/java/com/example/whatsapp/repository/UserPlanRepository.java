package com.example.whatsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.whatsapp.entity.UserPlan;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Long>{
	
	@Query(value = "SELECT * FROM user_plans WHERE plan_type = :plan_type ", nativeQuery = true)
	UserPlan getPlan(@Param("plan_type") String plan_type);

}
