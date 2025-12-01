package Execution;

import Vehicles.Car;
import javax.swing.*;
import java.awt.*;

public class SimulatorGUI extends JFrame {
    private static VehicleRunner[] runners = new VehicleRunner[3];
    private static Thread[] threads = new Thread[3];
    private JPanel vehiclePanel;
    private JLabel totalDistanceLabel;

    public SimulatorGUI() {
        setTitle("Highway Simulation");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Fleet Highway Simulator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        vehiclePanel = new JPanel();
        vehiclePanel.setLayout(new BoxLayout(vehiclePanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(vehiclePanel);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        JPanel statsPanel = new JPanel();

        totalDistanceLabel = new JLabel("Shared Counter: 0 km | Actual Sum: 0 km");
        totalDistanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statsPanel.add(totalDistanceLabel);
        bottomPanel.add(statsPanel);

        JPanel buttonPanel = new JPanel();
        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton resumeBtn = new JButton("Resume");
        JButton stopBtn = new JButton("Stop");
        JButton refuelBtn = new JButton("Refuel All");

        buttonPanel.add(startBtn);
        buttonPanel.add(pauseBtn);
        buttonPanel.add(resumeBtn);
        buttonPanel.add(stopBtn);
        buttonPanel.add(refuelBtn);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        for (int i = 0; i < 3; i++) {
            Car car = new Car("V" + i, "CarModel", 100, 0, 4);
            try {
                car.refuel(50);
            } catch (Exception e) {
            }

            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel("");
            row.add(label);
            vehiclePanel.add(row);

            runners[i] = new VehicleRunner(car, label);
        }

        startBtn.addActionListener(e -> {
            SharedHighway.reset();
            for (int i = 0; i < 3; i++) {
                threads[i] = new Thread(runners[i]);
                threads[i].start();
            }
            startBtn.setEnabled(false);
        });

        pauseBtn.addActionListener(e -> {
            for (int i = 0; i < 3; i++) {
                runners[i].setPaused(true);
            }
        });

        resumeBtn.addActionListener(e -> {
            for (int i = 0; i < 3; i++) {
                runners[i].setPaused(false);
            }
        });

        stopBtn.addActionListener(e -> {
            for (int i = 0; i < 3; i++) {
                runners[i].stop();
            }
            startBtn.setEnabled(true);
        });

        refuelBtn.addActionListener(e -> {
            for (int i = 0; i < 3; i++) {
                runners[i].refuelVehicle();
            }
        });

        Timer timer = new Timer(100, e -> {
            double actualSum = 0;
            for(VehicleRunner r : runners) {
                if(r != null) {
                    actualSum += r.getCurrentMileage();
                }
            }

            totalDistanceLabel.setText("Shared Counter: " + SharedHighway.totalHighwayDistance + " km | Actual Sum: " + (int)actualSum + " km");
        });
        timer.start();
    }

}