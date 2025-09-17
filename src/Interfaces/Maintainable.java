package Interfaces;

public interface Maintainable {

    //Sets maintenance flag.
    void scheduleMaintenance();

    //True if mileage > 10000 km.
    boolean needsMaintenance();

    //Resets flag, prints message.
    void performMaintenance();
}