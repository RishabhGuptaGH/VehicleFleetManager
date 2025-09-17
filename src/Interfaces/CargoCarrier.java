package Interfaces;

import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;

public interface CargoCarrier {

    //Loads if ≤ capacity; throws Exceptions.OverloadException if exceeded.
    void loadCargo(double weight)throws OverloadException;

    //Unloads; throws Exceptions.InvalidOperationException if weight > current cargo.
    void unloadCargo(double weight)throws InvalidOperationException;

    //Returns max capacity.
    double getCargoCapacity();

    //Returns current cargo.
    double getCurrentCargo();
}
