package pl.mazi.transportdesktopapp.Controller;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import pl.mazi.transportdesktopapp.Rest.EmployeeRest;
import pl.mazi.transportdesktopapp.https.response.EmployeeResponse;
import pl.mazi.transportdesktopapp.model.EmployeeTableModel;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {
    private final Logger logger = Logger.getLogger(getClass().getName());

    private final EmployeeRest employeeRest;
    private volatile String message;

    @FXML private BorderPane borderPane;
    @FXML private TableView<EmployeeTableModel> tableVIewEmployee;
    @FXML private TableColumn<EmployeeTableModel, Long> columntIdEmployee;
    @FXML private TableColumn<EmployeeTableModel, String> columnFirstName;
    @FXML private TableColumn<EmployeeTableModel, String> columnLastName;
    @FXML private TableColumn<EmployeeTableModel, Double> columnSalary;
    @FXML private TableColumn<EmployeeTableModel, LocalDate> columnHireDate;
    @FXML private TableColumn<EmployeeTableModel, LocalDate> columnBirthDate;
    @FXML private TableColumn<EmployeeTableModel, String> columnEmployeeType;
    @FXML private TableColumn<EmployeeTableModel, String> columnEditRemove;
    @FXML private TableColumn<EmployeeTableModel, String> columnPlaceOfResidence;
    @FXML private TableColumn<EmployeeTableModel, String> columnPeselNumber;
    @FXML private TableColumn<EmployeeTableModel, String> column_PhoneNumber;
    @FXML private Label label_info;
    @FXML private Button buttonAddNewEmployee;
    @FXML private Button buttonRefreshTable;
    @FXML private Button buttonAddNewEmployeeType;

    public EmployeeController() {
        this.employeeRest = new EmployeeRest();
    }

    private ObservableList<EmployeeTableModel> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableView();
        initButtonAddNewEmploye();
        initButtonRefreshTable();
        initButtonAddNewEmployeeType();
    }

    private void initButtonAddNewEmployeeType() {
        buttonAddNewEmployeeType.setOnAction(actionEvent -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/add-remove-employeeType.fxml"));
                if(borderPane.getRight() == null){
                    borderPane.setRight(root);
                } else{
                    borderPane.setRight(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    /*
        Zainicjowanie przycisku do odświeżenia tabeli pracownicy (Wcztanie danych z beckendu)
     */
    private void initButtonRefreshTable() {
        buttonRefreshTable.setOnAction(event -> {
            loadEmployeesData(data);
        });
    }

    /*
        Zainicjowanie przycisku do otworzenia widoku dodania pracownika
     */
    private void initButtonAddNewEmploye() {
        buttonAddNewEmployee.setTooltip(new Tooltip("Dodaj nowego pracownika"));
        buttonAddNewEmployee.setOnAction(actionEvent -> {
            try {
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/add-employee.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                stage.setTitle("MM - Transport | Dodaj nowego pracownika");
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /*
        Wczytanie danych z beckendu oraz wczytanie ich do tabeli pracownicy
     */
    void initTableView(){
        columntIdEmployee.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,Long>("idEmployee")); columntIdEmployee.setPrefWidth(50.0);
        columnFirstName.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,String>("firstName"));
        columnLastName.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,String>("lastName"));
        columnSalary.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,Double>("salary"));
        columnHireDate.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,LocalDate>("hireDate"));
        columnBirthDate.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,LocalDate>("birthDate"));
        columnEmployeeType.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,String>("employeeType"));
        columnPlaceOfResidence.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,String>("placeOfResidence"));
        columnPeselNumber.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel,String>("peselNumber"));
        column_PhoneNumber.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String >("phoneNumber"));


//        ObservableList<EmployeeTableModel> data = FXCollections.observableArrayList();

        Callback<TableColumn<EmployeeTableModel,String>, TableCell<EmployeeTableModel,String>> cellFactory =
                new Callback<TableColumn<EmployeeTableModel, String>, TableCell<EmployeeTableModel, String>>() {
                    @Override
                    public TableCell<EmployeeTableModel, String> call(TableColumn<EmployeeTableModel, String> param) {
                        final TableCell<EmployeeTableModel,String> cell = new TableCell<EmployeeTableModel, String>(){

                            final Button buttonDeleteEmployee = new Button("Usuń");
                            final Button buttonEditEmployee = new Button("Edytuj");


                            @Override
                            protected void updateItem(String item, boolean empty) {
                                buttonEditEmployee.setStyle("-fx-background-color: #0059DE; -fx-text-fill: white;");
                                buttonEditEmployee.setTooltip(new Tooltip("Edytuj pracownika"));

                                buttonDeleteEmployee.setTooltip(new Tooltip("Usuń pracownika"));
                                buttonDeleteEmployee.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                                HBox hbox = new HBox(buttonDeleteEmployee, buttonEditEmployee);
                                hbox.setAlignment(Pos.CENTER);
                                hbox.setSpacing(10);

                                if(empty){
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    buttonDeleteEmployee.setOnAction(event->{
                                        EmployeeTableModel employeeTableModel = getTableView().getItems().get(getIndex());
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Potwierdź");
                                        alert.setHeaderText("Czy na pewno chcesz usunąć pracownika?\nOperacji nie będzie można cofnąć!");
                                        alert.setResizable(false);
                                        alert.setContentText("Pracownik: " + employeeTableModel.getFirstName() + " " + employeeTableModel.getLastName());
                                        Optional<ButtonType> result = alert.showAndWait();
                                        if(result.get() == ButtonType.OK) {
                                            try {
                                                Thread thread = new Thread(() -> {
                                                    message = EmployeeRest.deleteEmployee(employeeTableModel.getIdEmployee());
                                                });
                                                thread.start();
                                                thread.join();

                                                label_info.setText(message);
                                                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                                                pause.setOnFinished(e->label_info.setText(""));
                                                pause.play();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }else{
                                            alert.close();
                                        }
                                    });

                                    buttonEditEmployee.setOnAction(event->{
                                        showEditPage(getTableView().getItems().get(getIndex()));
                                    });
                                    setGraphic(hbox);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        columnEditRemove.setCellFactory(cellFactory);
        columnEditRemove.setPrefWidth(50);
        loadEmployeesData(data);
        tableVIewEmployee.setItems(data);
    }


    /*
        Otworzenie okna dla edytowania pracownika orazy wywołanie metody wczytującej dane do formularza
     */
    private void showEditPage(EmployeeTableModel employeeTableModel){
        if(employeeTableModel != null){
            try {
                Stage editStage = createEditStage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/edit-employee.fxml"));
                editStage.setScene(new Scene(fxmlLoader.load()));
                EditEmployeeController editEmployeeController = fxmlLoader.getController();
                editEmployeeController.loadEmployeeDate(employeeTableModel.getIdEmployee());
                editStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /*

        Stworzenie stage
     */
    private Stage createEditStage(){
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    // Load Data from server
    private void loadEmployeesData(ObservableList<EmployeeTableModel> data) {
        Thread thread = new Thread(()->{
            data.clear();
            List<EmployeeResponse> employeeResponseList = employeeRest.getEmployees();
            data.addAll(employeeResponseList.stream().map(EmployeeTableModel::of).collect(Collectors.toList()));
        });
        thread.start();
    }

    /*
        Funkcja zwracająca główne okno EmployeeView.
     */
    private Stage getStage(){
        return (Stage) borderPane.getScene().getWindow();
     }
}
