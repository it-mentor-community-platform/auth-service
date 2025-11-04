package com.itmentorcommunityplatform.authservice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itmentorcommunityplatform.authservice.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
        Map<String, String> data = parseInitData(initData);

        validateHash(data);
        validateAuthDate(data, expirationSeconds);

        long telegramUserId = extractTelegramUserId(data);
        User user = new User();
        user.setTelegramUserId(telegramUserId);
        return user;
    }

    private Map<String, String> parseInitData(String initData) {
        if (initData.startsWith("initData=")) {
            initData = URLDecoder.decode(initData.substring("initData=".length()), StandardCharsets.UTF_8);
        }

        Map<String, String> map = new HashMap<>();
        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf('=');
            if (idx <= 0) continue;
            String key = pair.substring(0, idx);
            String rawValue = pair.substring(idx + 1);
            String value = URLDecoder.decode(rawValue, StandardCharsets.UTF_8);
            map.put(key, value);
        }

        log.info("Parsed initData map");
        return map;
    }


    private void validateHash(Map<String, String> data) {
        String receivedHash = data.remove("hash");
        if (receivedHash == null)
            throw new InvalidInitDataException("Missing hash");

        String dataCheckString = data.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));

        byte[] secretKey = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, "WebAppData".getBytes(StandardCharsets.UTF_8))
                .hmac(botToken.getBytes(StandardCharsets.UTF_8));


        String calculatedHash = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey)
                .hmacHex(dataCheckString);

        if (!calculatedHash.equalsIgnoreCase(receivedHash))
            throw new InvalidInitDataException("Invalid hash");
    }

    private void validateAuthDate(Map<String, String> data, long expirationSeconds) {
        String authDateStr = data.get("auth_date");
        if (authDateStr == null)
            throw new InvalidInitDataException("Missing auth_date");

        long authDate = Long.parseLong(authDateStr);
        long now = System.currentTimeMillis() / 1000;

        if (authDate > now + 300)
            throw new InvalidInitDataException("Invalid auth_date: from the future");

        if (now - authDate > expirationSeconds)
            throw new InvalidInitDataException("InitData expired");
    }

    private long extractTelegramUserId(Map<String, String> data) {
        try {
            String userJson = data.get("user");
            if (userJson == null)
                throw new InvalidInitDataException("Missing user");

            return new ObjectMapper().readTree(userJson).get("id").asLong();
        } catch (Exception e) {
            throw new InvalidInitDataException("Invalid user JSON");
        }
    }
}


