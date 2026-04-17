package Execution;

import Vehicles.Car;
import Vehicles.Bus;
import Vehicles.Truck;
import Vehicles.Airplane;
import Vehicles.CargoShip;
import Vehicles.Vehicle;
import Interfaces.FuelConsumable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SimulatorGUI extends JFrame {
    private static final int NUM_VEHICLES = 5;
    private static final String[] VEHICLE_COLORS = {
            "#4CAF50", "#2196F3", "#FF9800", "#9C27B0", "#F44336"
    };
    private static final String[] VEHICLE_ICONS = {
            "\uD83D\uDE97", "\uD83D\uDE8C", "\uD83D\uDE9A", "\u2708\uFE0F", "\uD83D\uDEF3"
    };

    private List<VehicleRunner> runners = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();
    private JPanel vehiclePanel;
    private JLabel totalDistanceLabel;
    private JLabel statusLabel;
    private JButton startBtn;
    private JButton pauseBtn;
    private JButton resumeBtn;
    private JButton stopBtn;
    private JButton refuelBtn;
    private JCheckBox syncCheckBox;
    private List<JLabel> vehicleLabels = new ArrayList<>();
    private List<JPanel> vehicleRows = new ArrayList<>();

    public SimulatorGUI() {
        setTitle("Fleet Highway Simulator");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.decode("#1E1E2E"));

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createVehicleListPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        initVehicles();

        startBtn.addActionListener(e -> startSimulation());
        pauseBtn.addActionListener(e -> pauseSimulation());
        resumeBtn.addActionListener(e -> resumeSimulation());
        stopBtn.addActionListener(e -> stopSimulation());
        refuelBtn.addActionListener(e -> refuelAll());

        Timer timer = new Timer(100, e -> updateDisplay());
        timer.start();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(Color.decode("#1E1E2E"));
        titlePanel.setBorder(new EmptyBorder(15, 15, 5, 15));

        JLabel titleLabel = new JLabel("Fleet Highway Simulator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.decode("#CDD6F4"));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Real-time multithreaded vehicle simulation with race condition demonstration", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(Color.decode("#7F849C"));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(subtitleLabel);

        return titlePanel;
    }

    private JScrollPane createVehicleListPanel() {
        vehiclePanel = new JPanel();
        vehiclePanel.setLayout(new BoxLayout(vehiclePanel, BoxLayout.Y_AXIS));
        vehiclePanel.setBackground(Color.decode("#1E1E2E"));
        vehiclePanel.setBorder(new EmptyBorder(5, 10, 5, 10));

        JScrollPane scrollPane = new JScrollPane(vehiclePanel);
        scrollPane.setBackground(Color.decode("#1E1E2E"));
        scrollPane.getViewport().setBackground(Color.decode("#1E1E2E"));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.decode("#45475A"), 1),
                "Vehicle Status",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13),
                Color.decode("#BAC2DE")
        ));
        return scrollPane;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(Color.decode("#181825"));
        controlPanel.setBorder(new EmptyBorder(10, 15, 15, 15));

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        statsPanel.setBackground(Color.decode("#181825"));

        totalDistanceLabel = new JLabel("Shared Counter: 0 km | Actual Sum: 0 km", SwingConstants.CENTER);
        totalDistanceLabel.setFont(new Font("Consolas", Font.BOLD, 15));
        totalDistanceLabel.setForeground(Color.decode("#A6E3A1"));
        totalDistanceLabel.setOpaque(true);
        totalDistanceLabel.setBackground(Color.decode("#1E1E2E"));
        totalDistanceLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#45475A"), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        statsPanel.add(totalDistanceLabel);

        statusLabel = new JLabel("Status: Ready", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        statusLabel.setForeground(Color.decode("#89B4FA"));
        statusLabel.setOpaque(true);
        statusLabel.setBackground(Color.decode("#1E1E2E"));
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.decode("#45475A"), 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        statsPanel.add(statusLabel);

        controlPanel.add(statsPanel, BorderLayout.NORTH);

        JPanel bottomRow = new JPanel(new BorderLayout(10, 0));
        bottomRow.setBackground(Color.decode("#181825"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        buttonPanel.setBackground(Color.decode("#181825"));

        startBtn = createStyledButton("Start", "#4CAF50");
        pauseBtn = createStyledButton("Pause", "#FF9800");
        resumeBtn = createStyledButton("Resume", "#2196F3");
        stopBtn = createStyledButton("Stop", "#F44336");
        refuelBtn = createStyledButton("Refuel All", "#9C27B0");
        JButton resetBtn = createStyledButton("Reset", "#607D8B");

        buttonPanel.add(startBtn);
        buttonPanel.add(pauseBtn);
        buttonPanel.add(resumeBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(refuelBtn);
        buttonPanel.add(resetBtn);

        resetBtn.addActionListener(e -> resetSimulation());

        bottomRow.add(buttonPanel, BorderLayout.CENTER);

        syncCheckBox = new JCheckBox("Sync Lock", false);
        syncCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        syncCheckBox.setForeground(Color.decode("#CDD6F4"));
        syncCheckBox.setBackground(Color.decode("#181825"));
        syncCheckBox.setFocusPainted(false);
        syncCheckBox.addActionListener(e -> {
            SharedHighway.setSynchronizationEnabled(syncCheckBox.isSelected());
            updateSyncStatus();
        });
        bottomRow.add(syncCheckBox, BorderLayout.EAST);

        controlPanel.add(bottomRow, BorderLayout.SOUTH);

        return controlPanel;
    }

    private JButton createStyledButton(String text, String colorHex) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.decode(colorHex));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setPreferredSize(new Dimension(100, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.decode(colorHex).brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(Color.decode(colorHex));
            }
        });
        return btn;
    }

    private void initVehicles() {
        Vehicle[] vehicles = {
                new Car("V0", "Sedan", 180, 0, 4),
                new Bus("V1", "CityBus", 100, 0, 6),
                new Truck("V2", "Hauler", 120, 0, 8),
                new Airplane("V3", "Boeing737", 850, 0, 35000),
                new CargoShip("V4", "Freighter", 40, 0, false)
        };

        for (int i = 0; i < vehicles.length; i++) {
            try {
                ((FuelConsumable) vehicles[i]).refuel(100);
            } catch (Exception ignored) {
            }

            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setBackground(Color.decode("#1E1E2E"));
            row.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.decode("#313244")),
                    new EmptyBorder(6, 8, 6, 8)
            ));

            JLabel typeIcon = new JLabel(" " + VEHICLE_ICONS[i] + " ");
            typeIcon.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            typeIcon.setForeground(Color.decode(VEHICLE_COLORS[i]));

            JLabel label = new JLabel(String.format("%s [%s] - Waiting...", vehicles[i].getClass().getSimpleName(), vehicles[i].getId()));
            label.setFont(new Font("Consolas", Font.PLAIN, 13));
            label.setForeground(Color.decode(VEHICLE_COLORS[i]));

            row.add(typeIcon, BorderLayout.WEST);
            row.add(label, BorderLayout.CENTER);

            vehiclePanel.add(row);
            vehicleLabels.add(label);
            vehicleRows.add(row);

            runners.add(new VehicleRunner(vehicles[i], label));
        }
    }

    private void startSimulation() {
        SharedHighway.setSynchronizationEnabled(syncCheckBox.isSelected());
        SharedHighway.reset();

        for (int i = 0; i < runners.size(); i++) {
            runners.set(i, new VehicleRunner(runners.get(i).vehicle, vehicleLabels.get(i)));
        }

        threads.clear();
        for (VehicleRunner runner : runners) {
            Thread t = new Thread(runner);
            threads.add(t);
            t.start();
        }
        startBtn.setEnabled(false);
        statusLabel.setText("Status: Running");
        statusLabel.setForeground(Color.decode("#A6E3A1"));
    }

    private void pauseSimulation() {
        for (VehicleRunner runner : runners) {
            runner.setPaused(true);
        }
        statusLabel.setText("Status: Paused");
        statusLabel.setForeground(Color.decode("#F9E2AF"));
    }

    private void resumeSimulation() {
        for (VehicleRunner runner : runners) {
            runner.setPaused(false);
        }
        statusLabel.setText("Status: Running");
        statusLabel.setForeground(Color.decode("#A6E3A1"));
    }

    private void stopSimulation() {
        for (VehicleRunner runner : runners) {
            runner.stop();
        }
        startBtn.setEnabled(true);
        statusLabel.setText("Status: Stopped");
        statusLabel.setForeground(Color.decode("#F38BA8"));
    }

    private void refuelAll() {
        for (VehicleRunner runner : runners) {
            runner.refuelVehicle();
        }
        statusLabel.setText("Status: Refueled - Running");
        statusLabel.setForeground(Color.decode("#A6E3A1"));
    }

    private void resetSimulation() {
        stopSimulation();
        SharedHighway.reset();
        for (int i = 0; i < runners.size(); i++) {
            runners.get(i).vehicle.setCurrentMilage(0);
            vehicleLabels.get(i).setText(String.format("%s [%s] - Waiting...",
                    runners.get(i).vehicle.getClass().getSimpleName(), runners.get(i).vehicle.getId()));
        }
        statusLabel.setText("Status: Ready");
        statusLabel.setForeground(Color.decode("#89B4FA"));
        startBtn.setEnabled(true);
    }

    private void updateDisplay() {
        double actualSum = 0;
        for (VehicleRunner r : runners) {
            actualSum += r.getCurrentMileage();
        }

        boolean inSync = Math.abs(SharedHighway.totalHighwayDistance - (int) actualSum) < 2;
        totalDistanceLabel.setText(String.format("Shared Counter: %d km | Actual Sum: %d km",
                SharedHighway.totalHighwayDistance, (int) actualSum));

        if (SharedHighway.isSynchronizationEnabled()) {
            totalDistanceLabel.setForeground(Color.decode("#A6E3A1"));
        } else {
            totalDistanceLabel.setForeground(inSync ? Color.decode("#A6E3A1") : Color.decode("#F38BA8"));
        }
    }

    private void updateSyncStatus() {
        if (syncCheckBox.isSelected()) {
            syncCheckBox.setForeground(Color.decode("#A6E3A1"));
            syncCheckBox.setText("Sync Lock (ON)");
        } else {
            syncCheckBox.setForeground(Color.decode("#CDD6F4"));
            syncCheckBox.setText("Sync Lock");
        }
    }
}
