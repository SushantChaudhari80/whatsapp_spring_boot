package com.example.whatsapp.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.whatsapp.service.WhatsAppService;
import com.example.whatsapp.utils.UserPlanChecker;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappController {

    @Autowired
    private WhatsAppService whatsAppService;
    
    @Autowired
    private UserPlanChecker checker;


    @GetMapping("/status/")
    public ResponseEntity<Object> getWhatsappStatus(HttpSession session) {
        String username = (String) session.getAttribute("username");
        Object response = whatsAppService.getStatus(username);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/send")
    public ResponseEntity<Object> sendMessage(@RequestBody Map<String, String> payload, HttpSession session) {
        String clientId = (String) session.getAttribute("username");
        String groupId = payload.get("groupId");
        String message = payload.get("message");

        if (clientId == null || groupId == null || message == null) {
            return ResponseEntity.badRequest().body("Missing required parameters");
        }

        boolean isValid = checker.validateRequest(clientId);

        if (isValid) {
            Object response = whatsAppService.sendMessage(clientId, groupId, message);
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "❌ Message limit exceeded or plan expired. Please upgrade your plan.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }
    }
    
    
    @PostMapping("/reset")
    public Object resetSession(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
            String url = "http://103.91.187.228:8085/resetSession/" + username;
            session.invalidate();
            return new ResponseEntity<>("Session reset for user: " + username, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("No active session to reset.", HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ Download contacts as CSV
    @GetMapping("/downloadContacts/")
    public ResponseEntity<byte[]> downloadContacts(HttpSession session) {
    //whatsAppService.downloadContacts(username); // username should come from session
    	String username = (String) session.getAttribute("username");
    	
    	 byte[] fileData = whatsAppService.getFileData(username); // Your service call

    	    if (fileData == null || fileData.length == 0) {
    	        return ResponseEntity.notFound().build();
    	    }

    	    return ResponseEntity.ok()
    	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.csv")
    	            .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
    	            .body(fileData);
    	
    }
    
    
}

