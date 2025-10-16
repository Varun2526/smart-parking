package backend.exceptions;

/**
 * Thrown when no compatible parking slots are available for the requested vehicle type.
 */
public class SlotNotAvailableException extends Exception {
    
    /**
     * Constructs a new exception with a detailed message.
     *
     * @param vehicleType the type of vehicle for which allocation was attempted
     */
    public SlotNotAvailableException(String vehicleType) {
        super("No available parking slots for vehicle type: " + vehicleType);
    }
}
