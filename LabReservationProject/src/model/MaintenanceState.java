package model;

public class MaintenanceState implements EquipmentState {

    @Override
    public void enable(Equipment equipment) {
        equipment.setState(new AvailableState());
        System.out.println("Maintenance finished. Equipment is now available.");
    }

    @Override
    public void disable(Equipment equipment) {
        System.out.println("Equipment is under maintenance and cannot be disabled again.");
    }

    @Override
    public void markUnderMaintenance(Equipment equipment) {
        System.out.println("Equipment is already under maintenance.");
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String getStateName() {
        return "Maintenance";
    }
}