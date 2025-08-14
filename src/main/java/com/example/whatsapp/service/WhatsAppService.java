package com.example.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.whatsapp.utils.SessionManager;

import java.util.Map;

@Service
public class WhatsAppService {
	
	@Autowired
	MonthlyMessageStatsService stats;

    private final String WHATSAPP_API = "http://103.91.187.228:8085";
    private final RestTemplate restTemplate = new RestTemplate();

    public Object getStatus(String clientId) {
        String url = WHATSAPP_API + "/getWhatsappStatus/" + clientId;
        return restTemplate.getForObject(url, Object.class);
    }

    public Object sendMessage(String clientId, String groupId, String message) {
        String url = WHATSAPP_API + "/sendMessage";
        Map<String, String> body = Map.of("groupId", groupId, "message", message, "userId", clientId);
        Object res=restTemplate.postForObject(url, body, Object.class);
        
        if (res instanceof Map) {
            Map<String, Object> resMap = (Map<String, Object>) res;
            Boolean success = (Boolean) resMap.get("success");
            if (Boolean.TRUE.equals(success)) {
                stats.incrementMessageCount(SessionManager.getInstance().getUsername());
            } 
        } else {
            System.out.println("Unexpected response format: " + res);
        }
        
        return res;
    }
}
