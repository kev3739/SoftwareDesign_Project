package model;

public interface EquipmentState {
    void enable(Equipment equipment);
    void disable(Equipment equipment);
    void markUnderMaintenance(Equipment equipment);
    boolean isAvailable();
    String getStateName();
}
