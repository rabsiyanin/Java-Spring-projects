package ru.kozlov.Lab.ui;

import com.vaadin.flow.component.ComponentEvent;
import lombok.Getter;
import ru.kozlov.Lab.model.Passenger;

@Getter
public abstract class PassengerFormEvent extends ComponentEvent<PassengerForm> {
    private Passenger passenger;

    public PassengerFormEvent(PassengerForm source, Passenger passenger) {
        super(source, false);
        this.passenger = passenger;

    }
}

