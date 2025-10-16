package backend.exceptions;

/**
 * Thrown when a search operation fails to find a vehicle
 * with the specified registration number.
 */
public class VehicleNotFoundException extends Exception {

    /**
     * Constructs a new exception with a detailed message.
     *
     * @param registrationNumber the registration number that was not found
     */
    public VehicleNotFoundException(String registrationNumber) {
        super("Vehicle not found with registration number: " + registrationNumber);
    }
}
