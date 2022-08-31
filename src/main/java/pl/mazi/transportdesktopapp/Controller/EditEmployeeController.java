package pl.mazi.transportdesktopapp.Controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.https.response.EmployeeResponse;
import pl.mazi.transportdesktopapp.https.response.EmployeeTypeResponse;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditEmployeeController implements Initializable {

    private EmployeeRest employeeRest;

    @FXML private TextField textField_firstName;
    @FXML private TextField textField_lastName;
    @FXML private TextField textField_phoneNumber;
    @FXML private TextField textField_peselNumber;
    @FXML private TextField textField_salary;
    @FXML private TextField textField_placeOfResidence;
    @FXML private JFXDatePicker datePicker_birthDate;
    @FXML private JFXDatePicker datePicker_hireDate;
    @FXML private ChoiceBox<String> choiceBox_employeeType;
    @FXML private JFXButton button_cancel;
    @FXML private JFXButton button_accept;
    @FXML private BorderPane borderPane;
    @FXML private Label label_Info;

    private Long idEmployee;
    private volatile String message;

    public EditEmployeeController() {
        this.employeeRest = new EmployeeRest();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setChoiceBoxes();
        initCancelButton();
        initAcceptButton();
    }

    private void initAcceptButton() {
        button_accept.setOnAction(actionEvent -> {
            EmployeeResponse employeeResponse = new EmployeeResponse();
            employeeResponse.setIdEmployee(idEmployee);
            employeeResponse.setFirstName(textField_firstName.getText());
            employeeResponse.setLastName(textField_lastName.getText());
            employeeResponse.setEmployeeType(choiceBox_employeeType.getValue());
            employeeResponse.setBirthDate(datePicker_birthDate.getValue());
            employeeResponse.setHireDate(datePicker_hireDate.getValue());
            employeeResponse.setPeselNumber(textField_peselNumber.getText());
            employeeResponse.setSalary(Double.parseDouble(textField_salary.getText()));
            employeeResponse.setPhoneNumber(textField_phoneNumber.getText());
            employeeResponse.setPlaceOfResidence(textField_placeOfResidence.getText());

            try {
                Thread thread = new Thread(()->{
                    message = employeeRest.updateEmployee(employeeResponse);
                });
                thread.start();
                thread.join();
                showMessageInLabel(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void initCancelButton() {
        button_cancel.setOnAction(actionEvent -> {
            this.getStage().close();
        });
    }

    public void loadEmployeeDate(Long idEmployee){
        try {
            Thread thread = new Thread(()->{
            EmployeeResponse employeeResponse = employeeRest.getEmployee(idEmployee);
            this.idEmployee = employeeResponse.getIdEmployee();
            textField_firstName.setText(employeeResponse.getFirstName());
            textField_lastName.setText(employeeResponse.getLastName());
            textField_phoneNumber.setText(employeeResponse.getPhoneNumber());
            textField_peselNumber.setText(employeeResponse.getPeselNumber());
            textField_salary.setText(employeeResponse.getSalary().toString());
            textField_placeOfResidence.setText(employeeResponse.getPlaceOfResidence());
            datePicker_birthDate.setValue(employeeResponse.getBirthDate());
            datePicker_hireDate.setValue(employeeResponse.getHireDate());
            choiceBox_employeeType.setValue(employeeResponse.getEmployeeType());
        });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setChoiceBoxes(){
        EmployeeRest employeeRest = new EmployeeRest();
        List<EmployeeTypeResponse> list = employeeRest.getAllEmployeType();
        for(EmployeeTypeResponse x: employeeRest.getAllEmployeType()){
            choiceBox_employeeType.getItems().add(x.getType());
        }
    }

    private void showMessageInLabel(String message){
        label_Info.setText(message);
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event-> label_Info.setText(""));
        pause.play();
    }

    private Stage getStage(){
        return (Stage) borderPane.getScene().getWindow();
    }
}
