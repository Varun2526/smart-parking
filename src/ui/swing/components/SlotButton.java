package ui.swing.components;

import backend.models.ParkingSlot;

import javax.swing.*;
import java.awt.*;

/**
 * JButton representing a parking slot.
 * Shows color and tooltip based on occupancy and vehicle info.
 */
public class SlotButton extends JButton {

    private final ParkingSlot slot;

    public SlotButton(ParkingSlot slot) {
        this.slot = slot;
        setPreferredSize(new Dimension(60, 60));
        updateAppearance();

        // Optional: show more info on hover
        setToolTipText(getSlotInfo());
        setFocusable(false);
    }

    private void updateAppearance() {
        if (slot.isOccupied()) {
            setBackground(Color.RED);
            setText("Occupied");
        } else {
            setBackground(Color.GREEN);
            setText("Free");
        }
        setOpaque(true);
        setBorderPainted(false);
    }

    private String getSlotInfo() {
        if (slot.isOccupied()) {
            return "Slot ID: " + slot.getSlotId() + "\nVehicle: " + slot.getParkedVehicle().getRegistrationNumber();
        } else {
            return "Slot ID: " + slot.getSlotId() + "\nStatus: Free";
        }
    }
}
