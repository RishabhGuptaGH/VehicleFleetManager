package Execution;

import Vehicles.*;
import Exceptions.*;
import Interfaces.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;


public class FleetManager {
    private ArrayList<Vehicle> fleet;
    private TreeSet<String> vidSet;
    private TreeSet<String> modelSet;

    public FleetManager(){
        fleet = new ArrayList<>();
        vidSet = new TreeSet<>();
        modelSet = new TreeSet<>();
    }

    public void addVehicle(Vehicle v1) throws InvalidOperationException {
        if (vidSet.contains(v1.getId())) {
            throw new InvalidOperationException("Vehicle with same ID exists.");
        }
        fleet.add(v1);
        vidSet.add(v1.getId());
        modelSet.add(v1.getModel());
    }

    public void removeVehicle(String ID) throws InvalidOperationException {
        Vehicle toRemove = null;
        for (Vehicle tempVehicle : fleet) {
            if (tempVehicle.getId().equals(ID)) {
                toRemove = tempVehicle;
                break;
            }
        }

        if (toRemove == null) throw new InvalidOperationException("Vehicle not Found.");

        fleet.remove(toRemove);
        vidSet.remove(toRemove.getId());

        boolean modelStillPresent = false;
        for (Vehicle tempVehicle : fleet) {
            if (tempVehicle.getModel().equals(toRemove.getModel())) {
                modelStillPresent = true;
                break;
            }
        }
        if (!modelStillPresent) modelSet.remove(toRemove.getModel());
    }

    public void startAllJourney(double distance) throws InvalidOperationException, InsufficientFuelException {
        System.out.println("\n--- Starting all journeys for a distance of " + distance + " km ---");

        for (Vehicle tempVehicle : fleet) {
            try {
                tempVehicle.move(distance);
                System.out.println(tempVehicle.getId() + ": Moved Successfully!");
            } catch (InsufficientFuelException | InvalidOperationException e) {
                System.err.println("Could not move vehicle " + e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance) {
        System.out.println("\n--- Total Fuel Consumption by Fleet ---");
        double sum = 0;
        for (Vehicle tempVehicle : fleet) {
            try {
                if (tempVehicle instanceof FuelConsumable fc) {
                    if (distance == 0) {
                        System.out.println(tempVehicle.getId() + ": Will consume 0.0 Units Successfully!");
                    } else {
                        double temp = fc.consumeFuel(distance);
                        sum += temp;
                        fc.refuel(temp);
                        System.out.println(tempVehicle.getId() + ": Will consume " + temp + " Units Successfully!");
                    }
                }
            } catch (InsufficientFuelException e) {
                System.err.println("Could not consume fuel by Vehicle " + tempVehicle.getId() + ": Not enough fuel");
            } catch (InvalidOperationException e) {
                System.out.println(tempVehicle.getId() + ": Will consume 0.0 Units Successfully! {It has Sail}");
            }
        }
        return sum;
    }

    public void refuelAll(double amount) throws InvalidOperationException {
        System.out.println("\n--- Starting refueling by " + amount + " unit ---");
        for (Vehicle tempVehicle : fleet) {
            try {
                if (tempVehicle instanceof FuelConsumable fc) {
                    fc.refuel(amount);
                    System.out.println(tempVehicle.getId() + ": Refueled Successfully!");
                }
            } catch (InvalidOperationException e) {
                System.err.println(tempVehicle.getId() + ": " + e.getMessage());
            }
        }
    }

    public void maintainAll() {
        System.out.println("\n--- Starting maintenance if needed ---");
        for (Vehicle tempVehicle : fleet) {
            if (tempVehicle instanceof Maintainable t1) {
                if (t1.needsMaintenance()) {
                    t1.scheduleMaintenance();
                    t1.performMaintenance();
                } else {
                    System.out.println(tempVehicle.getId() + ": No Maintenance Required");
                }
            }
        }
    }

    public List<Vehicle> searchByType(Class<?> type) {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (type.isInstance(v)) result.add(v);
        }
        return result;
    }

    public List<Vehicle> getVehicleNeedingMaintenance() {
        ArrayList<Vehicle> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (v instanceof Maintainable m && m.needsMaintenance()) result.add(v);
        }
        return result;
    }

    public String generateReport() {
        if (fleet.isEmpty()) {
            return "Fleet Report:\nNo vehicles in the fleet.";
        }

        StringBuilder report = new StringBuilder();
        report.append("\n--- Fleet Status Report ---\n");

        int totalVehicles = fleet.size();
        report.append("Total Vehicles: ").append(totalVehicles).append("\n");
        report.append("---------------------------\n");

        double totalMileage = 0;
        for (Vehicle v : fleet) {
            totalMileage += v.getCurrentMilage();
        }

        report.append("Distinct Models present:\n");
        int count = 0;
        for (String tempModel : modelSet) {
            count++;
            report.append("  - ").append(tempModel).append("\n");
        }
        report.append("Total Unique Models: ").append(count).append("\n");
        report.append("---------------------------\n");

        double totalEfficiency = 0;
        int fuelVehicleCount = 0;
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof FuelConsumable) {
                totalEfficiency += vehicle.calculateFuelEfficiency();
                fuelVehicleCount++;
            }
        }

