package pl.mazi.transportdesktopapp.https.response;

import lombok.Data;

@Data
public class VehicleTypeResponse {
    private Long idVehicleType;
    private String type;

    @Override
    public String toString() {
        return type;
    }
}
