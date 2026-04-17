package Vehicles;

public abstract class AirVehicle extends Vehicle {
    private double maxAltitude;

    public AirVehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, double vMaxAltitude) {
        super(vId, vModel, vMaxSpeed, vCurrentMilage);
        maxAltitude = vMaxAltitude;
    }

    @Override
    public double estimateJourneyTime(double distance) {
        return 0.95 * distance / getMaxSpeed();
    }

    public double getMaxAltitude() {
        return maxAltitude;
    }

    @Override
    public String toCsvString() {
        return super.toCsvString() + "," + maxAltitude;
    }
}
