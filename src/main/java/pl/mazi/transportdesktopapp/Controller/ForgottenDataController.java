package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.mazi.transportdesktopapp.Rest.AuthRest;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgottenDataController implements Initializable {

    @FXML private BorderPane borderPane;
    @FXML private TextField textField_email;
    @FXML private Label info_Label;
    @FXML private JFXButton button_cancel;
    @FXML private JFXButton button_send;

    private AuthRest authRest;
    private volatile String message;

    public static final String INFO_MESSAGE =
            "Wprowadż adres email powiązany z twoim kontem aby zresetować swoje dane do logowania (nazwe użytkownika oraz hasło).\n" +
            "Nowe dane do logowania zostaną przesłane na twoją skrzynke pocztową podana w formularzu";


    public ForgottenDataController() {
        this.authRest = new AuthRest();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCancelButton();
        initSendButton();
    }

    private void initSendButton() {
        button_send.setOnAction(actionEvent -> {
            String email = textField_email.getText();
            if(checkEmailValidation(email)){
                message = authRest.resetUserDate(email);
                showMessageInLabel(message);
            } else {
                showMessageInLabel("Podaj poprawny email!");
            }
        });
    }

    private void initCancelButton() {
        button_cancel.setOnAction(actionEvent -> {
            this.getStage().close();
        });
    }

    private Boolean checkEmailValidation(String email){
        if(email.isEmpty()) return false;
        final String stringPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9]";
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void showMessageInLabel(String message){
        info_Label.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event-> info_Label.setText(""));
        pause.play();
    }

    private Stage getStage(){
        return (Stage) this.borderPane.getScene().getWindow();
    }
}
