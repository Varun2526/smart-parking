package backend.utils;

import backend.models.ParkingFloor;
import backend.models.ParkingSlot;

import java.util.List;

/**
 * Utility class for displaying menus, grids, and messages to the console.
 */
public class DisplayHelper{

    /**
     * Prints the main menu options to the console.
     */
    public static void printMainMenu() {
        System.out.println("\n=== SMART PARKING MANAGEMENT SYSTEM ===");
        System.out.println("1. Park Vehicle");
        System.out.println("2. Exit Vehicle");
        System.out.println("3. View Available Slots");
        System.out.println("4. Search Vehicle");
        System.out.println("5. View Parking Summary");
        System.out.println("6. Exit");
        System.out.print("Enter choice: ");
    }

    /**
     * Displays the status of all slots across floors.
     list of ParkingFloor objects
     */
    public static void displaySlotGrid(List<ParkingFloor> floors) {
        System.out.println("\n--- Parking Slots Status ---");
        for (ParkingFloor floor : floors) {
            System.out.println(floor.getFloorId() + ": ");
            for (ParkingSlot slot : floor.getSlots()) {
                String symbol = slot.isOccupied() ? "[X]" : "[ ]";
                System.out.print(symbol);
            }
            System.out.println();
        }
    }

    
    public static void printError(String message) {
        System.err.println("Error: " + message);
    }

   
    public static void printInfo(String message) {
        System.out.println(message);
    }
}