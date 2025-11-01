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

    public void addVehicle(Vehicle v1)throws InvalidOperationException{
        for(String tempId: vidSet){
            if(tempId.equals(v1.getId())){
                throw new InvalidOperationException("Vehicle with same ID exists.");
            }
        }
        
        fleet.add(v1);
        vidSet.add(v1.getId());
        modelSet.add(v1.getModel());
    }


    public void removeVehicle(String ID)throws InvalidOperationException{
        boolean removed = false, valid = true;
        String model = "";

        for (Vehicle tempVehicle: fleet){
            if(tempVehicle.getId().equals(ID)){
                fleet.remove(tempVehicle);
                vidSet.remove(tempVehicle.getId());
                model = tempVehicle.getModel();
                removed = true;
                break;
            }            
        }
        
        if(!removed)throw new InvalidOperationException("Vehicle not Found.");

        else{
            for(Vehicle tempVehicle: fleet){
                if(tempVehicle.getModel().equals(model)){
                    valid = false;
                    break;
                }
            }
            if(valid)modelSet.remove(model);
        }
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

        double totalMileage = 0;

        report.append("Distinct Models present:\n");

        int count = 0;
        for(String tempModel: modelSet){
            count++;
            report.append("  - " + tempModel + "\n");
        }
        report.append("Total Unique Models: " + count + "\n");
        report.append("---------------------------\n");

        double totalEfficiency = 0;
        int fuelVehicleCount = 0;
        for (Vehicle vehicle : fleet) {
            if (vehicle instanceof FuelConsumable) {
                totalEfficiency += vehicle.calculateFuelEfficiency();
                fuelVehicleCount++;
            }
        }

        report.append("Vehicle with Max Speed:" + this.getVehicleWithMaxSpeed().getMaxSpeed() + "\n");
        report.append("---------------------------\n");

        report.append("Vehicle with Min Speed:" + this.getVehicleWithMinSpeed().getMaxSpeed() + "\n");
        report.append("---------------------------\n");

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
                String basicString = vehicle.toCsvString();
                String currClass = basicString.split(",")[0];
                        
                switch (currClass) {
                case "Car":
                    Car v1 = (Car)vehicle;
                    basicString += "," + v1.getNumWheels() + "," + v1.getFuelLevel() + "," + v1.getPassengerCapacity() + "," + v1.getCurrentPassengers();
                    break;
                case "Bus":
                    Bus b1 = (Bus)vehicle;
                    basicString += "," + b1.getNumWheels() + "," + b1.getFuelLevel() + "," + b1.getPassengerCapacity() + "," + b1.getCurrentPassengers() + "," + b1.getCargoCapacity() + ',' + b1.getCurrentCargo();
                    break;
                case "Truck":
                    Truck t1 = (Truck)vehicle;
                    basicString += "," + t1.getNumWheels() + "," + t1.getFuelLevel() + "," + t1.getCargoCapacity() + "," + t1.getCurrentCargo();
                    break;
                case "Airplane":
                    Airplane a1 = (Airplane)vehicle;
                    basicString += "," + a1.getMaxAltitude() + "," + a1.getFuelLevel() +  "," + a1.getPassengerCapacity() + "," + a1.getCurrentPassengers() + "," + a1.getCargoCapacity() + "," + a1.getCurrentCargo();
                    String tempArr_Plane[] = basicString.split(",");
                    tempArr_Plane[0] = "AirPlane";

                    basicString = String.join(",", tempArr_Plane);
                    break;
                case "CargoShip":
                    CargoShip c1 = (CargoShip)vehicle;
                    basicString += "," + c1.shipHasSail() + "," + c1.getFuelLevel() + "," + c1.getCargoCapacity() + "," + c1.getCurrentCargo();
                    break;

                }
                writer.println(basicString);
            }
        }
        System.out.println("Fleet successfully saved to " + filename);
    }

    public void sortFleetByEfficiency(){
        Collections.sort(fleet, Comparator.comparingDouble(Vehicle::calculateFuelEfficiency));
    }
    
    public void sortFleetBySpeed(){
        Collections.sort(fleet, Comparator.comparingDouble(Vehicle::getMaxSpeed));
    }

    public void sortFleetByModel(){
        Collections.sort(fleet, Comparator.comparing(Vehicle::getModel));
    }

    public Vehicle getVehicleWithMaxSpeed(){
        return Collections.max(fleet, Comparator.comparingDouble(Vehicle:: getMaxSpeed));
    }

    public Vehicle getVehicleWithMinSpeed(){
        return Collections.min(fleet, Comparator.comparingDouble(Vehicle:: getMaxSpeed));
    }

    public void printFleetByArgumnet(String arg){
        for(Vehicle temp: fleet){
            if(arg == "Speed"){
                System.out.println(temp.getId() + ": " + temp.getMaxSpeed());
            }
            else if(arg == "Model"){
                System.out.println(temp.getId() + ": " + temp.getModel());
            }
            else if(arg == "Efficiency"){
                System.out.println(temp.getId() + ": " + temp.getModel());
            }
        }
    }
}