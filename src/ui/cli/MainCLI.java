package ui.cli;

import backend.exceptions.InvalidTokenException;
import backend.exceptions.SlotNotAvailableException;
import backend.exceptions.VehicleNotFoundException;
import backend.models.*;
import backend.services.ParkingService;
import backend.utils.DisplayHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line interface for the Smart Parking Management System.
 */
public class MainCLI {

    private final ParkingService parkingService;
    private final Scanner scanner;

    public MainCLI(ParkingService parkingService) {
        this.parkingService = parkingService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            try {
                DisplayHelper.printMainMenu();
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> handleParkVehicle();
                    case 2 -> handleExitVehicle();
                    case 3 -> handleViewAvailableSlots();
                    case 4 -> handleSearchVehicle();
                    case 5 -> handleViewParkingSummary();
                    case 6 -> {
                        System.out.println("Exiting system. Goodbye!");
                        running = false;
                    }
                    default -> DisplayHelper.printError("Invalid choice. Please enter 1-6.");
                }
            } catch (NumberFormatException e) {
                DisplayHelper.printError("Invalid input. Please enter a number.");
            }
        }
        scanner.close();
    }

    private void handleParkVehicle() {
        System.out.print("Enter registration number: ");
        String regNo = scanner.nextLine().trim();
        if (regNo.isEmpty()) {
            DisplayHelper.printError("Registration number cannot be empty");
            return;
        }

        System.out.print("Enter vehicle type (1=Two Wheeler, 2=Four Wheeler, 3=Heavy Vehicle): ");
        String typeInput = scanner.nextLine();
        Vehicle vehicle;

        try {
            int typeChoice = Integer.parseInt(typeInput);
            switch (typeChoice) {
                case 1 -> vehicle = new TwoWheeler(regNo);
                case 2 -> vehicle = new FourWheeler(regNo);
                case 3 -> vehicle = new HeavyVehicle(regNo);
                default -> {
                    DisplayHelper.printError("Invalid vehicle type choice");
                    return;
                }
            }

            var token = parkingService.parkVehicle(vehicle);
            DisplayHelper.printInfo("Vehicle parked. Token ID: " + token.getTokenId());
            DisplayHelper.printInfo("Slot allocated: " + token.getSlotId());

        } catch (NumberFormatException e) {
            DisplayHelper.printError("Invalid vehicle type input");
        } catch (SlotNotAvailableException | IllegalArgumentException e) {
            DisplayHelper.printError(e.getMessage());
        }
    }

    private void handleExitVehicle() {
        System.out.print("Enter token ID: ");
        String tokenId = scanner.nextLine().trim();
        if (tokenId.isEmpty()) {
            DisplayHelper.printError("Token ID cannot be empty");
            return;
        }
        try {
            int fee = parkingService.exitVehicle(tokenId);
            DisplayHelper.printInfo("Vehicle exited. Parking fee: ₹" + fee);
        } catch (InvalidTokenException e) {
            DisplayHelper.printError(e.getMessage());
        }
    }

    private void handleViewAvailableSlots() {
        List<ParkingFloor> floors = parkingService.getFloors();
        DisplayHelper.displaySlotGrid(floors);
    }

    private void handleSearchVehicle() {
        System.out.print("Enter registration number to search: ");
        String regNo = scanner.nextLine().trim();
        if (regNo.isEmpty()) {
            DisplayHelper.printError("Registration number cannot be empty");
            return;
        }

        try {
            ParkingSlot slot = parkingService.searchVehicle(regNo);
            DisplayHelper.printInfo("Vehicle found in slot: " + slot.getSlotId());
        } catch (VehicleNotFoundException e) {
            DisplayHelper.printError(e.getMessage());
        }
    }

    private void handleViewParkingSummary() {
        List<ParkingFloor> floors = parkingService.getFloors();
        DisplayHelper.printInfo("---- Parking Summary ----");
        for (ParkingFloor floor : floors) {
            long occupied = floor.getSlots().stream().filter(ParkingSlot::isOccupied).count();
            DisplayHelper.printInfo(floor.getFloorId() + ": " + occupied + "/" + floor.getSlots().size() + " occupied");
        }
    }

    public static void main(String[] args) {
        // Set up a sample parking lot with configuration
        List<ParkingFloor> floors = new ArrayList<>();

        ParkingFloor groundFloor = new ParkingFloor("G1");
        // Add slots: IDs and compatible types (example)
        for (int i = 1; i <= 5; i++) {
            groundFloor.addSlot(new ParkingSlot("G1-TW-" + i, "TWO_WHEELER"));
        }
        for (int i = 6; i <= 12; i++) {
            groundFloor.addSlot(new ParkingSlot("G1-FW-" + i, "FOUR_WHEELER"));
        }
        for (int i = 13; i <= 15; i++) {
            groundFloor.addSlot(new ParkingSlot("G1-HV-" + i, "HEAVY_VEHICLE"));
        }

        ParkingFloor firstFloor = new ParkingFloor("F1");
        for (int i = 1; i <= 5; i++) {
            firstFloor.addSlot(new ParkingSlot("F1-TW-" + i, "TWO_WHEELER"));
        }
        for (int i = 6; i <= 12; i++) {
            firstFloor.addSlot(new ParkingSlot("F1-FW-" + i, "FOUR_WHEELER"));
        }
        for (int i = 13; i <= 15; i++) {
            firstFloor.addSlot(new ParkingSlot("F1-HV-" + i, "HEAVY_VEHICLE"));
        }

        floors.add(groundFloor);
        floors.add(firstFloor);

        ParkingService parkingService = new ParkingService(floors);
        MainCLI cli = new MainCLI(parkingService);
        cli.start();
    }
}
