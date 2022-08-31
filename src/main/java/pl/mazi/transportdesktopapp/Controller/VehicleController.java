package pl.mazi.transportdesktopapp.Controller;

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
import pl.mazi.transportdesktopapp.Rest.VehicleRest;
import pl.mazi.transportdesktopapp.https.response.VehicleResponse;
import pl.mazi.transportdesktopapp.model.VehicleTableModel;
import pl.mazi.transportdesktopapp.popup.Popup;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class VehicleController implements Initializable {
    Logger logger = Logger.getLogger(getClass().getName());

    private static final String ADD_VEHICLE_URL = "/fxml/add-vehicle.fxml";
    private static final String MENAGE_VEHICLE_TYPE_URL = "/fxml/menageVehicleType.fxml";
    private volatile String message;


    @FXML private BorderPane borderPane;
    @FXML private TableView<VehicleTableModel> vehicleTableView;
    @FXML private TableColumn<VehicleTableModel, Long> tableColumn_idVehicle;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_driver;
    @FXML private TableColumn<VehicleTableModel, String > tableColumn_mark;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_model;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_vehicleType;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_mileage;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_vinNumber;
    @FXML private TableColumn<VehicleTableModel, Double> tableColumn_weight;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_platenumber;
    @FXML private TableColumn<VehicleTableModel, LocalDate> tableColumn_dateOfTechnicalInspection;
    @FXML private TableColumn<VehicleTableModel, String> tableColumn_buttons;
    @FXML private Button button_add;
    @FXML private Button button_refresh;
    @FXML private Button button_openMenageVehicleType;

    private ObservableList<VehicleTableModel> data = FXCollections.observableArrayList();
    private VehicleRest vehicleRest;


    public VehicleController(){
        vehicleRest = new VehicleRest();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initButtons();
        initTable();
    }

    private void initTable() {
        vehicleTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableColumn_idVehicle.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,Long>("idVehicle"));
        tableColumn_mark.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,String>("mark"));
        tableColumn_model.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,String>("model"));
        tableColumn_vehicleType.setCellValueFactory(new PropertyValueFactory<VehicleTableModel, String>("vehicleType"));
        tableColumn_mileage.setCellValueFactory(new PropertyValueFactory<VehicleTableModel, String>("mileage"));
        tableColumn_vinNumber.setCellValueFactory(new PropertyValueFactory<VehicleTableModel, String>("vinNumber"));
        tableColumn_weight.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,Double>("weight"));
        tableColumn_driver.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,String >("driver"));
        tableColumn_platenumber.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,String >("plateNumber"));
        tableColumn_dateOfTechnicalInspection.setCellValueFactory(new PropertyValueFactory<VehicleTableModel,LocalDate>("dateOfTechnicalInspection"));

        Callback<TableColumn<VehicleTableModel,String>, TableCell<VehicleTableModel,String>> cellFactory =
                new Callback<TableColumn<VehicleTableModel, String>, TableCell<VehicleTableModel, String>>() {
                    @Override
                    public TableCell<VehicleTableModel, String> call(TableColumn<VehicleTableModel, String> param) {
                        final TableCell<VehicleTableModel,String> cell = new TableCell<VehicleTableModel, String>(){

                            final Button buttonDeleteVehicle = new Button("Usuń");
                            final Button buttonSetEmployeeToVehicle = new Button("Przypisz kierowce");
                            final Button buttonEditVehicle = new Button("Edytuj");


                            @Override
                            protected void updateItem(String item, boolean empty) {
                                buttonEditVehicle.setStyle("-fx-background-color: #0059DE; -fx-text-fill: white;");
                                buttonEditVehicle.setTooltip(new Tooltip("Edytuj pojazd"));
                                buttonSetEmployeeToVehicle.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                                buttonSetEmployeeToVehicle.setTooltip(new Tooltip("Przypisz kierowce do pojazdu"));
                                buttonDeleteVehicle.setTooltip(new Tooltip("Usuń pojazd"));
                                buttonDeleteVehicle.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                                HBox hbox = new HBox(buttonEditVehicle, buttonSetEmployeeToVehicle, buttonDeleteVehicle);
                                hbox.setAlignment(Pos.CENTER);
                                hbox.setSpacing(10);

                                if(empty){
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    buttonDeleteVehicle.setOnAction(event->{
                                        VehicleTableModel vehicle = getTableView().getItems().get(getIndex());
                                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                        alert.setTitle("Usunięcie");
                                        alert.setHeaderText("Czy na pewno chcesz usunąć pojazd?\nOperacji nie można cofnąć!");
                                        alert.setContentText(vehicle.toString());
                                        Optional<ButtonType> result = alert.showAndWait();
                                        if(result.get() == ButtonType.OK){
                                            try {
                                                Thread thread = new Thread(() -> {
                                                    message = vehicleRest.deleteVehicle(vehicle.getIdVehicle());
                                                });
                                                thread.start();
                                                thread.join();
                                                Popup popup = new Popup("Info",message);
                                                popup.show();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                                    buttonEditVehicle.setOnAction(actionEvent -> {
                                        VehicleTableModel vehicle = getTableView().getItems().get(getIndex());
                                        openEditView(vehicle.getIdVehicle());
                                    });


                                    buttonSetEmployeeToVehicle.setOnAction(actionEvent -> {
                                        VehicleTableModel vehicle = getTableView().getItems().get(getIndex());
                                        openAssignEmployeeView(vehicle);
                                    });

                                    setGraphic(hbox);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        tableColumn_buttons.setCellFactory(cellFactory);
        loadVehicleDataFromBackend(data);
        vehicleTableView.setItems(data);

    }

    private void loadVehicleDataFromBackend(ObservableList<VehicleTableModel> data){
        try {
            Thread thread = new Thread(()->{
                data.clear();
                List<VehicleResponse> vehicleList = vehicleRest.getAllVehicle();
                data.addAll(vehicleList.stream().map(VehicleTableModel::of).collect(Collectors.toList()));
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initButtons() {
        button_add.setOnAction(actionEvent -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(ADD_VEHICLE_URL));
                if(borderPane.getRight() == null) borderPane.setRight(root);
                else borderPane.setRight(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        button_refresh.setOnAction(actionEvent -> {
            loadVehicleDataFromBackend(data);
        });

        button_openMenageVehicleType.setOnAction(actionEvent -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource(MENAGE_VEHICLE_TYPE_URL));
                if(borderPane.getRight() == null) borderPane.setRight(root);
                else borderPane.setRight(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

private void openAssignEmployeeView(VehicleTableModel vehicleTableModel){
    if(vehicleTableModel != null){
        try {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/assignEmployee.fxml"));
            stage.setScene(new Scene(fxmlLoader.load()));
            AssignEmployeeController assignEmployeeController= fxmlLoader.getController();
            assignEmployeeController.setVehicleLabel(vehicleTableModel);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


private void openEditView(Long idVehicle){
    try {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/edit-vehicle.fxml"));
        stage.setScene(new Scene(fxmlLoader.load()));
        EditVehicleController editVehicleController = fxmlLoader.getController();
        editVehicleController.setAllLabels(idVehicle);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
