package pl.mazi.transportdesktopapp.https.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VehicleRequest {
    private Long idEmployee;
    private String mark;
    private String model;
    private String vehicleType;
    private String mileage;
    private String vinNumber;
    private Double weight;
    private String plateNumber;
    private LocalDate dateOfTechnicalInspection;
}
