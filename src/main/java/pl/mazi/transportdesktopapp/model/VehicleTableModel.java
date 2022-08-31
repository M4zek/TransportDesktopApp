package pl.mazi.transportdesktopapp.model;

import lombok.Data;
import pl.mazi.transportdesktopapp.https.response.VehicleResponse;

import java.time.LocalDate;

@Data
public class VehicleTableModel {
    private Long idVehicle;
    private String driver;
    private String mark;
    private String model;
    private String vehicleType;
    private String mileage;
    private String vinNumber;
    private Double weight;
    private String plateNumber;
    private LocalDate dateOfTechnicalInspection;

    public VehicleTableModel(){}

    public VehicleTableModel(Long idVehicle, String driver, String mark, String model, String vehicleType, String mileage, String vinNumber, Double weight, String plateNumber, LocalDate dateOfTechnicalInspection) {
        this.idVehicle = idVehicle;
        this.driver = driver;
        this.mark = mark;
        this.model = model;
        this.vehicleType = vehicleType;
        this.mileage = mileage;
        this.vinNumber = vinNumber;
        this.weight = weight;
        this.plateNumber = plateNumber;
        this.dateOfTechnicalInspection = dateOfTechnicalInspection;
    }

    public static VehicleTableModel of(VehicleResponse vehicleResponse){
        return new VehicleTableModel(
                vehicleResponse.getIdVehicle(),
                vehicleResponse.getDriver(),
                vehicleResponse.getMark(),
                vehicleResponse.getModel(),
                vehicleResponse.getVehicleType(),
                vehicleResponse.getMileage(),
                vehicleResponse.getVinNumber(),
                vehicleResponse.getWeight(),
                vehicleResponse.getPlateNumber(),
                vehicleResponse.getDateOfTechnicalInspection());
    }

    @Override
    public String toString() {
        return "Pojazd: \n" +
                "\tKierowca: " + driver + '\n' +
                "\tMarka: " + mark + '\n' +
                "\tModel:'" + model + '\n' +
                "\tTyp: " + vehicleType + '\n' +
                "\tPrzebieg: " + mileage + '\n' +
                "\tVIN: " + vinNumber + '\n' +
                "\tMasa całkowita: " + weight + '\n' +
                "\tNumer Rejestracyjny: " + plateNumber + '\n' +
                "\tTermin badania technicznego: " + dateOfTechnicalInspection;
    }
}
