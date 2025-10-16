package backend.models;

import java.util.Objects;

/**
 * Represents an individual parking slot within a floor.
 * Tracks slot ID, compatible vehicle type, and occupancy status.
 */
public class ParkingSlot {
    private final String slotId;            // Unique slot identifier, e.g., “G1-05”
    private final String compatibleType;    // Vehicle type this slot supports
    private Vehicle parkedVehicle;          // Currently parked vehicle, null if empty

    /**
     * Constructor
     * @param slotId Unique ID for the slot (e.g., "G1-05")
     * @param compatibleType Vehicle type allowed (e.g., "TWO_WHEELER")
     */
    public ParkingSlot(String slotId, String compatibleType) {
        if (slotId == null || slotId.isBlank()) {
            throw new IllegalArgumentException("Slot ID cannot be empty");
        }
        if (compatibleType == null || compatibleType.isBlank()) {
            throw new IllegalArgumentException("Compatible type cannot be empty");
        }
        this.slotId = slotId;
        this.compatibleType = compatibleType;
        this.parkedVehicle = null;
    }

    // Getters
    public String getSlotId() {
        return slotId;
    }

    public String getCompatibleType() {
        return compatibleType;
    }

    public boolean isOccupied() {
        return parkedVehicle != null;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    /**
     * Parks a vehicle in this slot.
     * @param vehicle Vehicle to park
     * @throws IllegalStateException if slot is already occupied
     * @throws IllegalArgumentException if vehicle type is not compatible
     */
    public void parkVehicle(Vehicle vehicle) {
        if (isOccupied()) {
            throw new IllegalStateException("Slot " + slotId + " is already occupied");
        }
        if (!vehicle.getType().equals(compatibleType)) {
            throw new IllegalArgumentException(
                "Vehicle type " + vehicle.getType() +
                " not compatible with slot " + slotId + " (" + compatibleType + ")"
            );
        }
        this.parkedVehicle = vehicle;
    }

    /**
     * Removes the parked vehicle, freeing the slot.
     * @return Vehicle that was parked
     * @throws IllegalStateException if slot is already empty
     */
    public Vehicle freeSlot() {
        if (!isOccupied()) {
            throw new IllegalStateException("Slot " + slotId + " is already free");
        }
        Vehicle v = parkedVehicle;
        parkedVehicle = null;
        return v;
    }

    @Override
    public String toString() {
        return String.format(
            "Slot[%s, type=%s, occupied=%s]",
            slotId,
            compatibleType,
            isOccupied() ? "yes (" + parkedVehicle.getRegistrationNumber() + ")" : "no"
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingSlot)) return false;
        ParkingSlot that = (ParkingSlot) o;
        return slotId.equals(that.slotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(slotId);
    }
}
