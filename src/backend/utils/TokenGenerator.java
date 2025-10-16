package backend.utils;

import java.util.UUID;

/**
 * Utility class for generating unique tokens for parking entries.
 */
public class TokenGenerator {
    /**
     * Generates a new unique token string.
     * @return a UUID-based token string
     */
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
