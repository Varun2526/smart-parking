package backend.models;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class representing any vehicle in the parking system.
 * Handles common properties and validation for all vehicle types.
 */
public abstract class Vehicle {
    protected String registrationNumber;
    protected LocalDateTime entryTime;
    protected String ownerName;
    protected String contactNumber;

    /**
     * Constructor with validation
     * @param registrationNumber Vehicle registration (e.g., "KA01AB1234")
     * @param ownerName Owner's name (optional, can be null)
     * @param contactNumber Contact number (optional, can be null)
     * @throws IllegalArgumentException if registration is invalid
     */
    public Vehicle(String registrationNumber, String ownerName, String contactNumber) {
        validateRegistrationNumber(registrationNumber);
        this.registrationNumber = registrationNumber.toUpperCase().trim();
        this.entryTime = LocalDateTime.now();
        this.ownerName = ownerName;
        this.contactNumber = contactNumber;
    }

    /**
     * Simplified constructor for quick entry (registration only)
     */
    public Vehicle(String registrationNumber) {
        this(registrationNumber, null, null);
    }

    /**
     * Validates registration number format
     * Real-world rules: Must not be empty, min 6 characters, alphanumeric
     */
    private void validateRegistrationNumber(String regNo) {
        if (regNo == null || regNo.trim().isEmpty()) {
            throw new IllegalArgumentException("Registration number cannot be empty");
        }
        
        String cleaned = regNo.trim().replaceAll("\\s+", "");
        
        if (cleaned.length() < 6) {
            throw new IllegalArgumentException(
                "Registration number must be at least 6 characters: " + regNo
            );
        }
        
        // Check if contains at least some alphanumeric characters
        if (!cleaned.matches(".*[A-Za-z0-9].*")) {
            throw new IllegalArgumentException(
                "Registration number must contain alphanumeric characters: " + regNo
            );
        }
    }

    // Getters
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getOwnerName() {
        return ownerName != null ? ownerName : "N/A";
    }

    public String getContactNumber() {
        return contactNumber != null ? contactNumber : "N/A";
    }

    /**
     * Abstract method - each vehicle type defines its own type
     * @return Vehicle type string (e.g., "TWO_WHEELER", "FOUR_WHEELER")
     */
    public abstract String getType();

    /**
     * Abstract method - parking fee rate per hour for this vehicle type
     * @return Hourly rate in rupees
     */
    public abstract int getHourlyRate();

    // Setters for optional fields (in case user wants to update)
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    /**
     * Override equals to compare vehicles by registration number
     * Important for searching and preventing duplicates
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) 
        return true;
        if (o == null || getClass() != o.getClass()) 
        return false;
        Vehicle vehicle = (Vehicle) o;
        return registrationNumber.equals(vehicle.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }

    /**
     * Detailed string representation for logging and debugging
     */
    @Override
    public String toString() {
        return String.format(
            "%s [Reg: %s, Owner: %s, Contact: %s, Entry: %s]",
            getType(),
            registrationNumber,
            getOwnerName(),
            getContactNumber(),
            entryTime.toString()
        );
    }

    /**
     * Get formatted entry time for display
     */
    public String getFormattedEntryTime() {
        return entryTime.format(java.time.format.DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a"));
    }
}
