package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pl.mazi.transportdesktopapp.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    private User user = User.getInstance();

    @FXML private JFXTabPane mainTabPane;
    @FXML private Tab tabHome;
    @FXML private Tab tabEmployee;
    @FXML private Tab tabVehicles;
    @FXML private Tab tabAccount;

    private static final String ACCOUNT_SETTINGS = "/fxml/accountSettings.fxml";
    private static final String EMPLOYEE = "/fxml/employee.fxml";
    private static final String VEHICLE = "/fxml/vehicle.fxml";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadView();
    }

    private void loadView(){
        try {
            BorderPane employeeTab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(EMPLOYEE)));
            BorderPane accountTab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(ACCOUNT_SETTINGS)));
            BorderPane vehicleTab = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(VEHICLE)));
            tabEmployee.setContent(employeeTab);
            tabAccount.setContent(accountTab);
            tabVehicles.setContent(vehicleTab);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage getStage(){
        return (Stage) mainTabPane.getScene().getWindow();
    }
}
