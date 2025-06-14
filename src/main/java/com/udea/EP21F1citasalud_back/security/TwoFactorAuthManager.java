package com.udea.EP21F1citasalud_back.security;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TwoFactorAuthManager {
    public static class TwoFactorCodeInfo {
        private String code;
        private LocalDateTime expiresAt;
        public TwoFactorCodeInfo(String code, LocalDateTime expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }
        public String getCode() { return code; }
        public LocalDateTime getExpiresAt() { return expiresAt; }
    }

    private final Map<String, TwoFactorCodeInfo> codeMap = new ConcurrentHashMap<>();

    public void storeCode(String email, String code, int minutes) {
        codeMap.put(email, new TwoFactorCodeInfo(code, LocalDateTime.now().plusMinutes(minutes)));
    }

    public boolean verifyCode(String email, String code) {
        TwoFactorCodeInfo info = codeMap.get(email);
        if (info == null) return false;
        if (LocalDateTime.now().isAfter(info.getExpiresAt())) {
            codeMap.remove(email);
            return false;
        }
        boolean valid = info.getCode().equals(code);
        if (valid) codeMap.remove(email);
        return valid;
    }

    public void removeCode(String email) {
        codeMap.remove(email);
    }
}
