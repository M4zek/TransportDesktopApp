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
import pl.mazi.transportdesktopapp.https.request.VehicleRequest;
import pl.mazi.transportdesktopapp.https.response.DriverResponse;
import pl.mazi.transportdesktopapp.https.response.VehicleTypeResponse;
import pl.mazi.transportdesktopapp.popup.Popup;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.Rest.VehicleRest;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AddVehicleController implements Initializable {

    Logger logger = Logger.getLogger(getClass().getName());

    private EmployeeRest employeeRest;
    private VehicleRest vehicleRest;
    private volatile String message;

    @FXML private BorderPane borderPane;
    @FXML private ChoiceBox<String> choiceBox_vehicleType;
    @FXML private TextField textField_mark;
    @FXML private TextField textField_model;
    @FXML private TextField textField_mileage;
    @FXML private TextField textField_vinNumber;
    @FXML private TextField textField_weight;
    @FXML private TextField textField_plateNumber;
    @FXML private JFXDatePicker datePicker_dateOfTechnicalInspection;
    @FXML private ChoiceBox<String> choiceBox_driver;
    @FXML private JFXButton button_add;

    public AddVehicleController(){
        employeeRest = new EmployeeRest();
        vehicleRest = new VehicleRest();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logger.info("Initializable AddVehicleController");
        initButtons();
        initChoiceBox();
    }

    private void initChoiceBox() {
        Thread thread = new Thread(()->{
            choiceBox_driver.getItems().add("Wybierz");
            choiceBox_vehicleType.getItems().add("Wybierz");
            choiceBox_driver.setValue("Wybierz");
            choiceBox_vehicleType.setValue("Wybierz");
            for(VehicleTypeResponse item : vehicleRest.getAllVehicleType()) choiceBox_vehicleType.getItems().add(item.getType());
            for(DriverResponse driver: employeeRest.getDrivers()) choiceBox_driver.getItems().add(
                    driver.getIdEmployee() + "," + driver.getFirstName() + " " + driver.getLastName());
        });
        thread.start();
    }

    private void initButtons() {
        button_add.setOnAction(actionEvent -> {
            VehicleRequest vehicleRequest = new VehicleRequest();
            if(checkAllFields(vehicleRequest)){
                try {
                    Thread thread = new Thread(()->{
                        message = vehicleRest.addVehicle(vehicleRequest);
                    });
                    thread.start();
                    thread.join();
                    Popup popup = new Popup("Informacja", message);
                    System.out.println("ERROR: " + message);
                    popup.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Boolean checkAllFields(VehicleRequest vehicleRequest){
        boolean check = true;

        String red = "-fx-background-color: rgba(255,0,0,0.2)";
        String white = "-fx-background-color: rgba(100,100,100,0.2)";

        String vehicleType = choiceBox_vehicleType.getValue();
        String marka = textField_mark.getText();
        String model = textField_model.getText();
        String mileage = textField_mileage.getText();
        String vinNumber = textField_vinNumber.getText();
        String weight = textField_weight.getText();
        String plateNumber = textField_plateNumber.getText();
        LocalDate dateofInspecion = datePicker_dateOfTechnicalInspection.getValue();
        String driver = choiceBox_driver.getValue();

        if(vehicleType.equals("Wybierz")){
            choiceBox_vehicleType.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setVehicleType(vehicleType);
            choiceBox_vehicleType.setStyle(white);
        }

        if(marka.trim().length() < 2){
            textField_mark.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setMark(marka);
            textField_mark.setStyle(white);
        }

        if(model.trim().length() < 2){
            textField_model.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setModel(model);
            textField_model.setStyle(white);
        }

        if(mileage.trim().isEmpty()){
            textField_mileage.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setMileage(mileage);
            textField_mileage.setStyle(white);
        }

        if(!(vinNumber.trim().length()==17)){
            textField_vinNumber.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setVinNumber(vinNumber);
            textField_vinNumber.setStyle(white);
        }

        if(weight.trim().length() < 2){
            textField_weight.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setWeight(Double.parseDouble(weight));
            textField_weight.setStyle(white);
        }

        if(plateNumber.trim().length() < 5){
            textField_plateNumber.setStyle(red);
            check = false;
        } else {
            vehicleRequest.setPlateNumber(plateNumber);
            textField_plateNumber.setStyle(white);
        }

        if(dateofInspecion == null){
            datePicker_dateOfTechnicalInspection.setStyle(red);
            check = false;
        } else {
            datePicker_dateOfTechnicalInspection.setStyle(white);
            vehicleRequest.setDateOfTechnicalInspection(dateofInspecion);
        }

        if(!driver.equals("Wybierz")){
            String split[] = driver.split(",");
            vehicleRequest.setIdEmployee(Long.parseLong(split[0]));
        }
        return check;
    }

    private Stage getStage(){
        return (Stage) borderPane.getScene().getWindow();
    }
}