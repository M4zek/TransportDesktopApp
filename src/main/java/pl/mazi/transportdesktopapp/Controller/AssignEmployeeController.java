package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.Rest.VehicleRest;
import pl.mazi.transportdesktopapp.https.request.EmployeeToDriverRequest;
import pl.mazi.transportdesktopapp.https.response.DriverResponse;
import pl.mazi.transportdesktopapp.model.VehicleTableModel;
import pl.mazi.transportdesktopapp.popup.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class AssignEmployeeController implements Initializable {
    @FXML private VBox vBox;
    @FXML private Label label_vehicle;
    @FXML private ChoiceBox<String> choiceBox_Driver;
    @FXML private JFXButton button_assign;
    @FXML private JFXButton button_cancel;

    private final EmployeeRest employeeRest;
    private final VehicleRest vehicleRest;

    private VehicleTableModel vehicleTableModel = null;
    private volatile String message;

    public AssignEmployeeController(){
        employeeRest = new EmployeeRest();
        vehicleRest = new VehicleRest();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoicBox();
        initButton();
    }

    public void setVehicleLabel(VehicleTableModel vehicle){
        this.vehicleTableModel = vehicle;
        label_vehicle.setText(vehicle.getVehicleType()+ ", " + vehicle.getModel() + " " + vehicle.getMark() );
    }

    private void initButton() {
        button_assign.setOnAction(actionEvent -> {
            String driverString = choiceBox_Driver.getValue();
            if(!driverString.equals("Wybierz")){
                if(driverString.equals("Brak")){
                    try {
                        Long idEmployee = Long.parseLong("-1");
                        Long idVehicle = vehicleTableModel.getIdVehicle();
                        Thread thread = new Thread(()->{
                            message = vehicleRest.setEmployeeToVehicle(new EmployeeToDriverRequest(idVehicle, idEmployee));
                        });
                        thread.start();
                        thread.join();
                        Popup popup = new Popup("Informacja", message);
                        popup.show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        String split[] = driverString.split(",");
                        Long idEmployee = Long.parseLong(split[0]);
                        Long idVehicle = vehicleTableModel.getIdVehicle();
                        Thread thread = new Thread(()->{
                            message = vehicleRest.setEmployeeToVehicle(new EmployeeToDriverRequest(idVehicle, idEmployee));
                        });
                        thread.start();
                        thread.join();
                        Popup popup = new Popup("Informacja", message);
                        popup.show();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        button_cancel.setOnAction(actionEvent -> {
            this.getStage().close();
        });
    }

    private void initChoicBox() {
        choiceBox_Driver.getItems().addAll("Wybierz","Brak"); choiceBox_Driver.setValue("Wybierz");
        for(DriverResponse item: employeeRest.getDrivers()) choiceBox_Driver.getItems().add(item.toString());
    }

    private Stage getStage(){
        return (Stage) this.vBox.getScene().getWindow();
    }
}
