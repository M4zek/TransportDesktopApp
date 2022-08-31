package pl.mazi.transportdesktopapp.https.response;

import lombok.Data;

@Data
public class DriverResponse {
    private Long idEmployee;
    private String firstName;
    private String lastName;

    @Override
    public String toString() {
        return idEmployee + ", " + firstName + " " + lastName;
    }
}
