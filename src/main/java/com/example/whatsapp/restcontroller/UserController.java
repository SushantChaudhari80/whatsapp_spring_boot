package com.example.whatsapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsapp.entity.User;
import com.example.whatsapp.service.CustomUserDetailsService;

@RestController
@RequestMapping("/api/whatsapp")
public class UserController {
	
	@Autowired
	private CustomUserDetailsService userService;

	
	@GetMapping("/user/details")
	public ResponseEntity<User> getUserDetails(){
		return ResponseEntity.ok(userService.getUserDetails());
	}
	
}
