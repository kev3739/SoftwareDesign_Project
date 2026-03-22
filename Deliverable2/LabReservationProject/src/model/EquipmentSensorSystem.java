package model;

import util.Observer;
import util.Subject;

import java.util.ArrayList;
import java.util.List;

public class EquipmentSensorSystem implements Subject {
    private List<Observer> observers = new ArrayList<>();

    public void sendStatusUpdate(Equipment equipment, String status) {
        equipment.setStatus(status);
        notifyObservers(equipment);
    }

    public void sendUsageUpdate(Equipment equipment, UsageRecord record) {
        notifyObservers(record);
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Object eventData) {
        for (Observer observer : observers) {
            observer.update(eventData);
        }
    }
}
