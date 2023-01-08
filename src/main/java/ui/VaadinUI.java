package ru.kozlov.Lab.ui;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import ru.kozlov.Lab.Service.PassengerService;
import ru.kozlov.Lab.model.Passenger;

@Route("")
@CssImport("./styles/shared-styles.css")
public class VaadinUI extends VerticalLayout {

    Grid<Passenger> grid = new Grid<>(Passenger.class);

    TextField filterTextAge = new TextField();

    TextField filterTextId = new TextField();

    TextField deleteAgesField = new TextField();

    HorizontalLayout buttonsLayout = new HorizontalLayout();


    private PassengerForm form;

    private PassengerService passengerService;

    public VaadinUI(PassengerService passengerService) {
        this.passengerService = passengerService;
        addClassName("list-view");
        setSizeFull();
        System.out.println(grid.getPropertySet().getProperties().toList());
        configureGrid();

        configureFilter();

        form = new PassengerForm();

        form.addListener(PassengerForm.SaveEvent.class, this::savePassenger);
        form.addListener(PassengerForm.DeleteEvent.class, this:: deletePassenger);
        form.addListener(PassengerForm.UpdateEvent.class, this::updatePassenger);


        Div content = new Div(form, grid);
        content.addClassName("content");
        content.setSizeFull();

        buttonsLayout.setPadding(true);
        buttonsLayout.add(filterTextId);
        buttonsLayout.add(filterTextAge);
        buttonsLayout.add(deleteAgesField);

        add(buttonsLayout, content);
        updateDataGrid();

    }


    public void configureFilter() {
        filterTextId.setPlaceholder("Find by id...");
        filterTextId.setClearButtonVisible(true);
        filterTextId.setValueChangeMode(ValueChangeMode.EAGER);
        filterTextId.addKeyPressListener(Key.ENTER, keyPressEvent -> updateDataGridById());
        filterTextId.addKeyPressListener(Key.CANCEL, keyPressEvent -> updateDataGrid());


        filterTextAge.setPlaceholder("Find by age...");
        filterTextAge.setClearButtonVisible(true);
        filterTextAge.setValueChangeMode(ValueChangeMode.EAGER);
        filterTextAge.addKeyPressListener(Key.ENTER, keyPressEvent -> updateDataGridByAge());
        filterTextAge.addKeyPressListener(Key.CLEAR, keyPressEvent -> updateDataGrid());


        deleteAgesField.setPlaceholder("Delete by age...");
        deleteAgesField.setClearButtonVisible(true);
        deleteAgesField.setValueChangeMode(ValueChangeMode.EAGER);
        deleteAgesField.addKeyPressListener(Key.ENTER, keyPressEvent -> {
            deletePassengersByAge();
            updateDataGrid();
        });
        deleteAgesField.addKeyPressListener(Key.CANCEL, keyPressEvent -> updateDataGrid());

    }

    private void configureGrid() {
        grid.addClassName("Passenger-grid");
        grid.setSizeFull();
        grid.setColumns("passengerId", "name", "survived", "age", "sex", "sibSP", "parch", "ticket", "fare", "cabin", "embarked");


        grid.asSingleSelect().addValueChangeListener(event -> editPassenger(event.getValue()));

    }

    private void editPassenger(Passenger passenger) {
        if (passenger != null) {
            form.setPassenger(passenger);
            form.setVisible(true);
        }

    }

    private void updateDataGrid() {
        grid.setItems(passengerService.findAll());
    }

    private void updateDataGridByAge() {
        grid.setItems(passengerService.findAll(filterTextAge.getValue()));
    }

    private void updateDataGridById() {
        grid.setItems(passengerService.findById(Integer.parseInt(filterTextId.getValue())));
    }


    private void savePassenger(PassengerForm.SaveEvent event) {
        System.out.println("this is save event");
        passengerService.insert(event.getPassenger());
        updateDataGrid();
    }

    private void deletePassenger(PassengerForm.DeleteEvent event){
        System.out.println("this is delete event");
        passengerService.delete(event.getPassenger().getPassengerId());
        updateDataGrid();
    }

    private void updatePassenger(PassengerForm.UpdateEvent event){
        System.out.println("this is update event");
        passengerService.update(event.getPassenger());
        updateDataGrid();
    }

    private void deletePassengersByAge(){
        passengerService.deleteByAge(deleteAgesField.getValue());
    }
}
