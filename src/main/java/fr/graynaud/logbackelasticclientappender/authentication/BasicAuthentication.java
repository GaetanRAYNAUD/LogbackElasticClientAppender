package fr.graynaud.logbackelasticclientappender.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthentication implements Authentication {

    private String username;

    private String password;

    @Override
    public String getAuthorizationHeader() {
        return "Basic " + Base64.getEncoder().encodeToString((this.username + ":" + this.password).getBytes(StandardCharsets.UTF_8));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
