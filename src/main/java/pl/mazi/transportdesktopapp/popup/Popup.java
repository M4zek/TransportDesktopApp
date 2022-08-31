package pl.mazi.transportdesktopapp.popup;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pl.mazi.transportdesktopapp.Controller.InfoController;

import java.io.IOException;

public class Popup extends Stage {
    public Popup(String title, String message) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/info.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.setScene(scene);
            this.initModality(Modality.APPLICATION_MODAL);
            this.initStyle(StageStyle.UNDECORATED);
            InfoController infoController = fxmlLoader.getController();
            infoController.setLabels(title, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
