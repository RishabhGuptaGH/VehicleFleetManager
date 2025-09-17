package Interfaces;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;

public interface FuelConsumable {

    //Adds fuel; throws Exceptions.InvalidOperationException if amount ≤ 0.
    void refuel(double amount)throws InvalidOperationException;

    //Returns current fuel level.
    double getFuelLevel();

    //Reduces fuel based on efficiency; returns consumed amount; throws Exceptions.InsufficientFuelException if not enough fuel.
    double consumeFuel(double distance)throws InsufficientFuelException;
}
