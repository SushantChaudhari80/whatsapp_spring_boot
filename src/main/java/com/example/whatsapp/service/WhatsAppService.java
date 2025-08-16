package com.example.whatsapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.nio.charset.StandardCharsets;
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
                stats.incrementMessageCount(clientId);
            }
        } else {
            System.out.println("Unexpected response format: " + res);
        }
        return res;
    }
    
    /** 🔄 Reset WhatsApp Session */
    public Object resetSession(String clientId) {
        String url = WHATSAPP_API + "/resetSession/" + clientId;
        return restTemplate.getForObject(url, Object.class);
    }
    
//    public byte[] getFileData() {
//    byte[] fileData = downloadContacts(username); // username should come from session
//    	
//    	//write login here
//    	return fileData;
//    }
    
    public byte[] getFileData(String username) {
        byte[] fileData = downloadContacts(username);
        if (fileData == null || fileData.length == 0) {
            return fileData;
        }
        String csvData = new String(fileData, StandardCharsets.UTF_8);
        String[] lines = csvData.split("\r?\n");
        StringBuilder cleanedCsv = new StringBuilder();
        cleanedCsv.append("number\n");
        for (int i = 1; i < lines.length; i++) {
            String[] cols = lines[i].split(",");
            if (cols.length >= 4) {
                String number = cols[3].replace("\"", "").replace("@c.us", "").trim();
                cleanedCsv.append("\"").append(number).append("\"\n");
            }
        }
        return cleanedCsv.toString().getBytes(StandardCharsets.UTF_8);
    }


    

    /** 📥 Download contacts CSV as byte[] */
    public byte[] downloadContacts(String clientId) {
        String url = WHATSAPP_API + "/downloadContacts/" + clientId;
        byte[] fileData = restTemplate.getForObject(url, byte[].class);

        return fileData;
    }
}
