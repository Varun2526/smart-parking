package backend.utils;

import backend.models.Token;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading parking tokens to a persistent file.
 */
public class TokenStorage {

    private static final String FILE_PATH = "tokens.txt";

    /**
     * Saves a token to disk.
     * @param token the token to store
     */
    public static void saveToken(Token token) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(token.getTokenId() + "," + token.getSlotId() + "," + token.getVehicleRegNumber());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving token: " + e.getMessage());
        }
    }

    /**
     * Loads all stored tokens from file.
     * @return list of tokens as strings
     */
    public static List<String> loadTokens() {
        List<String> tokens = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tokens.add(line);
            }
        } catch (IOException e) {
            System.err.println("No existing token records found.");
        }
        return tokens;
    }
}
