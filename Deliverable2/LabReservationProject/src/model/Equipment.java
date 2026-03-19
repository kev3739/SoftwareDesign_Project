package model;

public class Equipment {
    private String equipmentId;
    private String description;
    private String labLocation;
    private String status;
    private EquipmentState state;

    public Equipment(String equipmentId, String description, String labLocation) {
        this.equipmentId = equipmentId;
        this.description = description;
        this.labLocation = labLocation;
        this.status = "AVAILABLE";
        this.state = new AvailableState();
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public String getDescription() {
        return description;
    }

    public String getLabLocation() {
        return labLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EquipmentState getState() {
        return state;
    }

    public void setState(EquipmentState state) {
        this.state = state;
    }

    public void enable() {
        state.enable(this);
    }

    public void disable() {
        state.disable(this);
    }

    public void markUnderMaintenance() {
        state.markUnderMaintenance(this);
    }

    public boolean isAvailable() {
        return state.isAvailable();
    }

    public String getCurrentStateName() {
        return state.getStateName();
    }
}