package ui.swing.components;

import backend.exceptions.InvalidTokenException;
import backend.exceptions.SlotNotAvailableException;
import backend.models.Vehicle;
import backend.models.TwoWheeler;
import backend.models.FourWheeler;
import backend.models.HeavyVehicle;
import backend.services.ParkingService;

import javax.swing.*;
import java.awt.*;

/**
 * ControlPanel with spinner-based parking duration input for fee calculation.
 */
public class ControlPanel extends JPanel {

    private final ParkingService parkingService;
    private final Runnable refreshCallback;

    private JTextField regNumberField;
    private JComboBox<String> vehicleTypeCombo;
    private JTextField tokenField;
    private JSpinner hourSpinner;
    private JSpinner minuteSpinner;
    private JTextArea statusArea;

    public ControlPanel(ParkingService parkingService, Runnable refreshCallback) {
        this.parkingService = parkingService;
        this.refreshCallback = refreshCallback;
        setupLayout();
        createComponents();
        updateStatusArea();
    }

    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(320, 0));
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private void createComponents() {
        JLabel title = new JLabel("Parking Control Panel");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        add(Box.createVerticalStrut(15));
        add(createSeparator("Vehicle Entry"));

        add(Box.createVerticalStrut(10));
        add(new JLabel("Registration Number:"));
        add(Box.createVerticalStrut(5));
        regNumberField = new JTextField(15);
        add(regNumberField);

        add(Box.createVerticalStrut(10));
        add(new JLabel("Vehicle Type:"));
        add(Box.createVerticalStrut(5));
        vehicleTypeCombo = new JComboBox<>(new String[]{"Two Wheeler", "Four Wheeler", "Heavy Vehicle"});
        add(vehicleTypeCombo);

        add(Box.createVerticalStrut(15));
        JButton parkBtn = new JButton("Park Vehicle");
        parkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        parkBtn.setBackground(new Color(75, 139, 244));
        parkBtn.setForeground(Color.BLACK);
        parkBtn.setFocusPainted(false);
        parkBtn.addActionListener(e -> parkVehicle());
        add(parkBtn);

        add(Box.createVerticalStrut(25));
        add(createSeparator("Exit Vehicle"));

        add(Box.createVerticalStrut(10));
        add(new JLabel("Token ID:"));
        add(Box.createVerticalStrut(5));
        tokenField = new JTextField(15);
        add(tokenField);

        add(Box.createVerticalStrut(10));
        add(new JLabel("Hours Parked:"));
        add(Box.createVerticalStrut(5));
        hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 23, 1)); 
        add(hourSpinner);

        add(Box.createVerticalStrut(10));
        add(new JLabel("Minutes Parked:"));
        add(Box.createVerticalStrut(5));
        minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1)); 
        add(minuteSpinner);

        add(Box.createVerticalStrut(10));
        JButton calcFeeBtn = new JButton("Calculate Fee");
        calcFeeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        calcFeeBtn.setBackground(new Color(0, 123, 255));
        calcFeeBtn.setForeground(Color.BLACK);
        calcFeeBtn.setFocusPainted(false);
        calcFeeBtn.addActionListener(e -> calculateFee());
        add(calcFeeBtn);

        add(Box.createVerticalStrut(15));
        JButton exitBtn = new JButton("Exit Vehicle");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setBackground(new Color(220, 53, 69));
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setFocusPainted(false);
        exitBtn.addActionListener(e -> exitVehicle());
        add(exitBtn);

        add(Box.createVerticalStrut(20));
        statusArea = new JTextArea(10, 25);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setBorder(BorderFactory.createTitledBorder("Currently Parked Vehicles"));
        add(new JScrollPane(statusArea));

        add(Box.createVerticalGlue());
    }

    private JSeparator createSeparator(String labelText) {
        JPanel sectionHeader = new JPanel();
        sectionHeader.setLayout(new BoxLayout(sectionHeader, BoxLayout.Y_AXIS));
        sectionHeader.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        sectionHeader.add(label);
        sectionHeader.add(Box.createVerticalStrut(4));
        sectionHeader.add(separator);

        return separator;
    }

    private void parkVehicle() {
        try {
            String reg = regNumberField.getText().trim().toUpperCase();
            if (reg.isEmpty()) {
                showError("Please enter a registration number.");
                return;
            }

            String type = (String) vehicleTypeCombo.getSelectedItem();
            Vehicle vehicle = switch (type) {
                case "Two Wheeler" -> new TwoWheeler(reg);
                case "Four Wheeler" -> new FourWheeler(reg);
                default -> new HeavyVehicle(reg);
            };

            var token = parkingService.parkVehicle(vehicle);

            String message = String.format("Vehicle %s parked successfully!\nSlot: %s\nToken: %s",
                    vehicle.getRegistrationNumber(), token.getSlotId(), token.getTokenId());
            showInfo(message);

            regNumberField.setText("");
            refreshCallback.run();
            updateStatusArea();

        } catch (SlotNotAvailableException | IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void calculateFee() {
        try {
            String tokenId = tokenField.getText().trim();

            int hours = (Integer) hourSpinner.getValue();
            int minutes = (Integer) minuteSpinner.getValue();

            if (tokenId.isEmpty()) {
                showError("Please enter Token ID.");
                return;
            }
            if (hours == 0 && minutes == 0) {
                showError("Please select duration greater than zero.");
                return;
            }

            int totalMinutes = hours * 60 + minutes;

            java.time.LocalDateTime exitTime = java.time.LocalDateTime.now();
            java.time.LocalDateTime entryTime = exitTime.minusMinutes(totalMinutes);

            int fee = parkingService.calculateFeeForToken(tokenId, entryTime, exitTime);

            showInfo("Parking Fee: ₹" + fee + "\nEntry: " + entryTime + "\nExit: " + exitTime);

        } catch (backend.exceptions.InvalidTokenException e) {
            showError(e.getMessage());
        }
    }

    private void exitVehicle() {
        try {
            String tokenId = tokenField.getText().trim();
            if (tokenId.isEmpty()) {
                showError("Please enter a token ID.");
                return;
            }

            int fee = parkingService.exitVehicle(tokenId);

            String message = "Vehicle exited successfully!\nParking Fee: ₹" + fee;
            showInfo(message);

            tokenField.setText("");
            hourSpinner.setValue(0);
            minuteSpinner.setValue(0);
            refreshCallback.run();
            updateStatusArea();

        } catch (backend.exceptions.InvalidTokenException e) {
            showError(e.getMessage());
        }
    }

    private void updateStatusArea() {
        String fullStatus = parkingService.getAllParkedVehiclesInfo();
        statusArea.setText(fullStatus);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
