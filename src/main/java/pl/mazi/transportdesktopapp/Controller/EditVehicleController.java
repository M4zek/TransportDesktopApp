package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.Rest.VehicleRest;
import pl.mazi.transportdesktopapp.https.response.VehicleResponse;
import pl.mazi.transportdesktopapp.https.response.VehicleTypeResponse;
import pl.mazi.transportdesktopapp.popup.Popup;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EditVehicleController implements Initializable {
    @FXML private BorderPane borderPane;
    @FXML private ChoiceBox<VehicleTypeResponse> choiceBox_vehicleType;
    @FXML private TextField textField_mark;
    @FXML private TextField textField_model;
    @FXML private TextField textField_mileage;
    @FXML private TextField textField_vinNumber;
    @FXML private TextField textField_weight;
    @FXML private TextField textField_plateNumber;
    @FXML private JFXDatePicker datePicker_dateOfTechnicalInspection;
    @FXML private JFXButton button_update;
    @FXML private JFXButton button_close;

    private VehicleRest vehicleRest;
    private EmployeeRest employeeRest;
    private volatile VehicleResponse vehicle;
    private volatile String message;

    public EditVehicleController(){
        this.vehicleRest = new VehicleRest();
        this.employeeRest = new EmployeeRest();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoiceBox();
        initButtons();
    }

    private void initButtons() {
        button_close.setOnAction(actionEvent -> {
            this.getStage().close();
        });
        button_update.setOnAction(actionEvent -> {
            VehicleResponse vehicleResponse = new VehicleResponse();
            vehicleResponse.setIdVehicle(vehicle.getIdVehicle());
            if(checkChangesInFields(vehicleResponse)){
                try {
                    Thread thread = new Thread(()->{
                        message = vehicleRest.updateVehicle(vehicleResponse);
                    });
                    thread.start();
                    thread.join();
                    Popup popup = new Popup("Informacja", message);
                    popup.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Popup popup = new Popup("Informacja","Nie zmieniłeś żadnych danych!");
                popup.show();
            }
        });
    }

    private boolean checkChangesInFields(VehicleResponse vehicleResponse) {
        Boolean check = false;
        String vehicleType = choiceBox_vehicleType.getValue().getType();
        String mark = textField_mark.getText();
        String model = textField_model.getText();
        String mileage = textField_mileage.getText();
        String vinNumber = textField_vinNumber.getText();
        Double weight = Double.parseDouble(textField_weight.getText());
        String plateNumber = textField_plateNumber.getText();
        LocalDate dateOfInspection = datePicker_dateOfTechnicalInspection.getValue();

        if(!vehicleType.equals(vehicle.getVehicleType()) && !vehicleType.isEmpty()) { check = true; vehicleResponse.setVehicleType(vehicleType); }
        if(!mark.equals(vehicle.getMark()) && !mark.isEmpty()){ check = true; vehicleResponse.setMark(mark); }
        if(!model.equals(vehicle.getModel()) && !model.isEmpty()){ check = true; vehicleResponse.setModel(model); }
        if(!mileage.equals(vehicle.getMileage()) && !mileage.isEmpty()){ check = true; vehicleResponse.setMileage(mileage); }
        if(!vinNumber.equals(vehicle.getVinNumber()) && !vinNumber.isEmpty()) { check = true; vehicleResponse.setVinNumber(vinNumber); }
        if(!plateNumber.equals(vehicle.getPlateNumber()) && !plateNumber.isEmpty()){ check = true; vehicleResponse.setPlateNumber(plateNumber);}
        if(!dateOfInspection.equals(vehicle.getDateOfTechnicalInspection())){ check = true; vehicleResponse.setDateOfTechnicalInspection(dateOfInspection); }
        return check;
    }

    private void initChoiceBox() {
        try {
            Thread thread = new Thread(()->{
                for(VehicleTypeResponse item: vehicleRest.getAllVehicleType()) choiceBox_vehicleType.getItems().add(item);
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setAllLabels(Long idVehicle){
        try {
            Thread thread = new Thread(()->{
                vehicle = vehicleRest.getVehicle(idVehicle);
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        VehicleTypeResponse type = choiceBox_vehicleType.getItems()
                .stream()
                .filter(item-> item.getType().equals(vehicle.getVehicleType()))
                .findAny()
                .orElse(null);

        choiceBox_vehicleType.setValue(type);
        textField_mark.setText(vehicle.getMark());
        textField_model.setText(vehicle.getModel());
        textField_mileage.setText(vehicle.getMileage());
        textField_vinNumber.setText(vehicle.getVinNumber());
        textField_weight.setText(vehicle.getWeight().toString());
        textField_plateNumber.setText(vehicle.getPlateNumber());
        datePicker_dateOfTechnicalInspection.setValue(vehicle.getDateOfTechnicalInspection());
    }

    private Stage getStage(){
            return (Stage) this.borderPane.getScene().getWindow();
    }
}
