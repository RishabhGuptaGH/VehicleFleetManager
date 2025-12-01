package Execution;

import Vehicles.Vehicle;
import Interfaces.FuelConsumable;
import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

import javax.swing.*;

public class VehicleRunner implements Runnable {
    public final Vehicle vehicle;
    private final JLabel statusLabel;
    private volatile boolean running = true;
    private volatile boolean paused = false;

    public VehicleRunner(Vehicle vehicle, JLabel statusLabel) {
        this.vehicle = vehicle;
        this.statusLabel = statusLabel;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (paused) {
                    Thread.sleep(100);
                    continue;
                }

                vehicle.move(10);

                SharedHighway.updateDistance(10);

                SwingUtilities.invokeLater(() -> {
                    String status = String.format("ID: %s | Model: %s | Mileage: %.1f | Fuel: %.1f",
                            vehicle.getId(), vehicle.getModel(), vehicle.getCurrentMilage(),
                            ((FuelConsumable)vehicle).getFuelLevel());
                    statusLabel.setText(status);
                });

                Thread.sleep(1000);

            } catch (InsufficientFuelException e) {
                SwingUtilities.invokeLater(() -> statusLabel.setText(vehicle.getId() + ": OUT OF FUEL!"));
                paused = true;
            } catch (InterruptedException | InvalidOperationException e) {
                e.printStackTrace();
            }
        }
    }

    public void refuelVehicle() {
        try {
            ((FuelConsumable)vehicle).refuel(100);
            paused = false;
        } catch (InvalidOperationException e) {
            e.printStackTrace();
        }
    }

    public double getCurrentMileage(){return vehicle.getCurrentMilage();}
}