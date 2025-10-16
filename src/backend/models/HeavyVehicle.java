package backend.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a heavy vehicle (truck/bus) in the parking system.
 * Extends Vehicle to enforce type-specific behavior and rate.
 */
public class HeavyVehicle extends Vehicle {

    private static final int HOURLY_RATE = 30;       // ₹30 per hour for heavy vehicles
    private static final int MINIMUM_HOURS = 1;      // Minimum chargeable hours
    private static final int GRACE_PERIOD_MIN = 10;  // First 10 minutes free

    /**
     * Constructor for quick entry with only registration number.
     * @param registrationNumber Vehicle registration (e.g., "KA01AB1234")
     */
    public HeavyVehicle(String registrationNumber) {
        super(registrationNumber);
    }

    /**
     * Constructor with full details.
     * @param registrationNumber Vehicle registration
     * @param ownerName Owner's name (optional)
     * @param contactNumber Owner contact number (optional)
     */
    public HeavyVehicle(String registrationNumber, String ownerName, String contactNumber) {
        super(registrationNumber, ownerName, contactNumber);
    }

    @Override
    public String getType() {
        return "HEAVY_VEHICLE";
    }

    @Override
    public int getHourlyRate() {
        return HOURLY_RATE;
    }

    /**
     * Calculates fee for this vehicle based on entry and exit times.
     * @param entryTime Entry timestamp
     * @param exitTime Exit timestamp
     * @return Total fee in rupees
     * @throws IllegalArgumentException if exitTime is before entryTime
     */
    public int calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        long minutesParked = java.time.Duration.between(entryTime, exitTime).toMinutes();
        // Deduct grace period
        long billableMinutes = Math.max(0, minutesParked - GRACE_PERIOD_MIN);
        // Convert to hours, rounding up
        long hours = (billableMinutes + 59) / 60;
        hours = Math.max(hours, MINIMUM_HOURS);

        return (int) (hours * HOURLY_RATE);
    }

    @Override
    public String toString() {
        return String.format(
            "%s [Reg=%s, Entry=%s, Rate=₹%d/hr]",
            getType(),
            getRegistrationNumber(),
            getFormattedEntryTime(),
            HOURLY_RATE
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeavyVehicle)) return false;
        return Objects.equals(getRegistrationNumber(), ((HeavyVehicle) o).getRegistrationNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRegistrationNumber(), getType());
    }
}

