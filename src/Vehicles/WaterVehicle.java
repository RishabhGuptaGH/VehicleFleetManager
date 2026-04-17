package Vehicles;

public abstract class WaterVehicle extends Vehicle {
    private boolean hasSail;

    public WaterVehicle(String vId, String vModel, double vMaxSpeed, double vCurrentMilage, boolean vHasSail) {
        super(vId, vModel, vMaxSpeed, vCurrentMilage);
        hasSail = vHasSail;
    }

    @Override
    public double estimateJourneyTime(double distance) {
        return 1.15 * distance / getMaxSpeed();
    }

    public boolean shipHasSail() {
        return hasSail;
    }

    @Override
    public String toCsvString() {
        return super.toCsvString() + "," + hasSail;
    }
}
