# Vehicle Fleet Management System

## Project Overview
A Java-based vehicle fleet management system with a modern Swing GUI dashboard that demonstrates core Object-Oriented Programming (OOP) principles and multithreading concepts. The system manages a diverse fleet of land, water, and air vehicles with real-time simulation capabilities.

## Core OOP Principles

- **Inheritance:** A multi-level class hierarchy starting from the abstract `Vehicle` class. `LandVehicle`, `AirVehicle`, and `WaterVehicle` extend `Vehicle`, while concrete classes (`Car`, `Truck`, `Bus`, `Airplane`, `CargoShip`) extend the appropriate mid-level class.

- **Polymorphism:** The `FleetManager` calls `move()` on any `Vehicle` subtype, and Java's dynamic dispatch ensures the correct type-specific implementation runs (e.g., "Driving on road..." for a Car, "Flying at..." for an Airplane).

- **Abstract Classes:** `Vehicle`, `LandVehicle`, `AirVehicle`, and `WaterVehicle` are abstract, defining common structure while leaving specific implementations (`move()`, `calculateFuelEfficihttps://github.com/RishabhGuptaGH/VehicleFleetManagerency()`) to concrete subclasses.

- **Interfaces:** `FuelConsumable`, `CargoCarrier`, `PassengerCarrier`, and `Maintainable` define modular behaviors. A `Bus`, for example, implements both `PassengerCarrier` and `CargoCarrier`.

## Multithreading & Race Condition Demo

The simulator demonstrates race conditions in a shared counter:

- **Without Synchronization (Sync Lock OFF):** Multiple threads update `SharedHighway.totalHighwayDistance` concurrently, causing lost updates and indeterminate output. The shared counter will diverge from the actual sum of mileages.

- **With Synchronization (Sync Lock ON):** A `ReentrantLock` ensures only one thread accesses the shared variable at a time, producing correct, deterministic output.

Toggle the **Sync Lock** checkbox in the GUI to switch between modes in real time.

## GUI Dashboard

The graphical interface is built with Java Swing featuring a dark theme with color-coded vehicle status:

- **Real-time vehicle tracking** with individual status labels showing mileage and fuel levels
- **5 vehicle types** simulated concurrently (Car, Bus, Truck, Airplane, CargoShip)
- **Controls:** Start, Pause, Resume, Stop, and Refuel All
- **Sync Lock toggle** to enable/disable thread synchronization
- **Shared Counter vs Actual Sum** display to visually verify race conditions (red text indicates divergence)

## Collections and Generics

The fleet uses a generic `ArrayList<Vehicle>` with `TreeSet<String>` for O(log n) duplicate ID detection, providing dynamic resizing and compile-time type safety while allowing polymorphic storage of all `Vehicle` subtypes.

## Features

- **Add/Remove Vehicles:** Add different vehicle types (Cars, Trucks, Buses, Airplanes, CargoShips) to the fleet
- **Refueling & Maintenance:** Manage fuel levels and perform maintenance with mileage-based triggers
- **CSV Import/Export:** Load fleet data from CSV and save current fleet status back to a file. See `Example.csv` for the expected format
- **Vehicle-Specific Operations:** Type-specific operations (loading cargo onto trucks, boarding passengers on buses, sailing with wind-powered ships)
- **Fleet Reports:** Generate detailed reports including total mileage, average fuel efficiency, min/max speed vehicles, and maintenance status
- **Search & Sort:** Search by vehicle type, sort by fuel efficiency/speed/model
- **Multithreaded Simulation:** Real-time highway simulation with race condition demonstration

## UML Diagram

![UML Diagram](UML%20Diagram.png)

## How to Run

### Prerequisites
- Java 17+ (uses pattern matching for `instanceof`)

### Command Line
1. **Clone the repository:**
   ```bash
   git clone https://github.com/RishabhGuptaGH/VehicleFleetManager.git
   cd VehicleFleetManager
   ```

2. **Compile:**
   ```bash
   javac -d bin -sourcepath src src\Execution\Main.java
   ```

3. **Run:**
   ```bash
   java -cp bin Execution.Main
   ```

### IntelliJ IDEA
1. Open the project: `File` > `Open...` and select the `VehicleFleetManager` folder
2. Run `src/Execution/Main.java` by right-clicking and selecting `Run 'Main.main()'`

## File Structure

```
VehicleFleetManager/
├── src/
│   ├── Exceptions/
│   │   ├── InsufficientFuelException.java
│   │   ├── InvalidOperationException.java
│   │   └── OverloadException.java
│   ├── Execution/
│   │   ├── FleetManager.java          # Fleet management logic
│   │   ├── Main.java                  # Entry point & console menu
│   │   ├── SharedHighway.java         # Shared counter with toggleable lock
│   │   ├── SimulatorGUI.java          # Modern dark-themed GUI dashboard
│   │   └── VehicleRunner.java         # Thread wrapper for vehicle simulation
│   ├── Interfaces/
│   │   ├── CargoCarrier.java
│   │   ├── FuelConsumable.java
│   │   ├── Maintainable.java
│   │   └── PassengerCarrier.java
│   └── Vehicles/
│       ├── AirVehicle.java            # Abstract - maxAltitude, journey time
│       ├── Airplane.java              # Concrete - passengers + cargo
│       ├── Bus.java                   # Concrete - passengers + cargo
│       ├── CargoShip.java             # Concrete - cargo, optional sail
│       ├── Car.java                   # Concrete - passengers only
│       ├── LandVehicle.java           # Abstract - numWheels, journey time
│       ├── Truck.java                 # Concrete - cargo only
│       ├── Vehicle.java               # Root abstract class
│       └── WaterVehicle.java          # Abstract - hasSail, journey time
├── Example.csv                         # Sample fleet data for import
├── UML Diagram.png
└── README.md
```

## CSV Format

The CSV format varies by vehicle type. Each line starts with the class name:

| Type        | Fields |
|-------------|--------|
| Car         | `Car,id,model,maxSpeed,mileage,numWheels,fuelLevel,passengerCapacity,currentPassengers` |
| Truck       | `Truck,id,model,maxSpeed,mileage,numWheels,fuelLevel,cargoCapacity,currentCargo` |
| Bus         | `Bus,id,model,maxSpeed,mileage,numWheels,fuelLevel,passengerCapacity,currentPassengers,cargoCapacity,currentCargo` |
| Airplane    | `Airplane,id,model,maxSpeed,mileage,maxAltitude,fuelLevel,passengerCapacity,currentPassengers,cargoCapacity,currentCargo` |
| CargoShip   | `CargoShip,id,model,maxSpeed,mileage,hasSail,fuelLevel,cargoCapacity,currentCargo` |

## License

This project is licensed under the terms found in the [LICENSE](LICENSE) file.
