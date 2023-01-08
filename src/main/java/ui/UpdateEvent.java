package ru.kozlov.Lab.ui;

import ru.kozlov.Lab.model.Passenger;

public class UpdateEvent extends PassengerFormEvent {

    UpdateEvent(PassengerForm source, Passenger passenger) {
        super(source, passenger);
    }
}
