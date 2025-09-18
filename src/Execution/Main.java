package Execution;

import Vehicles.*;
import Exceptions.*;
import Interfaces.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {

    private static void addVehicleWithChecks(FleetManager f1, Vehicle c1){
        try {
            f1.addVehicle(c1);
            System.out.println(c1.getId() + ": Added Successfully!");
        }
        catch (InvalidOperationException e){
            System.err.println(e.getMessage());
        }
    }

    private static void removeVehicleWithChecks(FleetManager f1, String c1){
        try{
            f1.removeVehicle(c1);
            System.out.println(c1 + ": Removed Successfully!");
        }
        catch (InvalidOperationException e){
            System.err.println(c1 + ": " + e.getMessage());
        }
    }

    private static void moveAllWithChecks(FleetManager f1, double d){
        try {
            f1.startAllJourney(d);
        }
        catch (InvalidOperationException | InsufficientFuelException e){
            System.err.println(e.getMessage());
        }
    }

    private static void refuelWithChecks(FleetManager f1, double amount){
        try {
            f1.refuelAll(amount);
        }
        catch (InvalidOperationException e){
            System.err.println(e.getMessage());
        }
    }

    private static void performMaintenance(FleetManager f1){
        f1.maintainAll();
    }

    private static List<Vehicle>searchByType(FleetManager f1, Class<?> type){
        return f1.searchByType(type);
    }

    private static void sortByFuelEfficiency(FleetManager f1){
        System.out.println("\n---Sorted Vehicles By Efficiency---");
        f1.sortFleetByEfficiency();
    }

    private static List<Vehicle>getVehicleNeedingMaintenance(FleetManager f1){
        return f1.getVehicleNeedingMaintenance();
    }

    private static void sleep(){
        long  i = 0;
        while (i < 100000000)i++;
    }

    private static double totalFuelConsumption(FleetManager f1, double distance){
        return f1.getTotalFuelConsumption(distance);
    }

    private static void displayMenu(){
        System.out.println("""
                \nEnter your choice:
                1. Add Vehicle
                2. Remove Vehicle
                3. Start Journey
                4. Refuel All
                5. Perform Maintenance
                6. Generate Report
                7. Save Fleet
                8. Load Fleet
                9. Search by Type
                10. List Vehicles Needing Maintenance
                11. Exit""");
    }

    private static double validDoubleInput(boolean zero){
        Scanner s1 = new Scanner(System.in);
        double value = 0;
        double threshold = 0;
        if(zero)threshold = -0.00001;
        while (true) {
            sleep();
            try {
                value = s1.nextDouble();
                s1.nextLine();
                if(value > threshold){
                    return value;
                }
                else{
                    System.err.println("Invalid input. Please enter a valid input");
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid input\n");
                s1.nextLine();

            }
        }
    }

    private static Class<?> getClassFromUserInput() {
        Scanner s1 = new Scanner(System.in);

        while (true) {

            System.out.print("Enter vehicle type to search for (Car, Bus, Truck, Airplane, CargoShip): ");
            String inputType = s1.nextLine();
            switch (inputType.toLowerCase()) {
                case "car":
                    return Car.class;
                case "bus":
                    return Bus.class;
                case "truck":
                    return Truck.class;
                case "airplane":
                    return Airplane.class;
                case "cargoship":
                    return CargoShip.class;
                default:
                    System.err.println("Invalid type entered. Type does not exist.");
                    sleep();
                    sleep();
            }
        }
    }

    public static int getValidPositiveIntInput() {
        int value;
        Scanner s1 = new Scanner(System.in);

        while (true) {
            try {
                value = s1.nextInt();
                s1.nextLine();

                if (value > 0) {
                    return value;
                } else {
                    System.err.println("Invalid input. The number must be positive and greater than zero.");
                }

            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a natural number.");
                s1.nextLine();
            }
        }
    }

    private static String generateReport(FleetManager f1){
        return f1.generateReport();
    }

    public static String getValidStringInput() {
        String input;
        Scanner s1 = new Scanner(System.in);

        while (true) {
            input = s1.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            } else {
                System.err.println("Input cannot be empty. Please try again.");
            }
        }
    }

    private static Vehicle createVehicleInstance(Class<?> vehicleClass, String id, String model, double maxSpeed, double currentMileage, boolean print) {
        Scanner s1 = new Scanner(System.in);
        switch (vehicleClass.getSimpleName()) {
            case "Car":
                if (print) System.out.println("Enter Number of Wheels: ");
                int carWheels = getValidPositiveIntInput();
                return new Car(id, model, maxSpeed, currentMileage, carWheels);

            case "Bus":
                if (print) System.out.println("Enter Number of Wheels: ");
                int busWheels = getValidPositiveIntInput();
                return new Bus(id, model, maxSpeed, currentMileage, busWheels);

            case "Truck":
                if (print) System.out.println("Enter Number of Wheels: ");
                int truckWheels = getValidPositiveIntInput();
                return new Truck(id, model, maxSpeed, currentMileage, truckWheels);

            case "Airplane":
                if (print) System.out.println("Enter Number of Wheels: ");
                double maxAltitude = validDoubleInput(false);
                return new Airplane(id, model, maxSpeed, currentMileage, maxAltitude);

            case "CargoShip":
                if (print) System.out.print("Does the ship have a sail? (true/false): ");
                boolean hasSail = s1.nextBoolean();
                s1.nextLine();
                return new CargoShip(id, model, maxSpeed, currentMileage, hasSail);

            default:
                System.err.println("Internal error: Unknown vehicle type.");
                return null;
        }
    }

    private static void saveToFile(FleetManager f1){
        while (true) {
            String s1 = getValidStringInput();
            try{
                f1.saveToFile(s1);
                break;
            } catch (IOException e) {
                System.err.println("File with same name already exists");
            }
        }
    }

    private static void readCsvFile(FleetManager f1) {
        Scanner scanner = new Scanner(System.in);
        String filename = null;
        boolean fileFound = false;

        while (!fileFound) {
            System.out.print("Enter the filename: ");
            filename = scanner.nextLine();
            File file = new File(filename);

            if (file.exists() && !file.isDirectory()) {
                fileFound = true;
            } else {
                System.err.println("Error: The file '" + filename + "' does not exist or is a directory. Please try again.");
            }
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> values = Arrays.asList(line.split(","));
                addFromList(f1, values);
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void addFromList(FleetManager f1, List<String> values) {
        switch (values.get(0)) {
            case "Car":
                Car v1 = new Car(values.get(1), values.get(2), Double.parseDouble(values.get(3)), 0, Integer.parseInt(values.get(4)));
                v1.setFuelLevel(Double.parseDouble(values.get(5)));
                v1.setPassengerCapacity(Integer.parseInt(values.get(6)));
                v1.setCurrentPassengers(Integer.parseInt(values.get(7)));

                addVehicleWithChecks(f1, v1);
                return;

            case "Truck":
                Truck v2 = new Truck(values.get(1), values.get(2), Double.parseDouble(values.get(3)), 0, Integer.parseInt(values.get(4)));
                v2.setFuelLevel(Double.parseDouble(values.get(5)));
                v2.setCargoCapacity(Double.parseDouble(values.get(6)));
                v2.setCurrentCargo(Double.parseDouble(values.get(7)));

                addVehicleWithChecks(f1, v2);
                return;

            case "AirPlane":
                Airplane v3 = new Airplane(values.get(1), values.get(2), Double.parseDouble(values.get(3)), 0, Double.parseDouble(values.get(4)));
                v3.setFuelLevel(Double.parseDouble(values.get(5)));
                v3.setPassengerCapacity(Integer.parseInt(values.get(6)));
                v3.setCurrentPassengers(Integer.parseInt(values.get(7)));
                v3.setCargoCapacity(Double.parseDouble(values.get(8)));
                v3.setCurrentCargo(Double.parseDouble(values.get(9)));

                addVehicleWithChecks(f1, v3);
                return;

            case "Bus":
                Bus v4 = new Bus(values.get(1), values.get(2), Double.parseDouble(values.get(3)), 0, Integer.parseInt(values.get(4)));
                v4.setFuelLevel(Double.parseDouble(values.get(5)));
                v4.setPassengerCapacity(Integer.parseInt(values.get(6)));
                v4.setCurrentPassengers(Integer.parseInt(values.get(7)));
                v4.setCargoCapacity(Double.parseDouble(values.get(8)));
                v4.setCurrentCargo(Double.parseDouble(values.get(9)));

                addVehicleWithChecks(f1, v4);
                return;

            case "CargoShip":
                CargoShip v5 = new CargoShip(values.get(1), values.get(2), Double.parseDouble(values.get(3)), 0, Boolean.parseBoolean(values.get(4)));
                v5.setFuelLevel(Double.parseDouble(values.get(5)));
                v5.setCargoCapacity(Double.parseDouble(values.get(6)));
                v5.setCurrentCargo(Double.parseDouble(values.get(7)));

                addVehicleWithChecks(f1, v5);
                return;
        }
    }

    public static void main(String[] args) {

        FleetManager f1 = new FleetManager();

        Scanner s1 = new Scanner(System.in);

        boolean done = false;

        while (!done){
            displayMenu();

            int choice = getValidPositiveIntInput();

            switch (choice){
                case 1:
                    Class<?> T = getClassFromUserInput();
                    System.out.println("Enter Vehicle ID: ");
                    String vID = getValidStringInput();
                    System.out.println("Enter Vehicle Model: ");
                    String vModel = getValidStringInput();
                    System.out.println("Enter Vehicle Max Speed: ");
                    double vMaxSpeed = validDoubleInput(false);
                    System.out.println("Enter Vehicle Mileage: ");
                    double vCurrentMilage = validDoubleInput(true);

                    Vehicle newVehicle = createVehicleInstance(T, vID, vModel, vMaxSpeed, vCurrentMilage, true);

                    if (newVehicle != null) {
                        addVehicleWithChecks(f1, newVehicle);
                    }
                    break;

                case 2:
                    System.out.println("\nEnter Vehicle ID to be removed");
                    String vId = getValidStringInput();
                    removeVehicleWithChecks(f1, vId);
                    break;

                case 3:
                    System.out.println("\nEnter distance to travel (+ve only)");

                    double distance  = validDoubleInput(false);
                    moveAllWithChecks(f1, distance);
                    break;

                case 4:
                    System.out.println("\nEnter amount of fuel to refuel (+ve only)");
                    double fuel  = validDoubleInput(false);
                    refuelWithChecks(f1, fuel);
                    break;

                case 5:
                    performMaintenance(f1);
                    break;

                case 6:
                    String report = generateReport(f1);
                    System.out.println(report);
                    break;

                case 7:
                    System.out.println("Enter the name of output file");
                    saveToFile(f1);
                    break;

                case 8:
                    readCsvFile(f1);
                    break;

                case 9:
                    Class<?> c1= getClassFromUserInput();
                    List<Vehicle> l1 = searchByType(f1,c1);

                    System.out.println("\n---List of vehicles belonging to Required Class---\n");

                    for(Vehicle tempVehicle: l1){
                        tempVehicle.displayInfo();
                    }
                    break;

                case 10:
                    List<Vehicle> l2 = getVehicleNeedingMaintenance(f1);

                    System.out.println("\n---List of vehicles requiring maintenance---\n");
                    System.out.println();
                    for(Vehicle tempVehicle: l2){
                        tempVehicle.displayInfo();
                        System.out.println();
                    }

                    break;

                case 11:
                    System.out.println("\n---Exiting Program---");
                    done = true;
                    break;

                default:
                    System.err.println("Invalid choice try again\n");
            }

            if(done)break;
        }
    }
}