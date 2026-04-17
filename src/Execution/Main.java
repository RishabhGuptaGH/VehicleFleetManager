package Execution;

import Vehicles.*;
import Exceptions.*;
import Interfaces.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class Main {

    private static void addVehicleWithChecks(FleetManager f1, Vehicle c1) {
        try {
            f1.addVehicle(c1);
            System.out.println(c1.getId() + ": Added Successfully!");
        } catch (InvalidOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void removeVehicleWithChecks(FleetManager f1, String c1) {
        try {
            f1.removeVehicle(c1);
            System.out.println(c1 + ": Removed Successfully!");
        } catch (InvalidOperationException e) {
            System.err.println(c1 + ": " + e.getMessage());
        }
    }

    private static void moveAllWithChecks(FleetManager f1, double d) {
        try {
            f1.startAllJourney(d);
        } catch (InvalidOperationException | InsufficientFuelException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void refuelWithChecks(FleetManager f1, double amount) {
        try {
            f1.refuelAll(amount);
        } catch (InvalidOperationException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void performMaintenance(FleetManager f1) {
        f1.maintainAll();
    }

    private static List<Vehicle> searchByType(FleetManager f1, Class<?> type) {
        return f1.searchByType(type);
    }

    private static void sortByFuelEfficiency(FleetManager f1) {
        System.out.println("\n---Sorted Vehicles By Efficiency---");
        f1.sortFleetByEfficiency();
    }

    private static List<Vehicle> getVehicleNeedingMaintenance(FleetManager f1) {
        return f1.getVehicleNeedingMaintenance();
    }

    private static double totalFuelConsumption(FleetManager f1, double distance) {
        return f1.getTotalFuelConsumption(distance);
    }

    private static void displayMenu() {
        System.out.println("""
                
                Enter your choice:
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
                11. Sort Fleet by Parameters
                12. Exit""");
    }

    private static double validDoubleInput(boolean allowNegative) {
        Scanner s1 = new Scanner(System.in);
        while (true) {
            try {
                double value = s1.nextDouble();
                s1.nextLine();
                if (allowNegative || value > 0) {
                    return value;
                }
                System.err.println("Invalid input. Please enter a positive value");
            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a valid number");
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
            }
        }
    }

    public static int getValidPositiveIntInput(int upperLimit) {
        Scanner s1 = new Scanner(System.in);

        while (true) {
            try {
                int value = s1.nextInt();
                s1.nextLine();

                if (value > 0 && value <= upperLimit) {
                    return value;
                }
                System.err.println("Invalid input. The number must be between 1 and " + upperLimit + ".");

            } catch (InputMismatchException e) {
                System.err.println("Invalid input. Please enter a natural number.");
                s1.nextLine();
            }
        }
    }

    private static String generateReport(FleetManager f1) {
        return f1.generateReport();
    }

    public static String getValidStringInput() {
        Scanner s1 = new Scanner(System.in);

        while (true) {
            String input = s1.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.err.println("Input cannot be empty. Please try again.");
        }
    }

    private static Vehicle createVehicleInstance(Class<?> vehicleClass, String id, String model, double maxSpeed, double currentMileage, boolean print) {
        Scanner s1 = new Scanner(System.in);
        switch (vehicleClass.getSimpleName()) {
            case "Car":
                if (print) System.out.println("Enter Number of Wheels: ");
                int carWheels = getValidPositiveIntInput(1000);
                return new Car(id, model, maxSpeed, currentMileage, carWheels);

            case "Bus":
                if (print) System.out.println("Enter Number of Wheels: ");
                int busWheels = getValidPositiveIntInput(1000);
                return new Bus(id, model, maxSpeed, currentMileage, busWheels);

            case "Truck":
                if (print) System.out.println("Enter Number of Wheels: ");
                int truckWheels = getValidPositiveIntInput(1000);
                return new Truck(id, model, maxSpeed, currentMileage, truckWheels);

            case "Airplane":
                if (print) System.out.println("Enter Max Altitude: ");
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

    private static void saveToFile(FleetManager f1) {
        while (true) {
            String filename = getValidStringInput();
            try {
                f1.saveToFile(filename);
                break;
            } catch (IOException e) {
                System.err.println("Error saving file: " + e.getMessage());
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

        int lineNum = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    lineNum++;
                    continue;
                }
                List<String> values = Arrays.asList(line.split(","));
                try {
                    addFromList(f1, values);
                } catch (Exception e) {
                    System.err.println("Error in Line: " + lineNum + " | Entry not Added (" + e.getMessage() + ")");
                } finally {
                    lineNum++;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
    }

    private static void addFromList(FleetManager f1, List<String> values) {
        String type = values.get(0);
        if (type.equalsIgnoreCase("airplane")) type = "Airplane";

        switch (type) {
            case "Car":
                Car v1 = new Car(values.get(1), values.get(2), Double.parseDouble(values.get(3)), Double.parseDouble(values.get(4)), Integer.parseInt(values.get(5)));
                if (values.size() > 6) {
                    v1.setFuelLevel(Double.parseDouble(values.get(6)));
                    v1.setPassengerCapacity(Integer.parseInt(values.get(7)));
                    v1.setCurrentPassengers(Integer.parseInt(values.get(8)));
                }
                addVehicleWithChecks(f1, v1);
                return;

            case "Truck":
                Truck v2 = new Truck(values.get(1), values.get(2), Double.parseDouble(values.get(3)), Double.parseDouble(values.get(4)), Integer.parseInt(values.get(5)));
                if (values.size() > 6) {
                    v2.setFuelLevel(Double.parseDouble(values.get(6)));
                    v2.setCargoCapacity(Double.parseDouble(values.get(7)));
                    v2.setCurrentCargo(Double.parseDouble(values.get(8)));
                }
                addVehicleWithChecks(f1, v2);
                return;

            case "Airplane":
                Airplane v3 = new Airplane(values.get(1), values.get(2), Double.parseDouble(values.get(3)), Double.parseDouble(values.get(4)), Double.parseDouble(values.get(5)));
                if (values.size() > 6) {
                    v3.setFuelLevel(Double.parseDouble(values.get(6)));
                    v3.setPassengerCapacity(Integer.parseInt(values.get(7)));
                    v3.setCurrentPassengers(Integer.parseInt(values.get(8)));
                    v3.setCargoCapacity(Double.parseDouble(values.get(9)));
                    v3.setCurrentCargo(Double.parseDouble(values.get(10)));
                }
                addVehicleWithChecks(f1, v3);
                return;

            case "Bus":
                Bus v4 = new Bus(values.get(1), values.get(2), Double.parseDouble(values.get(3)), Double.parseDouble(values.get(4)), Integer.parseInt(values.get(5)));
                if (values.size() > 6) {
                    v4.setFuelLevel(Double.parseDouble(values.get(6)));
                    v4.setPassengerCapacity(Integer.parseInt(values.get(7)));
                    v4.setCurrentPassengers(Integer.parseInt(values.get(8)));
                    v4.setCargoCapacity(Double.parseDouble(values.get(9)));
                    v4.setCurrentCargo(Double.parseDouble(values.get(10)));
                }
                addVehicleWithChecks(f1, v4);
                return;

            case "CargoShip":
                CargoShip v5 = new CargoShip(values.get(1), values.get(2), Double.parseDouble(values.get(3)), Double.parseDouble(values.get(4)), Boolean.parseBoolean(values.get(5)));
                if (values.size() > 6) {
                    v5.setFuelLevel(Double.parseDouble(values.get(6)));
                    v5.setCargoCapacity(Double.parseDouble(values.get(7)));
                    v5.setCurrentCargo(Double.parseDouble(values.get(8)));
                }
                addVehicleWithChecks(f1, v5);
                return;

            default:
                System.err.println("Unknown vehicle type: " + type);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimulatorGUI gui = new SimulatorGUI();
            gui.setVisible(true);
        });
    }
}
