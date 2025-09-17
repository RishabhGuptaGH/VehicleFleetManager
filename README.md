# Vehicle Fleet Management System

## Project Overview
This project is a console-based application built in Java to simulate a vehicle fleet management system. It showcases fundamental Object-Oriented Programming (OOP) concepts such as **inheritance**, **polymorphism**, and **abstraction**. The system allows for managing various types of vehicles, including land, water, and air vehicles, each with unique functionalities.

## Features
* **Add/Remove Vehicles:** Add different types of vehicles (e.g., Cars, Trucks, Airplanes) to the fleet.
* **Refueling & Maintenance:** Manage fuel levels for all vehicles and perform maintenance on them.
* **Loading/Storing Data:** The application can load fleet data from a CSV file and save the current fleet status back to a file. A sample input file, `Example.csv`, is included to demonstrate this functionality.
* **Vehicle-Specific Operations:** Perform operations unique to certain vehicle types (e.g., loading cargo onto a truck, carrying passengers on a bus).
* **Generate Reports:** Create a detailed report of all vehicles in the fleet.

## UML Diagram
The following UML diagram illustrates the class structure and relationships within the project. It highlights the use of abstract classes (`Vehicle`), interfaces (`FuelConsumable`), and a clear inheritance hierarchy.

![UML Diagram](UML%20Diagram.png)

## How to Run
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/RishabhGuptaGH/VehicleFleetManager.git
    ```

2.  ### Using Visual Studio Code (VS Code)
    1.  **Open the project folder:** In VS Code, go to `File` > `Open Folder...` and select your main project directory, `ASSIGNMENT1`.
    2.  **Ensure the Java Extension Pack is installed:** VS Code should prompt you to install it if you don't have it.
    3.  **Run the main class:** Navigate to `src/Execution/Main.java`. You should see a green "Run" button at the top of the file or next to the `main` method. Click it to run your program.

3.  ### Using IntelliJ IDEA
    1.  **Open the project:** In IntelliJ, go to `File` > `Open...` and select the `ASSIGNMENT1` folder. IntelliJ will automatically detect the project structure and configure it for you.
    2.  **Run the main class:** In the Project tool window on the left, navigate to `src/Execution/Main.java`. Right-click the file and select `Run 'Main.main()'` from the context menu. You can also click the green play button icon in the gutter next to the `main` method.



## File Structure
The project is organized into logical packages:
* `src/Exceptions`: Custom exception classes.
* `src/Execution`: Main application logic (`FleetManager`, `Main`).
* `src/Interfaces`: Defines the contracts for different vehicle behaviors.
* `src/Vehicles`: Contains the abstract and concrete vehicle classes.
* `Example.csv`: A sample CSV file used for loading fleet data.
* `UML Diagram.png`: The visual representation of the class structure.