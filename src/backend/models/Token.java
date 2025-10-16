package backend.models;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a parking token issued upon vehicle entry.
 * Tracks token ID, associated slot, entry and exit timestamps.
 */
public class Token {
    private final String tokenId;             // Unique token identifier (UUID)
    private final String slotId;              // Associated ParkingSlot ID
    private final String vehicleRegNumber;    // Vehicle registration number
    private final LocalDateTime entryTime;    // Timestamp of entry
    private LocalDateTime exitTime;           // Timestamp of exit, null until exit

    /**
     * Constructor issued when a vehicle is parked.
     * @param slotId ID of the allocated slot
     * @param vehicleRegNumber Registration number of the vehicle
     */
    public Token(String slotId, String vehicleRegNumber) {
        if (slotId == null || slotId.isBlank()) {
            throw new IllegalArgumentException("Slot ID cannot be empty");
        }
        if (vehicleRegNumber == null || vehicleRegNumber.isBlank()) {
            throw new IllegalArgumentException("Vehicle registration cannot be empty");
        }
        this.tokenId = UUID.randomUUID().toString();
        this.slotId = slotId;
        this.vehicleRegNumber = vehicleRegNumber;
        this.entryTime = LocalDateTime.now();
        this.exitTime = null;
    }

    // Getters
    public String getTokenId() {
        return tokenId;
    }

    public String getSlotId() {
        return slotId;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    /**
     * Records exit time. Can be called only once.
     * @throws IllegalStateException if exit already recorded
     */
    public void recordExit() {
        if (this.exitTime != null) {
            throw new IllegalStateException("Exit time already recorded for token " + tokenId);
        }
        this.exitTime = LocalDateTime.now();
    }

    /**
     * Calculates total parked duration in minutes.
     * @return duration between entry and exit in minutes
     * @throws IllegalStateException if exit not yet recorded
     */
    public long getParkedDurationMinutes() {
        if (exitTime == null) {
            throw new IllegalStateException("Exit time not recorded yet for token " + tokenId);
        }
        return java.time.Duration.between(entryTime, exitTime).toMinutes();
    }

    /**
     * Returns formatted entry time for display.
     */
    public String getFormattedEntryTime() {
        return entryTime.format(java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"));
    }

    /**
     * Returns formatted exit time for display.
     * @throws IllegalStateException if exit not yet recorded
     */
    public String getFormattedExitTime() {
        if (exitTime == null) {
            throw new IllegalStateException("Exit time not recorded yet for token " + tokenId);
        }
        return exitTime.format(java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"));
    }

    @Override
    public String toString() {
        return String.format(
            "Token[id=%s, slot=%s, vehicle=%s, entry=%s, exit=%s]",
            tokenId,
            slotId,
            vehicleRegNumber,
            getFormattedEntryTime(),
            exitTime != null ? getFormattedExitTime() : "N/A"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return tokenId.equals(token.tokenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId);
    }
}
