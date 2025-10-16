package backend.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a parking floor containing multiple slots.
 * Manages slots and provides queries for availability.
 */
public class ParkingFloor {
    private final String floorId;                   // Identifier (e.g., "G1", "F2")
    private final List<ParkingSlot> slots;          // All slots on this floor

    /**
     * Constructor to initialize a parking floor.
     * @param floorId Unique floor identifier
     */
    public ParkingFloor(String floorId) {
        if (floorId == null || floorId.isBlank()) {
            throw new IllegalArgumentException("Floor ID cannot be empty");
        }
        this.floorId = floorId;
        this.slots = new ArrayList<>();
    }

    /**
     * Adds a slot to this floor.
     * @param slot ParkingSlot object
     * @throws IllegalArgumentException if slot is null or already exists
     */
    public void addSlot(ParkingSlot slot) {
        if (slot == null) {
            throw new IllegalArgumentException("Slot cannot be null");
        }
        if (slots.contains(slot)) {
            throw new IllegalArgumentException("Slot " + slot.getSlotId() + " already exists on floor " + floorId);
        }
        slots.add(slot);
    }

    /**
     * Returns all slots on this floor.
     * @return Unmodifiable list of slots
     */
    public List<ParkingSlot> getSlots() {
        return Collections.unmodifiableList(slots);
    }

    /**
     * Finds first available slot compatible with the given vehicle type.
     * @param vehicleType Type string (e.g., "TWO_WHEELER")
     * @return Optional containing the slot if found, empty otherwise
     */
    public ParkingSlot findAvailableSlot(String vehicleType) {
        for (ParkingSlot slot : slots) {
            if (!slot.isOccupied() && slot.getCompatibleType().equals(vehicleType)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Counts available slots on this floor.
     * @return number of free slots
     */
    public long countAvailableSlots() {
        return slots.stream().filter(slot -> !slot.isOccupied()).count();
    }

    /**
     * Returns floor identifier.
     */
    public String getFloorId() {
        return floorId;
    }

    /**
     * Detailed string of floor status.
     */
    @Override
    public String toString() {
        long occupied = slots.stream().filter(ParkingSlot::isOccupied).count();
        return String.format(
            "Floor %s: %d/%d occupied",
            floorId, occupied, slots.size()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParkingFloor)) return false;
        ParkingFloor that = (ParkingFloor) o;
        return floorId.equals(that.floorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(floorId);
    }
}
