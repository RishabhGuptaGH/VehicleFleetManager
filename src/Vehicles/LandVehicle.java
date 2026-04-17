package Vehicles;

public abstract class LandVehicle extends Vehicle {
    private int numWheels;

    public LandVehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, int vNumWheels) {
        super(vId, vModel, vMaxSpeed, vCurrentMilage);
        numWheels = vNumWheels;
    }

    @Override
    public double estimateJourneyTime(double distance) {
        return 1.1 * distance / getMaxSpeed();
    }

    public int getNumWheels() {
        return numWheels;
    }

    @Override
    public String toCsvString() {
        return super.toCsvString() + "," + numWheels;
    }
}
