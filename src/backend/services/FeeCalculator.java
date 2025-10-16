package backend.services;

import backend.models.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service to calculate parking fees based on entry and exit times and vehicle type rates.
 */
public class FeeCalculator {

    private static final int GRACE_PERIOD_MINUTES = 10;  // First 10 minutes free
    private static final int MINIMUM_CHARGE_HOURS = 1;   // Minimum charge is 1 hour

    /**
     * Calculates the fee for parking a vehicle based on entry and exit times.
     *
     * @param vehicle the vehicle whose rate is used
     * @param entryTime the entry LocalDateTime
     * @param exitTime the exit LocalDateTime
     * @return total fee in rupees
     * @throws IllegalArgumentException if exitTime is before entryTime
     */
    public int calculateFee(Vehicle vehicle, LocalDateTime entryTime, LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        long durationMinutes = Duration.between(entryTime, exitTime).toMinutes();

        // Deduct grace period
        long billableMinutes = durationMinutes - GRACE_PERIOD_MINUTES;
        if (billableMinutes < 0) {
            billableMinutes = 0;
        }

        // Calculate total hours, rounding up fractional hours
        long billableHours = (billableMinutes + 59) / 60;

        // Minimum charge applies
        billableHours = Math.max(billableHours, MINIMUM_CHARGE_HOURS);

        int ratePerHour = vehicle.getHourlyRate();

        return (int) (billableHours * ratePerHour);
    }
}

