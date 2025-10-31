package com.itmentorcommunityplatform.authservice.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TelegramInitDataValidator {

    @Value("${telegram.bot-token}")
    private String botToken;

    public User validate(String initData, long expirationSeconds) throws InvalidInitDataException {
        try {
            Map<String, String> dataMap = parseInitData(initData);
            validateHash(dataMap);
            validateAuthDate(dataMap, expirationSeconds);

            Long telegramUserId = extractTelegramUserId(dataMap);
            User user = new User();
            user.setTelegramUserId(telegramUserId);
            log.info("InitData validated successfully for telegramUserId={}", telegramUserId);
            return user;

        } catch (InvalidInitDataException e) {
            log.warn("InitData validation failed: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during initData validation", e);
            throw new InvalidInitDataException("Invalid initData format");
        }
    }


    private Map<String, String> parseInitData(String initData) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx > 0 && idx < pair.length() - 1) {
                String key = pair.substring(0, idx);
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
                map.put(key, value);
            }
        }
        return map;
    }

    private void validateHash(Map<String, String> dataMap) throws Exception {
        String receivedHash = dataMap.remove("hash");
        if (receivedHash == null) throw new InvalidInitDataException("Missing hash");

        String dataCheckString = buildDataCheckString(dataMap);
        String calculatedHash = hmacSha256Hex(botToken.getBytes(StandardCharsets.UTF_8), dataCheckString);

        if (!calculatedHash.equalsIgnoreCase(receivedHash)) {
            throw new InvalidInitDataException("Invalid signature");
        }
    }

    private String buildDataCheckString(Map<String, String> dataMap) {
        return dataMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }

    private void validateAuthDate(Map<String, String> dataMap, long expirationSeconds) throws InvalidInitDataException {
        long authDate = Long.parseLong(dataMap.getOrDefault("auth_date", "0"));
        long now = System.currentTimeMillis() / 1000;
        if (now - authDate > expirationSeconds) {
            throw new InvalidInitDataException("InitData expired");
        }
    }

    private long extractTelegramUserId(Map<String, String> dataMap) throws Exception {
        String userJson = dataMap.get("user");
        if (userJson == null) throw new InvalidInitDataException("Missing user data");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(userJson);
        return node.get("id").asLong();
    }

    private String hmacSha256Hex(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(key, "HmacSHA256"));
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }
}

