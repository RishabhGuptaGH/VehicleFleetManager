package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

public abstract class LandVehicle extends Vehicle{
    private int numWheels;

    public LandVehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, int vNumWheels){
        super(vId,vModel,vMaxSpeed,vCurrentMilage);
        numWheels = vNumWheels;
    }

    public abstract void move(double distance)throws InvalidOperationException, InsufficientFuelException;

    public abstract double calculateFuelEfficiency();

    @Override
    public double estimateJourneyTime(double distance){
        return 1.1 * distance / getMaxSpeed();
    }

    public int getNumWheels() {
        return numWheels;
    }


}
