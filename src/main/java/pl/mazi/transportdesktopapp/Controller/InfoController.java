package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class InfoController implements Initializable {


    @FXML private Label label_title;
    @FXML private Label label_message;
    @FXML private JFXButton button_ok;
    @FXML private BorderPane borderPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label_message.setWrapText(true);
        initButton();
    }

    private void initButton() {
        button_ok.setOnAction(actionEvent -> {
            this.getStage().close();
        });
    }

    public void setLabels(String title, String message){
        label_title.setText(title);
        label_message.setText(message);
    }

    private Stage getStage(){
        return (Stage) borderPane.getScene().getWindow();
    }
}
