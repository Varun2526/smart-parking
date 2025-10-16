package backend.services;

import backend.exceptions.InvalidTokenException;
import backend.exceptions.SlotNotAvailableException;
import backend.exceptions.VehicleNotFoundException;
import backend.models.*;

import java.time.LocalDateTime;
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

        token.recordExit();
        ParkingSlot slot = findSlotById(token.getSlotId());
        Vehicle vehicle = slot.getParkedVehicle();
        slot.freeSlot();

        vehicleSlotMap.remove(vehicle.getRegistrationNumber());
        activeTokens.remove(tokenId);

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
     * Finds a parking slot by its ID across all floors.
     * @param slotId unique slot identifier
     * @return ParkingSlot with the given ID
     * @throws InvalidTokenException if slot not found
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
}
