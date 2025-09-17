package Interfaces;

import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;

public interface PassengerCarrier {

    //Boards if ≤ capacity; throws Exceptions.OverloadException.
    void boardPassengers(int count)throws OverloadException;

    //Disembarks; throws Exceptions.InvalidOperationException if count > current passengers.
    void disembarkPassengers(int count)throws InvalidOperationException;

    //Returns max capacity.
    int getPassengerCapacity();

    //Returns current passengers.
    int getCurrentPassengers();
}
