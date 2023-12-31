package fr.graynaud.logbackelasticclientappender.authentication;

/**
 * See <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/token-authentication-services.html#token-authentication-api-key">...</a>
 */
public class ApiKeyAuthentication implements Authentication {

    private String apiKey;

    @Override
    public String getAuthorizationHeader() {
        return "ApiKey " + this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
