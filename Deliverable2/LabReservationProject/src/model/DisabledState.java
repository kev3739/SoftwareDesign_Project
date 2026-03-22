package model;

public class DisabledState implements EquipmentState {

    @Override
    public void enable(Equipment equipment) {
        equipment.setState(new AvailableState());
        System.out.println("Equipment is now enabled.");
    }

    @Override
    public void disable(Equipment equipment) {
        System.out.println("Equipment is already disabled.");
    }

    @Override
    public void markUnderMaintenance(Equipment equipment) {
        equipment.setState(new MaintenanceState());
        System.out.println("Equipment moved from disabled to maintenance.");
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public String getStateName() {
        return "Disabled";
    }
}