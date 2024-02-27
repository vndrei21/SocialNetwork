package com.example.lab8.observer;

public interface Observable {
    void addObserver(Observer observer);

    void notifyObservers();
}
