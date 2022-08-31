package pl.mazi.transportdesktopapp.user;

import java.util.List;

public final class User {
    private static volatile User instance;
    private Long id;
    private String jwtToken;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String placeOfResidence;
    private List<String> roles;

    public User() {}

    public User(Long id, String jwtToken, String username, String email, String firstName, String lastName, String placeOfResidence, String phoneNumber, List<String> roles) {
        this.id = id;
        this.jwtToken = jwtToken;
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.placeOfResidence = placeOfResidence;
        this.roles = roles;
    }

    public static User getInstance(){
        User result = instance;
        if(result != null){
            return result;
        }
        synchronized (User.class){
            if(instance == null){
                instance = new User();
            }
            return instance;
        }
    }


    // GETTERS AND SETTERS
    public void setInstance(User instance) {
        User.instance = instance;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", jwtToken='" + jwtToken + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", placeOfResidence='" + placeOfResidence + '\'' +
                ", roles=" + roles +
                '}';
    }
}
