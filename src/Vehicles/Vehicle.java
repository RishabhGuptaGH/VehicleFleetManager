package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

public abstract class Vehicle {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMilage;

    public Vehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage) {
        id = vId;
        model = vModel;
        maxSpeed = vMaxSpeed;
        currentMilage = vCurrentMilage;
    }

    public abstract void move(double distance) throws InvalidOperationException, InsufficientFuelException;

    public abstract double calculateFuelEfficiency();

    abstract double estimateJourneyTime(double distance);

    public void displayInfo() {
        System.out.println("Vehicle ID: " + id);
        System.out.println("Vehicle Model: " + model);
        System.out.println("Vehicle Max Speed: " + maxSpeed + " km/h");
        System.out.println("Vehicle Current Mileage: " + currentMilage + " km");
    }

    @Override
    public String toString() {
        return String.format("%s [%s] - %s | Speed: %.1f km/h | Mileage: %.1f km",
                getClass().getSimpleName(), id, model, maxSpeed, currentMilage);
    }

    public String getId() {
        return id;
    }

    public double getCurrentMilage() {
        return currentMilage;
    }

    public void setCurrentMilage(double newMil) {
        currentMilage = newMil;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public String getModel() {
        return model;
    }

    public String toCsvString() {
        return String.join(",",
                this.getClass().getSimpleName(),
                this.getId(),
                this.getModel(),
                String.valueOf(this.getMaxSpeed()),
                String.valueOf(this.getCurrentMilage())
        );
    }
}
