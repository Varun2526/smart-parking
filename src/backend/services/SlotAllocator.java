package backend.services;

import backend.exceptions.SlotNotAvailableException;
import backend.models.ParkingFloor;
import backend.models.ParkingSlot;
import backend.models.Vehicle;
import java.util.List;

/**
 * Service responsible for smart allocation of parking slots to vehicles.
 */
public class SlotAllocator {

    /**
     * Finds the best available parking slot for the given vehicle.
     *
     * Prioritizes:
     * 1. Compatible vehicle type.
     * 2. Floors in order (assuming floors are sorted externally in priority order).
     * 3. Slots nearest to the entrance (assuming slots are sorted in order within floor).
     *
     * @param vehicle Vehicle to allocate slot for.
     * @param floors List of parking floors to search.
     * @return Allocated ParkingSlot.
     * @throws SlotNotAvailableException if no suitable slot is available.
     */
    public ParkingSlot findBestSlot(Vehicle vehicle, List<ParkingFloor> floors) throws SlotNotAvailableException {
        if (vehicle == null || floors == null || floors.isEmpty()) {
            throw new IllegalArgumentException("Vehicle and floors must be provided");
        }

        String vehicleType = vehicle.getType();

        for (ParkingFloor floor : floors) {
            List<ParkingSlot> slots = floor.getSlots();

            // Find first available compatible slot
            for (ParkingSlot slot : slots) {
                if (!slot.isOccupied() && vehicleType.equals(slot.getCompatibleType())) {
                    return slot;
                }
            }
        }

        throw new SlotNotAvailableException(vehicle.getType());
    }
}
