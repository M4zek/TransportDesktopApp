package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.https.response.EmployeeTypeResponse;
import pl.mazi.transportdesktopapp.popup.Popup;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AddRemoveEmployeeType implements Initializable {

    Logger logger = Logger.getLogger(getClass().getName());
    @FXML private VBox vBox;
    @FXML private TextField textField_newEmployeeType;
    @FXML private JFXButton button_add;
    @FXML private ChoiceBox<String> choiceBox_employeeType;
    @FXML private JFXButton button_remove;

    private EmployeeRest employeeRest;
    private volatile String message;

    public AddRemoveEmployeeType() {
        employeeRest = new EmployeeRest();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initChoiceBox();
        initButtons();
    }

    private void initButtons() {
        button_add.setOnAction(actionEvent -> {
            String type = textField_newEmployeeType.getText();
            type = type.substring(0,1).toUpperCase(Locale.ROOT) + type.substring(1);

            if(textField_newEmployeeType.getText().isEmpty() || textField_newEmployeeType.getText().length() < 4){
                Popup popup = new Popup("Informacja", "Wpisz poprawne stanowisko, min 4 znaki.");
                popup.showAndWait();
            } else {
                try {
                    Thread thread = new Thread(()->{
                        message = employeeRest.addEmployeeType(textField_newEmployeeType.getText());
                    });
                    thread.start();
                    thread.join();
                    Popup popup = new Popup("Informacja", message);
                    popup.show();
                } catch (InterruptedException e) {
                    Popup popup = new Popup("EXCEPTION",e.getMessage());
                    popup.showAndWait();
                }
            }
        });

        button_remove.setOnAction(actionEvent -> {
            String type = choiceBox_employeeType.getValue();
            if(!type.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ostrzeżenie");
                alert.setHeaderText("Na pewno chcesz to zrobić?");
                alert.setContentText("Usunięcie stanowiska: " + type +", spowoduje usunięcie wszystkich pracowników powiązanych z tym stanowiskiem!");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.OK){
                    try {
                        Thread thread = new Thread(()->{
                            message = employeeRest.deleteEmployeeType(type);
                        });
                        thread.start();
                        thread.join();
                        Popup popup = new Popup("Informacja",message);
                        popup.showAndWait();
                    } catch (InterruptedException e) {
                        Popup popup = new Popup("EXCEPTION",e.getMessage());
                        popup.showAndWait();
                    }
                } else {
                    alert.close();
                }
            }
        });
    }

    private void initChoiceBox() {
        List<EmployeeTypeResponse> lista = employeeRest.getAllEmployeType();
        for(EmployeeTypeResponse item: lista){
            choiceBox_employeeType.getItems().add(item.getType());
        }
    }
}
