package pl.dmcs.blaszczyk.security;

public class JWTConfig {
    private static final String SECRET = "SecretKeyToGenJWTs";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final String HEADER_STRING = "Authorization";
    private static final String SIGN_UP_URL = "/auth/user";

    public static String getSECRET() {
        return SECRET;
    }
    public static long getExpirationTime() {
        return EXPIRATION_TIME;
    }
    public static String getTokenPrefix() {
        return TOKEN_PREFIX;
    }
    public static String getHeaderString() {
        return HEADER_STRING;
    }
    public static String getSignUpUrl() {
        return SIGN_UP_URL;
    }
}
