package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;
import Interfaces.CargoCarrier;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;
import Interfaces.PassengerCarrier;

public class Airplane extends AirVehicle implements FuelConsumable, PassengerCarrier, CargoCarrier, Maintainable {

    private double fuelLevel;
    private int passengerCapacity;
    private int currentPassengers;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    public Airplane(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, double vMaxAltitude) {
        super(vId, vModel, vMaxSpeed, vCurrentMilage, vMaxAltitude);
        fuelLevel = 0;
        passengerCapacity = 200;
        currentPassengers = 0;
        cargoCapacity = 10000;
        currentCargo = 0;
        maintenanceNeeded = false;
    }

    @Override
    public double calculateFuelEfficiency() {
        return 5.0;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance <= 0) throw new InvalidOperationException(getId() + ": Distance must be positive!");
        consumeFuel(distance);
        setCurrentMilage(getCurrentMilage() + distance);
        System.out.println(getId() + ": Flying at " + getMaxAltitude() + " ft");
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (amount <= 0) throw new InvalidOperationException(getId() + ": Only positive fuel can be added");
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        double consumed = distance / calculateFuelEfficiency();
        if (consumed > getFuelLevel()) throw new InsufficientFuelException(getId() + ": Not enough fuel");
        fuelLevel -= consumed;
        return consumed;
    }

    @Override
    public void boardPassengers(int count) throws OverloadException {
        if (passengerCapacity - currentPassengers >= count) currentPassengers += count;
        else throw new OverloadException(getId() + ": Not enough capacity for all passengers");
    }

    @Override
    public void disembarkPassengers(int count) throws InvalidOperationException {
        if (count <= currentPassengers) {
            currentPassengers -= count;
        } else throw new InvalidOperationException(getId() + ": Can't disembark more people than available");
    }

    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public int getCurrentPassengers() {
        return currentPassengers;
    }

    @Override
    public void loadCargo(double weight) throws OverloadException {
        if (weight <= cargoCapacity - currentCargo) {
            currentCargo += weight;
        } else throw new OverloadException(getId() + ": Can't load Cargo, capacity full.");
    }

    @Override
    public void unloadCargo(double weight) throws InvalidOperationException {
        if (weight > currentCargo) throw new InvalidOperationException(getId() + ": Can't unload more than loaded!");
        currentCargo -= weight;
    }

    @Override
    public double getCargoCapacity() {
        return cargoCapacity;
    }

    @Override
    public double getCurrentCargo() {
        return currentCargo;
    }

    @Override
    public void scheduleMaintenance() {
        maintenanceNeeded = true;
    }

    @Override
    public boolean needsMaintenance() {
        return getCurrentMilage() > 10000 || maintenanceNeeded;
    }

    @Override
    public void performMaintenance() {
        System.out.println(getId() + ": Maintenance performed successfully!");
        maintenanceNeeded = false;
        setCurrentMilage(0);
    }

    public boolean getMaintenanceNeeded() {
        return maintenanceNeeded;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    public void setCurrentPassengers(int currentPassengers) {
        this.currentPassengers = currentPassengers;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    public void setCurrentCargo(double currentCargo) {
        this.currentCargo = currentCargo;
    }

    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }

    @Override
    public String toCsvString() {
        return super.toCsvString() + "," + fuelLevel + "," + passengerCapacity + ","
                + currentPassengers + "," + cargoCapacity + "," + currentCargo;
    }
}
