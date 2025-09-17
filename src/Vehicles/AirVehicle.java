package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

public abstract class AirVehicle extends Vehicle{
    private double maxAltitude;

    public AirVehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, double vMaxAltitude){
        super(vId,vModel,vMaxSpeed,vCurrentMilage);
        maxAltitude = vMaxAltitude;
    }

    public abstract void move(double distance)throws InvalidOperationException, InsufficientFuelException;

    public abstract double calculateFuelEfficiency();

    @Override
    public double estimateJourneyTime(double distance){
        return 0.95 * distance / getMaxSpeed();
    }

    public double getMaxAltitude(){
        return maxAltitude;
    }
}
