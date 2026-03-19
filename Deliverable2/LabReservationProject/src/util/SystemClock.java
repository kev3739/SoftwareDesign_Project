package util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SystemClock implements Subject {

    private static SystemClock instance;
    private List<Observer> observers;

    private SystemClock() {
        this.observers = new ArrayList<>();
    }

    public static SystemClock getInstance() {
        if (instance == null) {
            instance = new SystemClock();
        }
        return instance;
    }

    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    public void tick() {
        notifyObservers(now());
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
