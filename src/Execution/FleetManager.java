package Execution;

import Vehicles.*;
import Exceptions.*;
import Interfaces.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class FleetManager {
    private ArrayList<Vehicle> fleet;

    public FleetManager(){

        fleet = new ArrayList<>();
    }

    public void addVehicle(Vehicle v1)throws InvalidOperationException{
        boolean found = false;
        for(Vehicle tempVehicle: fleet){
            if(tempVehicle.getId().equals(v1.getId())){
                throw new InvalidOperationException("Vehicle with same ID exists.");
            }
        }
        if(!found){
            fleet.add(v1);
        }
    }

    public void removeVehicle(String ID)throws InvalidOperationException{
        boolean removed = false;
        for (Vehicle tempVehicle: fleet){
            if(tempVehicle.getId().equals(ID)){
                fleet.remove(tempVehicle);
                removed = true;
                break;
            }
        }

        if(!removed)throw new InvalidOperationException("Vehicle not Found.");
    }

    public void startAllJourney(double distance)throws InvalidOperationException,InsufficientFuelException{
        System.out.println("\n--- Starting all journeys for a distance of " + distance + " km ---");

        for (Vehicle tempVehicle: fleet) {
            sleep();

            try{
                tempVehicle.move(distance);
                System.out.println(tempVehicle.getId() + ": Moved Successfully!");
            }
            catch (InsufficientFuelException | InvalidOperationException e){
                System.err.println("Could not move vehicle "  + e.getMessage());
            }
        }
    }

    public double getTotalFuelConsumption(double distance){
        System.out.println("\n--- Total Fuel Consumption by Fleet ---");
        double sum = 0;
        for (Vehicle tempVehicle: fleet) {
            sleep();
            try {
                if(distance == 0){
                    sum += 0;
                    System.out.println(tempVehicle.getId() + ": Will consume " + 0 + " Units Successfully!");
                }
                else {
                    double temp = 0;
                    FuelConsumable t1 = (FuelConsumable) tempVehicle;
                    temp = t1.consumeFuel(distance);
                    sum += temp;
                    t1.refuel(temp);
                    System.out.println(tempVehicle.getId() + ": Will consume " + temp + " Units Successfully!");
                }
            }
            catch (InsufficientFuelException e){
                System.err.println("Could not consume fuel by Vehicle "+ tempVehicle.getId() + ": Not enough fuel");
            }
            catch (InvalidOperationException e){
                sum += 0;
                System.out.println(tempVehicle.getId() + ": Will consume " + 0 + " Units Successfully! {It has Sail}");
            }
        }
        return sum;
    }

    public void refuelAll(double amount)throws InvalidOperationException{
        System.out.println("\n--- Starting refueling by " + amount + " unit ---");
        for (Vehicle tempVehicle: fleet) {
            sleep();
            try {
                FuelConsumable t1 = (FuelConsumable)tempVehicle;
                t1.refuel(amount);
                System.out.println(tempVehicle.getId() + ": Refueled Successfully!");
            }
            catch (InvalidOperationException e){
                System.err.println(tempVehicle.getId() +  ": " + e.getMessage());
            }
        }
    }

    public void maintainAll(){
        System.out.println("\n--- Starting maintenance if needed ---");
        for (Vehicle tempVehicle: fleet) {
            Maintainable t1 = (Maintainable) tempVehicle;
            boolean needs = false;

            if(t1.needsMaintenance()){
                t1.scheduleMaintenance();
                needs = true;
            }

            if(needs){
                t1.performMaintenance();
            }
            else{
                System.out.println(tempVehicle.getId() + ": No Maintenance Required");
            }
        }
    }

    public List<Vehicle> searchByType(Class<?> type){
        ArrayList<Vehicle> v1 = new ArrayList<Vehicle>();

        for(Vehicle v: fleet){
            if(type.isInstance(v))v1.add(v);
        }

        return v1;
    }

    public void sortFleetByEfficiency(){
        fleet.sort((v1, v2) -> Double.compare(v2.calculateFuelEfficiency(), v1.calculateFuelEfficiency()));
    }

    public List<Vehicle> getVehicleNeedingMaintenance(){
        ArrayList<Vehicle> v1 = new ArrayList<Vehicle>();

        for(Vehicle v: fleet){
            Maintainable t = (Maintainable)v;
            if(t.needsMaintenance())v1.add(v);
        }

        return v1;
    }

    private static void sleep(){
        long  i = 0;
        while (i < 100000000)i++;
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

        int carCount = 0, busCount = 0, truckCount = 0, airplaneCount = 0, cargoShipCount = 0;
        double totalMileage = 0;
        for (Vehicle vehicle : fleet) {
            totalMileage += vehicle.getCurrentMilage();
            if (vehicle instanceof Car) carCount++;
            else if (vehicle instanceof Bus) busCount++;
            else if (vehicle instanceof Truck) truckCount++;
            else if (vehicle instanceof Airplane) airplaneCount++;
            else if (vehicle instanceof CargoShip) cargoShipCount++;
        }
        report.append("Vehicle Count by Type:\n");
        report.append("  - Cars: ").append(carCount).append("\n");
        report.append("  - Buses: ").append(busCount).append("\n");
        report.append("  - Trucks: ").append(truckCount).append("\n");
        report.append("  - Airplanes: ").append(airplaneCount).append("\n");
        report.append("  - Cargo Ships: ").append(cargoShipCount).append("\n");
        report.append("---------------------------\n");

        double totalEfficiency = 0;
        int fuelVehicleCount = 0;
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof FuelConsumable) {
                totalEfficiency += vehicle.calculateFuelEfficiency();
                fuelVehicleCount++;
            }
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
                report.append("  - ID: ").append(vehicle.getId()).append("\n");
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
}
