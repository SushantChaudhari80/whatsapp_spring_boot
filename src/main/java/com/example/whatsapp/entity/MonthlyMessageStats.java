package com.example.whatsapp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "monthly_message_stats")
public class MonthlyMessageStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "month", nullable = false)
    private String month;  

    @Column(name = "year", nullable = false)
    private String year;  
    
    @Column(name = "send_messages_count")
    private Integer sendMessagesCount = 0;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getSendMessagesCount() {
		return sendMessagesCount;
	}

	public void setSendMessagesCount(Integer sendMessagesCount) {
		this.sendMessagesCount = sendMessagesCount;
	}

	public MonthlyMessageStats(Long id, String userId, String month, String year, Integer sendMessagesCount) {
		super();
		this.id = id;
		this.userId = userId;
		this.month = month;
		this.year = year;
		this.sendMessagesCount = sendMessagesCount;
	}

	public MonthlyMessageStats() {
		super();
		// TODO Auto-generated constructor stub
	}

    
    
}

