package backend.services;

import backend.exceptions.InvalidTokenException;
import backend.exceptions.SlotNotAvailableException;
import backend.exceptions.VehicleNotFoundException;
import backend.models.*;
import backend.utils.TokenStorage;
import java.util.*;

/**
 * Core parking service managing vehicle entry, exit, searches,
 * and overall parking lot state.
 */
public class ParkingService {

    private final List<ParkingFloor> floors;
    private final Map<String, Token> activeTokens;           // Map tokenId -> Token
    private final Map<String, ParkingSlot> vehicleSlotMap;   // Map registrationNumber -> ParkingSlot

    private final SlotAllocator slotAllocator;
    private final FeeCalculator feeCalculator;

    public ParkingService(List<ParkingFloor> floors) {
        if (floors == null || floors.isEmpty()) {
            throw new IllegalArgumentException("At least one floor must be provided");
        }
        this.floors = floors;
        this.activeTokens = new HashMap<>();
        this.vehicleSlotMap = new HashMap<>();
        this.slotAllocator = new SlotAllocator();
        this.feeCalculator = new FeeCalculator();
    }

    /**
     * Parks a vehicle and generates a parking token.
     * @param vehicle vehicle to park
     * @return token generated for parked vehicle
     * @throws SlotNotAvailableException if no slots available
     * @throws IllegalArgumentException if vehicle is already parked
     */
    public synchronized Token parkVehicle(Vehicle vehicle) throws SlotNotAvailableException {
        String regNo = vehicle.getRegistrationNumber();
        if (vehicleSlotMap.containsKey(regNo)) {
            throw new IllegalArgumentException("Vehicle with registration " + regNo + " is already parked");
        }

        ParkingSlot slot = slotAllocator.findBestSlot(vehicle, floors);
        slot.parkVehicle(vehicle);

        Token token = new Token(slot.getSlotId(), regNo);

        // ============================
        // Save token to file for persistence
        // ============================
        TokenStorage.saveToken(token);
        System.out.println("Token saved: " + token.getTokenId() + " | Vehicle: " + regNo + " | Slot: " + slot.getSlotId());

        activeTokens.put(token.getTokenId(), token);
        vehicleSlotMap.put(regNo, slot);

        return token;
    }

    /**
     * Exits a vehicle from the parking using the token ID.
     * @param tokenId token received when parking
     * @return fee calculated for the parking duration
     * @throws InvalidTokenException if token invalid or already used
     */
    public synchronized int exitVehicle(String tokenId) throws InvalidTokenException {
    Token token = activeTokens.get(tokenId);
    if (token == null) {
        throw new InvalidTokenException(tokenId);
    }
    if (token.getExitTime() != null) {
        throw new InvalidTokenException("Token " + tokenId + " has already been used to exit");
    }

    // Record the exit time
    token.recordExit();

    // Find slot by ID
    ParkingSlot slot = findSlotById(token.getSlotId());

    // Get the vehicle parked in the slot
    Vehicle vehicle = slot.getParkedVehicle();

    // Free the parking slot
    slot.freeSlot();

    // Remove vehicle from vehicle to slot lookup
    vehicleSlotMap.remove(vehicle.getRegistrationNumber());

    // Remove token from active tokens
    activeTokens.remove(tokenId);

    // Calculate parking fee
    return feeCalculator.calculateFee(vehicle, token.getEntryTime(), token.getExitTime());
}

    /**
     * Searches for a parked vehicle by registration number.
     * @param registrationNumber vehicle registration
     * @return ParkingSlot where vehicle is parked
     * @throws VehicleNotFoundException if not found
     */
    public synchronized ParkingSlot searchVehicle(String registrationNumber) throws VehicleNotFoundException {
        ParkingSlot slot = vehicleSlotMap.get(registrationNumber);
        if (slot == null) {
            throw new VehicleNotFoundException(registrationNumber);
        }
        return slot;
    }

    /**
     * Returns all floors managed by the service.
     */
    public List<ParkingFloor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    /**
     * Helper to find slot by its ID across all floors.
     */
    private ParkingSlot findSlotById(String slotId) throws InvalidTokenException {
        for (ParkingFloor floor : floors) {
            for (ParkingSlot slot : floor.getSlots()) {
                if (slot.getSlotId().equals(slotId)) {
                    return slot;
                }
            }
        }
        throw new InvalidTokenException("Parking slot not found: " + slotId);
    }
    public synchronized String getAllParkedVehiclesInfo() {
    StringBuilder sb = new StringBuilder();
    if (activeTokens.isEmpty()) {
        sb.append("No vehicles currently parked.");
    } else {
        for (Token token : activeTokens.values()) {
            sb.append("Registration: ").append(token.getVehicleRegNumber())
              .append("\nSlot: ").append(token.getSlotId())
              .append("\nToken: ").append(token.getTokenId())
              .append("\n--------------------\n");
        }
    }
    return sb.toString();
}
public synchronized int calculateFeeForToken(String tokenId, java.time.LocalDateTime entryTime, java.time.LocalDateTime exitTime) throws InvalidTokenException {
    Token token = activeTokens.get(tokenId);
    if (token == null) {
        throw new InvalidTokenException(tokenId);
    }
    Vehicle vehicle = findSlotById(token.getSlotId()).getParkedVehicle();
    return feeCalculator.calculateFee(vehicle, entryTime, exitTime);
}

}
