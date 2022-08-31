package pl.mazi.transportdesktopapp.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.https.request.SignupRequest;
import pl.mazi.transportdesktopapp.https.response.EmployeeTypeResponse;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddEmployeeController implements Initializable {

    private volatile String message;

    public static final String INFO_PASSWORD = "Możesz utworzyć hasło lub wpisać swoje własne";

    @FXML private BorderPane borderPane;
    @FXML private VBox mainVBox;
    @FXML private ChoiceBox<String> choiceBox_empl;
    @FXML private ChoiceBox<String> choiceBox_rola;
    @FXML private TextField textField_firstName;
    @FXML private TextField textField_lastName;
    @FXML private TextField textField_phoneNumber;
    @FXML private TextField textField_numberPesel;
    @FXML private TextField textField_salary;
    @FXML private TextField textField_placeOfResidence;
    @FXML private JFXDatePicker datePicker_birthDate;
    @FXML private JFXDatePicker datePicker_hireDate;
    @FXML private ChoiceBox<String> choiceBox_employeeType;
    @FXML private VBox vBox_credentialsBox;
    @FXML private TextField textField_username;
    @FXML private TextField textField_email;
    @FXML private TextField textField_password;
    @FXML private JFXButton button_makePassword;
    @FXML private JFXButton button_cancel;
    @FXML private JFXButton button_continue;
    @FXML private Label label_Info;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initTooltip();
        setChoiceBoxs();
        initButtons();
    }

    private void initTooltip() {
        textField_password.setTooltip(new Tooltip(INFO_PASSWORD));
    }


    private void initButtons(){
        button_cancel.setOnAction(e->{ this.getStage().close();});
        button_makePassword.setOnAction(e->{textField_password.setText(makePassword());});
        button_continue.setOnAction(e->{

            if(checkAllInputValidation()){
                String firstName = textField_firstName.getText();
                String lastName = textField_lastName.getText();
                String phoneNumber = textField_phoneNumber.getText();
                String peselNumber = textField_numberPesel.getText();
                Double salary = Double.parseDouble(textField_salary.getText());
                String placeOfResidence = textField_placeOfResidence.getText();
                LocalDate birthDate = datePicker_birthDate.getValue();
                LocalDate hireDate = datePicker_hireDate.getValue();
                String employeeType = choiceBox_employeeType.getValue();

                if(vBox_credentialsBox.isDisable()){
                    SignupRequest signupRequest = new SignupRequest();
                    signupRequest.setFirstName(firstName);
                    signupRequest.setLastName(lastName);
                    signupRequest.setPhoneNumber(phoneNumber);
                    signupRequest.setPeselNumber(peselNumber);
                    signupRequest.setSalary(salary);
                    signupRequest.setPlaceOfResidence(placeOfResidence);
                    signupRequest.setHireDate(hireDate);
                    signupRequest.setBirthDate(birthDate);
                    signupRequest.setEmployeType(employeeType);

                    EmployeeRest employeeRest = new EmployeeRest();
                    message = employeeRest.tryAddNewEmployee(signupRequest);
                } else {
                    String username = textField_username.getText();
                    String email = textField_email.getText();
                    String password = textField_password.getText();
                    Set<String> role = new HashSet<>();
                    role.add(choiceBox_rola.getValue());

                    SignupRequest signupRequest = new SignupRequest();
                    signupRequest.setFirstName(firstName);
                    signupRequest.setLastName(lastName);
                    signupRequest.setPhoneNumber(phoneNumber);
                    signupRequest.setPeselNumber(peselNumber);
                    signupRequest.setSalary(salary);
                    signupRequest.setPlaceOfResidence(placeOfResidence);
                    signupRequest.setHireDate(hireDate);
                    signupRequest.setBirthDate(birthDate);
                    signupRequest.setEmployeType(employeeType);
                    signupRequest.setUsername(username);
                    signupRequest.setEmail(email);
                    signupRequest.setPassword(password);
                    signupRequest.setRole(role);

                    EmployeeRest employeeRest = new EmployeeRest();
                    message = employeeRest.tryAddNewEmployeeSystemUser(signupRequest);
                }
                showMessageInLabel(message);
            }
        });
    }

    private Boolean checkAllInputValidation(){
        if(choiceBox_empl.getValue() != null) {
            choiceBox_empl.setStyle("-fx-border-color:white;");
        } else {
            choiceBox_empl.setStyle("-fx-border-color:red;");
            return false;
        }

        if(textField_firstName.getText().isEmpty() || textField_firstName.getText().length() < 2){
            textField_firstName.setStyle("-fx-border-color: red");
            return false;
        } else {
            textField_firstName.setStyle("-fx-border-color: white");
        }

        if(textField_lastName.getText().isEmpty() || textField_lastName.getText().length() < 2){
            textField_lastName.setStyle("-fx-border-color: red");
            return false;
        } else {
            textField_lastName.setStyle("-fx-border-color: white");
        }

        if(textField_phoneNumber.getText().isEmpty() || textField_phoneNumber.getText().length() < 2){
            textField_phoneNumber.setStyle("-fx-border-color: red");
            return false;
        } else {
            textField_phoneNumber.setStyle("-fx-border-color: white");
        }

        if(!checkNrPeselValidation(textField_numberPesel.getText())){
            textField_numberPesel.setStyle("-fx-border-color: red");
            return false;
        } else {
            textField_numberPesel.setStyle("-fx-border-color: white");
        }

        if(textField_salary.getText().isEmpty()){
            textField_salary.setStyle("-fx-border-color: red");
            return false;
        } else {
            textField_salary.setStyle("-fx-border-color: white");
        }

        if(textField_placeOfResidence.getText().isEmpty()){
            textField_placeOfResidence.setStyle("-fx-border-color: red");
            return false;
        } else {
            textField_placeOfResidence.setStyle("-fx-border-color: white");
        }

        if(datePicker_birthDate.getValue() == null){
            datePicker_birthDate.setStyle("-fx-border-color: red");
            return false;
        } else {
            datePicker_birthDate.setStyle("-fx-border-color: transparent");
        }

        if(datePicker_hireDate.getValue() == null){
            datePicker_hireDate.setStyle("-fx-border-color: red");
            return false;
        } else {
            datePicker_hireDate.setStyle("-fx-border-color: transparent");
        }

        if(choiceBox_employeeType.getValue() != null) {
            choiceBox_employeeType.setStyle("-fx-border-color:white;");
        } else {
            choiceBox_employeeType.setStyle("-fx-border-color:red;");
            return false;
        }



        if(choiceBox_empl.getValue().equals("Użytkownik systemu")){

            if(textField_username.getText().isEmpty()){
                textField_username.setStyle("-fx-border-color: red");
                return false;
            } else {
                textField_username.setStyle("-fx-border-color: white");
            }

            if(!checkEmailValidation(textField_email.getText())){
                textField_email.setStyle("-fx-border-color: red");
                return false;
            } else {
                textField_email.setStyle("-fx-border-color: white");
            }

            if(textField_password.getText().isEmpty()){
                textField_password.setStyle("-fx-border-color: red");
                return false;
            } else {
                textField_password.setStyle("-fx-border-color: white");
            }
            if(choiceBox_rola.getValue() != null) {
                choiceBox_rola.setStyle("-fx-border-color:white;");
            } else {
                choiceBox_rola.setStyle("-fx-border-color:red;");
                return false;
            }
        }
        return true;
    }

    private void setChoiceBoxs(){
        Thread thread = new Thread(()->{
            EmployeeRest employeeRest = new EmployeeRest();
            List<EmployeeTypeResponse> list = employeeRest.getAllEmployeType();
            for(EmployeeTypeResponse x: employeeRest.getAllEmployeType()){
                choiceBox_employeeType.getItems().add(x.getType());
            }
            choiceBox_empl.getItems().setAll("Użytkownik systemu","Zwykły pracownik");
            choiceBox_rola.getItems().setAll("Operator","Kierowca");

            choiceBox_empl.setOnAction(e->{
                if(choiceBox_empl.getValue().equals("Użytkownik systemu")){
                    vBox_credentialsBox.setDisable(false);
                }else{
                    vBox_credentialsBox.setDisable(true);
                }
            });
        });
        thread.start();
    }

    private String makePassword(){
        Random random = new Random();
        return random.ints(97,123)
                .limit(15)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private Boolean checkNrPeselValidation(String pesel){
        if(pesel.length() < 11) return false;
        if(Integer.parseInt(pesel.substring(2,4)) < 0 || Integer.parseInt(pesel.substring(2,4)) > 12) return false;
        if(Integer.parseInt(pesel.substring(4,6)) < 0 || Integer.parseInt(pesel.substring(4,6)) > 31) return false;
        return true;
    }

    private void showMessageInLabel(String message){
        label_Info.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event-> label_Info.setText(""));
        pause.play();
    }

    private Boolean checkEmailValidation(String email){
        if(email.isEmpty()) return false;
        final String stringPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+.[A-Za-z0-9]";
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Stage getStage(){
        return (Stage) borderPane.getScene().getWindow();
    }
}