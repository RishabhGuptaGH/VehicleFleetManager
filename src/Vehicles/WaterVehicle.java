package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

public abstract class WaterVehicle extends Vehicle{
    private boolean hasSail;

    public WaterVehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, boolean vHasSail){
        super(vId,vModel,vMaxSpeed,vCurrentMilage);
        hasSail = vHasSail;
    }

    public abstract void move(double distance)throws InvalidOperationException, InsufficientFuelException;

    public abstract double calculateFuelEfficiency();

    @Override
    public double estimateJourneyTime(double distance){
        return 1.15 * distance / getMaxSpeed();
    }

    public boolean shipHasSail(){
        return hasSail;
    }
}