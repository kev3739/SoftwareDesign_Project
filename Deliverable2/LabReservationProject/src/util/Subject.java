package util;

public interface Subject {
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(Object eventData);
}
