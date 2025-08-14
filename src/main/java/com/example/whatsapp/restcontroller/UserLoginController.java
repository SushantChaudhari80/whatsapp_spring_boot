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
import com.example.whatsapp.utils.SessionManager;

@RestController
@RequestMapping("/api/whatsapp/")
public class UserLoginController {
	
	@Autowired
	private CustomUserDetailsService userService;
	
	@GetMapping("/validate/session")
	public String getSession() {
		if(SessionManager.getInstance().getUsername() != null) {
			return "Loged in as "+SessionManager.getInstance().getUsername();
		}else {
			return "Invalid Session";
		}
	}
	
	@PostMapping("/reset/session")
	public void resetSession() {
		SessionManager.getInstance().setUsername(null);
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
	    return ResponseEntity.ok(userService.login(loginRequest.getUsername(), loginRequest.getPassword()));
	}

}
