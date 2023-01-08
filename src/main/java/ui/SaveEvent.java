package ru.kozlov.Lab.ui;

import ru.kozlov.Lab.model.Passenger;

public class SaveEvent extends PassengerFormEvent {
    SaveEvent(PassengerForm source, Passenger passenger) {
        super(source, passenger);
    }

}
