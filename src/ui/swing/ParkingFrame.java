package ui.swing;

import backend.models.ParkingFloor;
import backend.models.ParkingSlot;
import backend.services.ParkingService;
import ui.swing.components.ControlPanel;
import ui.swing.components.SlotButton;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main JFrame for Smart Parking System GUI.
 * Shows parking slot grid and a control panel for operations.
 */
public class ParkingFrame extends JFrame {

    private final ParkingService parkingService;
    private final JPanel slotGridPanel;
    private final ControlPanel controlPanel;

    public ParkingFrame(ParkingService parkingService) {
        this.parkingService = parkingService;

        setTitle("Smart Parking Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        slotGridPanel = new JPanel();
        slotGridPanel.setLayout(new GridLayout(0, 12, 5, 5));  // 12 columns

        controlPanel = new ControlPanel(parkingService, this::refreshSlots);

        add(slotGridPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);

        refreshSlots();
    }

    /**
     * Refresh the grid of parking slots.
     * Called after parking/exit to update UI state.
     */
    public void refreshSlots() {
    slotGridPanel.removeAll();
    slotGridPanel.setLayout(new BoxLayout(slotGridPanel, BoxLayout.Y_AXIS)); // Vertical stacking of floor rows

    List<ParkingFloor> floors = parkingService.getFloors();

    for (ParkingFloor floor : floors) {
        // Create horizontal row panel for this floor
        JPanel floorRow = new JPanel();
        floorRow.setLayout(new BoxLayout(floorRow, BoxLayout.X_AXIS));
        floorRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        floorRow.setBorder(BorderFactory.createTitledBorder("Floor: " + floor.getFloorId()));

        // Add all slots in this floor as SlotButtons to the row
        for (ParkingSlot slot : floor.getSlots()) {
            SlotButton button = new SlotButton(slot);
            floorRow.add(button);
            floorRow.add(Box.createRigidArea(new Dimension(5, 0))); // spacing between slots
        }

        slotGridPanel.add(floorRow);
        slotGridPanel.add(Box.createRigidArea(new Dimension(0, 10)));  // spacing between floor rows
    }

    slotGridPanel.revalidate();
    slotGridPanel.repaint();
}

    public static void main(String[] args) {
        // Initialize backend parking data (similar to CLI setup)
        var floors = List.of(
                createFloor("G1"),
                createFloor("F1")
        );
        var parkingService = new ParkingService(floors);

        SwingUtilities.invokeLater(() -> {
            ParkingFrame frame = new ParkingFrame(parkingService);
            frame.setVisible(true);
        });
    }

    private static ParkingFloor createFloor(String floorId) {
        ParkingFloor floor = new ParkingFloor(floorId);
        for (int i = 1; i <= 5; i++)
            floor.addSlot(new ParkingSlot(floorId + "-TW-" + i, "TWO_WHEELER"));
        for (int i = 6; i <= 12; i++)
            floor.addSlot(new ParkingSlot(floorId + "-FW-" + i, "FOUR_WHEELER"));
        for (int i = 13; i <= 15; i++)
            floor.addSlot(new ParkingSlot(floorId + "-HV-" + i, "HEAVY_VEHICLE"));
        return floor;
    }
}
