package fr.graynaud.logbackelasticclientappender.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * See <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/token-authentication-services.html#token-authentication-access-token">...</a>
 */
public class TokenAuthentication implements Authentication {

    private String token;

    @Override
    public String getAuthorizationHeader() {
        return "Bearer " + this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
