package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

public abstract class Vehicle {
    private String id;
    private String model;
    private double maxSpeed;
    private double currentMilage;

    public Vehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage){
        id = vId;
        model = vModel;
        maxSpeed = vMaxSpeed;
        currentMilage = vCurrentMilage;

    }

    //Updates mileage, prints type-specific movement; throws Exceptions.InvalidOperationException if distance < 0.
    public abstract void move(double distance)throws InvalidOperationException, InsufficientFuelException;

    //Returns km per liter (or 0 for non-fuel vehicles).
    public abstract double calculateFuelEfficiency();

    //Returns time in hours (distance / maxSpeed, adjusted by type).
    abstract double estimateJourneyTime(double distance);

    public void displayInfo(){
        System.out.println("Vehicles.Vehicle ID: " + id);
        System.out.println("Vehicles.Vehicle Model: " + model);
        System.out.println("Vehicles.Vehicle Max Speed: " + maxSpeed);
        System.out.println("Vehicles.Vehicle Current Milage: " + currentMilage);
    };

    public String getId(){
        return id;
    }

    public double getCurrentMilage(){
        return currentMilage;
    }

    public void setCurrentMilage(double newMil){
        currentMilage = newMil;
    }

    public double getMaxSpeed() { return maxSpeed; }

    public String getModel(){ return model;}

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