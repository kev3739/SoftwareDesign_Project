package model;

public class AvailableState implements EquipmentState {

    @Override
    public void enable(Equipment equipment) {
        System.out.println("Equipment is already enabled.");
    }

    @Override
    public void disable(Equipment equipment) {
        equipment.setState(new DisabledState());
        System.out.println("Equipment is now disabled.");
    }

    @Override
    public void markUnderMaintenance(Equipment equipment) {
        equipment.setState(new MaintenanceState());
        System.out.println("Equipment is now under maintenance.");
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getStateName() {
        return "Available";
    }
}
