package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pl.mazi.transportdesktopapp.Rest.UserRest;
import pl.mazi.transportdesktopapp.https.request.UserUpdateRequest;
import pl.mazi.transportdesktopapp.popup.Popup;
import pl.mazi.transportdesktopapp.user.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    private User user = User.getInstance();

    @FXML private BorderPane borderPane;
    @FXML private TextField textField_firstName;
    @FXML private TextField textField_lastName;
    @FXML private TextField textField_placeOfResidence;
    @FXML private TextField textField_phoneNumber;
    @FXML private TextField textField_Username;
    @FXML private PasswordField passwordField_password;
    @FXML private PasswordField passwordField_confirmPassword;
    @FXML private TextField textField_email;
    @FXML private JFXButton button_confirm;
    @FXML private VBox vBox_firstName;
    @FXML private VBox vBox_lastName;
    @FXML private VBox vBox_placeOfResidence;
    @FXML private VBox vBox_phoneNumber;
    @FXML private VBox vBox_username;
    @FXML private VBox vBox_password;
    @FXML private VBox vBox_email;
    @FXML private JFXButton logout_button;

    private static String LOGIN_FXML = "/fxml/login.fxml";
    private volatile String message;
    private String oldFirstName = user.getFirstName();
    private String oldLastName = user.getLastName();
    private String oldUserName = user.getUsername();
    private String oldPhoneNumber = user.getPhoneNumber();
    private String oldPlaceOfResidence = user.getPlaceOfResidence();
    private String oldEmail = user.getEmail();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initUserData();
        initButton();
        initToolltip();
    }

    private void initButton() {
        button_confirm.setOnAction(actionEvent -> {
            UserRest userRest = new UserRest();
            UserUpdateRequest userUpdate = new UserUpdateRequest();

            if(checkFields(userUpdate)) {
                try {
                    Thread thread = new Thread(()->{
                        message = userRest.tryUserUpdate(userUpdate);
                    });
                    thread.start();
                    thread.join();
                    if(message.equals("UPDATE_SUCCES")){
                        Popup popup = new Popup("Zatwierdź","Zmiana danych przebiegła pomyślnie.\nKonieczne jest ponowne zalogowanie.");
                        popup.showAndWait();
                        logout();
                    } else {
                        Popup popup = new Popup("Informacja", message);
                        popup.show();
                    }
                } catch (InterruptedException e) {
                    Popup popup = new Popup("EXCEPTION",e.getMessage());
                    popup.showAndWait();
                }
            } else {
                Popup popup = new Popup("Informacja", "Nie zmieniłeś żadnych danych!");
                popup.showAndWait();
            }
        });

        logout_button.setOnAction(actionEvent -> {
            user.setInstance(null);
            openLoginPageAndCloseThis();
        });
    }

    private boolean checkFields(UserUpdateRequest userUpdate){
        userUpdate.setIdUser(user.getId());
        boolean check = false;
        String newFirstName = textField_firstName.getText();
        String newLastName = textField_lastName.getText();
        String newUserName = textField_Username.getText();
        String newPhoneNumber = textField_phoneNumber.getText();
        String newEmail = textField_email.getText();
        String newPlaceOfResidence = textField_placeOfResidence.getText();
        String password =passwordField_password.getText();
        String confirmPassword = passwordField_confirmPassword.getText();

        if(!newFirstName.equals(user.getFirstName())) {
            check = true;
            userUpdate.setFirstName(newFirstName);
        } else userUpdate.setFirstName("");

        if(!newLastName.equals(user.getLastName())){
            check = true;
            userUpdate.setLastName(newLastName);
        } else userUpdate.setLastName("");

        if(!newUserName.equals(user.getUsername())){
            check = true;
            userUpdate.setUsername(newUserName);
        } else userUpdate.setUsername("");

        if(!newPhoneNumber.equals(user.getPhoneNumber())){
            check = true;
            userUpdate.setPhoneNumber(newPhoneNumber);
        } else userUpdate.setPhoneNumber("");

        if(!newEmail.equals(user.getEmail())){
            check = true;
            userUpdate.setEmail(newEmail);
        } else userUpdate.setEmail("");

        if(!newPlaceOfResidence.equals(user.getPlaceOfResidence())){
            check = true;
            userUpdate.setPlaceOfResidence(newPlaceOfResidence);
        } else userUpdate.setPlaceOfResidence("");

        if(password.equals(confirmPassword) && !password.equals("")){
            check = true;
            userUpdate.setPassword(password);
        } else {
            userUpdate.setPassword("");
        }

        return check;
    }

    private void logout(){
        try {
            this.getStage().close();
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(LOGIN_FXML)));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUserData() {
        System.out.println("\n\nUSER: " + user.toString());
        textField_email.setText(user.getEmail());
        textField_firstName.setText(user.getFirstName());
        textField_lastName.setText(user.getLastName());
        textField_phoneNumber.setText(user.getPhoneNumber());
        textField_placeOfResidence.setText(user.getPlaceOfResidence());
        textField_Username.setText(user.getUsername());
    }

    private void initToolltip() {
        Tooltip.install(vBox_firstName,new Tooltip("Edytuj i kliknij potwierdź aby zmienić Imię."));
        Tooltip.install(vBox_lastName,new Tooltip("Edytuj i kliknij potwierdź aby zmienić Nazwisko."));
        Tooltip.install(vBox_placeOfResidence,new Tooltip("Edytuj i kliknij potwierdź aby zmienić Miejsce Zamieszkania."));
        Tooltip.install(vBox_phoneNumber,new Tooltip("Edytuj i kliknij potwierdź aby zmienić Numer Telefonu."));
        Tooltip.install(vBox_username,new Tooltip("Edytuj i kliknij potwierdź aby zmienić Nazwę Użytkownika."));
        Tooltip.install(vBox_password,new Tooltip("Wpisz hasło oraz potwierdź nowe hasło aby je zmienić dla twojego konta."));
        Tooltip.install(vBox_email,new Tooltip("Edytuj i kliknij potwierdź aby zmienić Email."));
    }

   private void openLoginPageAndCloseThis(){
        try {
            this.getStage().close();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("MM - Transport | Logowanie");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage getStage(){
        return (Stage) borderPane.getScene().getWindow();
    }
}
