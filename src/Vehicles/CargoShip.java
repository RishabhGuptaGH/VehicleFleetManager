package Vehicles;

import Exceptions.InsufficientFuelException;
import Exceptions.InvalidOperationException;
import Exceptions.OverloadException;
import Interfaces.CargoCarrier;
import Interfaces.FuelConsumable;
import Interfaces.Maintainable;

public class CargoShip extends WaterVehicle implements CargoCarrier, Maintainable, FuelConsumable {

    private double fuelLevel;
    private double cargoCapacity;
    private double currentCargo;
    private boolean maintenanceNeeded;

    public CargoShip(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, boolean vHasSail) {
        super(vId, vModel, vMaxSpeed, vCurrentMilage, vHasSail);
        fuelLevel = 0;
        cargoCapacity = 50000;
        currentCargo = 0;
        maintenanceNeeded = false;
    }

    @Override
    public double calculateFuelEfficiency() {
        if (!shipHasSail()) return 4.0;
        return 0;
    }

    @Override
    public void move(double distance) throws InvalidOperationException, InsufficientFuelException {
        if (distance <= 0) throw new InvalidOperationException(getId() + ": Distance must be positive!");

        if (!shipHasSail()) {
            consumeFuel(distance);
        }

        setCurrentMilage(getCurrentMilage() + distance);
        System.out.println(getId() + ": Sailing with cargo...");
    }

    @Override
    public void refuel(double amount) throws InvalidOperationException {
        if (shipHasSail()) throw new InvalidOperationException(getId() + ": Ships with sail can't be refueled");
        if (amount <= 0) throw new InvalidOperationException(getId() + ": Only positive fuel can be added");
        fuelLevel += amount;
    }

    @Override
    public double getFuelLevel() {
        return fuelLevel;
    }

    @Override
    public double consumeFuel(double distance) throws InsufficientFuelException {
        if (shipHasSail()) return 0;
        double consumed = distance / calculateFuelEfficiency();
        if (consumed > getFuelLevel()) throw new InsufficientFuelException(getId() + ": Not enough fuel");
        fuelLevel -= consumed;
        return consumed;
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
        return super.toCsvString() + "," + fuelLevel + "," + cargoCapacity + "," + currentCargo;
    }
}
