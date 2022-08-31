package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pl.mazi.transportdesktopapp.Rest.VehicleRest;
import pl.mazi.transportdesktopapp.https.response.VehicleTypeResponse;
import pl.mazi.transportdesktopapp.popup.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class MenagerVehicleTypeController implements Initializable {
    @FXML private VBox vBox;
    @FXML private TextField textField_newType;
    @FXML private JFXButton button_addNewType;
    @FXML private ChoiceBox<String> choiceBox_VehicleType;
    @FXML private JFXButton button_delete;

    private final VehicleRest vehicleRest;
    private volatile String message;

    public MenagerVehicleTypeController(){
        vehicleRest = new VehicleRest();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCheckBox();
        initButtons();
    }

    private void initButtons() {
        button_delete.setOnAction(actionEvent -> {
            if(!choiceBox_VehicleType.getValue().equals("Wybierz")) {
                try {
                    Thread thread = new Thread(() -> {
                        message = vehicleRest.deleteVehicleType(choiceBox_VehicleType.getValue());
                    });
                    thread.start();
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Popup popup = new Popup("Informacja",message);
                popup.show();
            } else {
                Popup popup = new Popup("Ostrzeżenie","Musisz wybrać typ!");
                popup.show();
            }
        });


        button_addNewType.setOnAction(actionEvent -> {
            if(!textField_newType.getText().isEmpty()){
                textField_newType.setStyle("-fx-background-color: rgba(100,100,100,0.2)");
                try {
                    Thread thread = new Thread(()->{
                        message = vehicleRest.addVehicleType(textField_newType.getText());
                    });
                    thread.start();
                    thread.join();
                    Popup popup = new Popup("Informacja",message);
                    popup.show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                textField_newType.setStyle("-fx-background-color: rgba(255,0,0,0.2)");
            }
        });
    }

    private void initCheckBox() {
        choiceBox_VehicleType.getItems().add("Wybierz");
        choiceBox_VehicleType.setValue("Wybierz");
        try {
            Thread thread = new Thread(()->{
                for(VehicleTypeResponse item : vehicleRest.getAllVehicleType()) choiceBox_VehicleType.getItems().add(item.getType());
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
