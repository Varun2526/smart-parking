package backend.services;

import backend.models.Vehicle;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service to calculate parking fees based on vehicle type and parking duration.
 */
public class FeeCalculator {

    private static final int GRACE_PERIOD_MINUTES = 10;  // 10 minutes free
    private static final int MINIMUM_CHARGE_HOURS = 1;   // minimum charge, 1 hour

    /**
     * Calculate parking fee.
     *
     * @param vehicle   parked vehicle
     * @param entryTime time vehicle entered
     * @param exitTime  time vehicle exited
     * @return fee payable in rupees
     */
    public int calculateFee(Vehicle vehicle, LocalDateTime entryTime, LocalDateTime exitTime) {
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        long totalMinutes = Duration.between(entryTime, exitTime).toMinutes();

        // Subtract grace period
        long billableMinutes = totalMinutes - GRACE_PERIOD_MINUTES;
        if (billableMinutes < 0) {
            billableMinutes = 0;
        }

        // Calculate billable hours, round up partial hours
        long billableHours = (billableMinutes + 59) / 60;

        // Ensure minimum charge applies
        billableHours = Math.max(billableHours, MINIMUM_CHARGE_HOURS);

        int hourlyRate = vehicle.getHourlyRate();

        return (int) (billableHours * hourlyRate);
    }
}