        if (!fleet.isEmpty()) {
            report.append("Vehicle with Max Speed: ").append(getVehicleWithMaxSpeed().getId())
                  .append(" (").append(getVehicleWithMaxSpeed().getMaxSpeed()).append(" km/h)\n");
            report.append("---------------------------\n");
            report.append("Vehicle with Min Speed: ").append(getVehicleWithMinSpeed().getId())
                  .append(" (").append(getVehicleWithMinSpeed().getMaxSpeed()).append(" km/h)\n");
            report.append("---------------------------\n");
        }

        double averageEfficiency = (fuelVehicleCount > 0) ? (totalEfficiency / fuelVehicleCount) : 0;
        report.append(String.format("Average Fuel Efficiency: %.2f km/unit\n", averageEfficiency));
        report.append("---------------------------\n");

        report.append(String.format("Total Fleet Mileage: %.2f km\n", totalMileage));
        report.append("---------------------------\n");

        report.append("Vehicles Needing Maintenance:\n");
        List<Vehicle> needsMaintenanceList = getVehicleNeedingMaintenance();
        if (needsMaintenanceList.isEmpty()) {
            report.append("  - None\n");
        } else {
            for (Vehicle vehicle : needsMaintenanceList) {
                report.append("  - ID: ").append(vehicle.getId())
                      .append(" (Mileage: ").append(String.format("%.1f", vehicle.getCurrentMilage())).append(" km)\n");
            }
        }
        report.append("--- End of Report ---\n");

        return report.toString();
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Vehicle vehicle : fleet) {
                writer.println(vehicle.toCsvString());
            }
        }
        System.out.println("Fleet successfully saved to " + filename);
    }

    public void sortFleetByEfficiency() {
        Collections.sort(fleet, Comparator.comparingDouble(Vehicle::calculateFuelEfficiency));
    }

    public void sortFleetBySpeed() {
        Collections.sort(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }

    public void sortFleetByModel() {
        Collections.sort(fleet, Comparator.comparing(Vehicle::getModel));
    }

    public Vehicle getVehicleWithMaxSpeed() {
        return Collections.max(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }

    public Vehicle getVehicleWithMinSpeed() {
        return Collections.min(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }

    public void printFleetByArgument(String arg) {
        for (Vehicle temp : fleet) {
            switch (arg) {
                case "Speed":
                    System.out.println(temp.getId() + ": " + temp.getMaxSpeed() + " km/h");
                    break;
                case "Model":
                    System.out.println(temp.getId() + ": " + temp.getModel());
                    break;
                case "Efficiency":
                    System.out.println(temp.getId() + ": " + String.format("%.2f", temp.calculateFuelEfficiency()) + " km/unit");
                    break;
            }
        }
    }
}
