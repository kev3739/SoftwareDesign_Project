package service;

import model.Equipment;
import model.UsageRecord;
import util.Observer;

public class SensorUpdateObserver implements Observer {
    @Override
    public void update(Object eventData) {
        if (eventData instanceof Equipment) {
            Equipment equipment = (Equipment) eventData;
            System.out.println("Equipment status updated: "
                    + equipment.getEquipmentId() + " -> " + equipment.getStatus());
        } else if (eventData instanceof UsageRecord) {
            UsageRecord record = (UsageRecord) eventData;
            System.out.println("Usage recorded: "
                    + record.getRecordId() + ", duration = "
                    + record.getDurationMinutes() + " minutes, status = "
                    + record.getOperationStatus());
        }
    }
}
