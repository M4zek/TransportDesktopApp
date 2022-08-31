package pl.mazi.transportdesktopapp.https.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleResponse {
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

    public VehicleResponse(){}

    public VehicleResponse(Long idVehicle, String driver, String mark, String model, String vehicleType, String mileage, String vinNumber, Double weight, String plateNumber, LocalDate dateOfTechnicalInspection) {
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
}
