package model;

public class LabManager extends User {

    private String managerId;

    public LabManager(String name, String email, String password, String managerId) {
        super(name, email, password, managerId);
        this.managerId = managerId;
    }

    public String getManagerId() {
        return managerId;
    }

    @Override
    public String getUserType() {
        return "LabManager";
    }

    @Override
    public double getHourlyRate() {
        return 0.0;
    }

    public Equipment addEquipment(String equipmentId, String description, String labLocation) {
        return new Equipment(equipmentId, description, labLocation);
    }

    public void enableEquipment(Equipment equipment) {
        equipment.enable();
    }

    public void disableEquipment(Equipment equipment) {
        equipment.disable();
    }

    public void markEquipmentUnderMaintenance(Equipment equipment) {
        equipment.markUnderMaintenance();
    }
}