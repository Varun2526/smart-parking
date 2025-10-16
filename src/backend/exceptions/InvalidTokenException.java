package backend.exceptions;

/**
 * Thrown when an operation references a token that is invalid,
 * non-existent, or has already been used.
 */
public class InvalidTokenException extends Exception {
    
    /**
     * Constructs a new exception with a detailed message.
     *
     * @param tokenId the token identifier that is invalid
     */
    public InvalidTokenException(String tokenId) {
        super("Invalid or expired token: " + tokenId);
    }
}
