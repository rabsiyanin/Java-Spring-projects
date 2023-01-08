package ru.kozlov.Lab.ui;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import ru.kozlov.Lab.model.Passenger;



public class PassengerForm extends FormLayout {

    TextField passengerId = new TextField("id");
    TextField name = new TextField("name");
    TextField pClass = new TextField("priorityClass");
    TextField survived = new TextField("survived");
    TextField sex = new TextField("sex");
    TextField age = new TextField("age");
    TextField sibSP = new TextField("sibSP");
    TextField parch = new TextField("parch");
    TextField ticket = new TextField("ticket");
    TextField fare = new TextField("fare");
    TextField cabin = new TextField("cabin");
    TextField embarked = new TextField("embarked");
    Button save = new Button("Save");
    Button delete = new Button("Delete");
    Button update = new Button("Update");
    Binder<Passenger> binder = new BeanValidationBinder<>(Passenger.class);
    private Passenger passenger;

    public PassengerForm() {
        addClassName("Passenger-Form");

        binder.bindInstanceFields(this);

        add(
                passengerId,
                survived,
                pClass,
                name,
                sex,
                age,
                sibSP,
                parch,
                ticket,
                fare,
                cabin,
                embarked,
                createButtonsLayout()
        );


    }


    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
        binder.setBean(passenger);
    }


    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        update.addThemeVariants(ButtonVariant.LUMO_SUCCESS);


        //save.addClickShortcut(Key.ENTER);
        //close.addClickShortcut(Key.ESCAPE);


        save.addClickListener(event -> save());
        update.addClickListener(event -> update());
        delete.addClickListener(event -> delete());

        return new HorizontalLayout(save, update, delete);
    }

    private void save() {
        try {
            binder.writeBean(passenger);
            fireEvent(new SaveEvent(this, passenger));

        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void update() {
        try {
            binder.writeBean(passenger);
            fireEvent(new UpdateEvent(this, passenger));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void delete() {
        try {
            binder.writeBean(passenger);
            fireEvent(new DeleteEvent(this, passenger));
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }


    // events

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public static abstract class PassengerFormEvent extends ComponentEvent<PassengerForm> {
        private Passenger Passenger;

        protected PassengerFormEvent(PassengerForm source, Passenger Passenger) {
            super(source, false);
            this.Passenger = Passenger;
        }

        public Passenger getPassenger() {
            return Passenger;
        }
    }

    public static class SaveEvent extends PassengerFormEvent {
        SaveEvent(PassengerForm source, Passenger Passenger) {
            super(source, Passenger);
        }
    }

    public static class DeleteEvent extends PassengerFormEvent {
        DeleteEvent(PassengerForm source, Passenger Passenger) {
            super(source, Passenger);
        }

    }

    public static class UpdateEvent extends PassengerFormEvent {
        UpdateEvent(PassengerForm source, Passenger Passenger) {
            super(source, Passenger);
        }
    }
}



