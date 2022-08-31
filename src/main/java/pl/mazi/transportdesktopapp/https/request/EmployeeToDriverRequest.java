package pl.mazi.transportdesktopapp.https.request;

import lombok.Data;

@Data
public class EmployeeToDriverRequest {
    private Long idVehicle;
    private Long idEmployee;

    public EmployeeToDriverRequest(Long idVehicle, Long idEmployee) {
        this.idVehicle = idVehicle;
        this.idEmployee = idEmployee;
    }
}
