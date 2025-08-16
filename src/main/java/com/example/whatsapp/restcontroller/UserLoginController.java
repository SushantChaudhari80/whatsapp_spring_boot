package com.example.whatsapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsapp.request.LoginRequest;
import com.example.whatsapp.service.CustomUserDetailsService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/whatsapp/")
public class UserLoginController {
	
	@Autowired
	private CustomUserDetailsService userService;
	
	@GetMapping("/validate/session")
	public String getSession(HttpSession session) {
		String username = (String) session.getAttribute("username");
		if (username != null) {
			return "Loged in as " + username;
		} else {
			return "Invalid Session";
		}
	}
	
	@PostMapping("/reset/session")
	public void resetSession(HttpSession session) {
		session.invalidate();
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest, HttpSession session) {
		String result = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
		if ("Success".equals(result)) {
			session.setAttribute("username", loginRequest.getUsername());
		}
		return ResponseEntity.ok(result);
	}

}
