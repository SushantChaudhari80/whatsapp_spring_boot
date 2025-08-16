package com.example.whatsapp.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.whatsapp.service.WhatsAppService;
import com.example.whatsapp.utils.SessionManager;
import com.example.whatsapp.utils.UserPlanChecker;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsappController {

    @Autowired
    private WhatsAppService whatsAppService;
    
    @Autowired
    private UserPlanChecker checker;


    @GetMapping("/status/")
    public ResponseEntity<Object> getWhatsappStatus() {
        Object response = whatsAppService.getStatus(SessionManager.getInstance().getUsername());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/send")
    public ResponseEntity<Object> sendMessage(@RequestBody Map<String, String> payload) {
        String clientId = SessionManager.getInstance().getUsername();
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
            // Construct an appropriate response when message sending is not allowed
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "❌ Message limit exceeded or plan expired. Please upgrade your plan.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
        }

    }
    
    
    // ✅ Reset WhatsApp session
    @GetMapping("/reset/")
    public Object resetSession() {
        return whatsAppService.resetSession(SessionManager.getInstance().getUsername());
    }

    // ✅ Download contacts as CSV
    @GetMapping("/downloadContacts/")
    public ResponseEntity<byte[]> downloadContacts() {
    	//whatsAppService.downloadContacts(SessionManager.getInstance().getUsername());
    	
    	 byte[] fileData = whatsAppService.getFileData(); // Your service call

    	    if (fileData == null || fileData.length == 0) {
    	        return ResponseEntity.notFound().build();
    	    }

    	    return ResponseEntity.ok()
    	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contacts.csv")
    	            .header(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8")
    	            .body(fileData);
    	
    }
    
    
}

