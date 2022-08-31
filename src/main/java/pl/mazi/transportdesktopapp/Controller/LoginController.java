package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.mazi.transportdesktopapp.Rest.AuthRest;
import pl.mazi.transportdesktopapp.https.request.LoginRequest;
import pl.mazi.transportdesktopapp.popup.Popup;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginController implements Initializable {

    public static final Logger logger = Logger.getLogger(LoginController.class.getName());
    private volatile String isAuth = "false";
    @FXML private BorderPane loginPane;
    @FXML private TextField textFieldUsername;
    @FXML private PasswordField textFieldPassword;
    @FXML private JFXButton buttonContinue;
    @FXML private Label label_forgotDate;


    public LoginController(){}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonContinue.setOnAction(actionEvent -> {
            String username = textFieldUsername.getText();
            String password = textFieldPassword.getText();

            if(username.isEmpty() && password.isEmpty()){
                Popup popup = new Popup("Logowanie!","Proszę uzupełnić wszystkie pola wymagane do zalogowania!");
                popup.show();

            } else {
                try {
                    LoginRequest loginRequest = new LoginRequest(username, password);
                    Thread thread = new Thread(()->{
                        isAuth = AuthRest.trySignin(loginRequest);
                    });
                    thread.start();
                    thread.join();

                    if(Boolean.parseBoolean(isAuth)){
                        this.getStage().close();
                        start();
                    } else {
                        Popup popup = new Popup("Błąd",isAuth);
                        popup.show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        label_forgotDate.setOnMouseClicked(e->{
            showRecoveredDataStage();
        });
    }

    private void showRecoveredDataStage() {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/forgottenData.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("MM - Transport | Odzyskiwane danych");
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void start(){
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainWindow.fxml"));
            Scene scene = new Scene(fxmlLoader.load(),1024,768);
            stage.setTitle("MM - Transport");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage getStage(){
        return (Stage) loginPane.getScene().getWindow();
    }
}