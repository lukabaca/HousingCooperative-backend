package pl.dmcs.blaszczyk.model.Response;

public class JWTAuthenticationResponse {
    private String token;
    private String tokenType = "Bearer";

    public JWTAuthenticationResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
