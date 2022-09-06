# Transport Desktop Application
A desktop application for managing a transportation company.

## About
A simple desktop application for managing a transportation company.
It is based on communication with the [Transport Server](https://github.com/M4zek/TransportServer) via HTTP requests secured with SSL Handshake 
and each request is attached with a [JSON Web Token](https://jwt.io/introduction) to authenticate the user. At the moment the application is not finished therefore it can be used as a purely database application to store information about employees and vehicles.
It was implemented using Java, JavaFX and cascading style sheets (CSS).


#### Futures
- Logging into the system
- Employee management
- Vehicle management
- Account data management 

#### To be implemented
- Driver management
- Driver time management
- Creating a chat room between employees
- Managing orders and assigning them to drivers

The application is not yet finished while I hope it will be finished in the future.

## Screenshot
Below I have provided a couple of screenshots of the current version of the program.</br>
<img src="https://github.com/M4zek/TransportDesktopApp/blob/master/src/main/resources/ss/account.png" width="600" height="450" />
<img src="https://github.com/M4zek/TransportDesktopApp/blob/master/src/main/resources/ss/employee.png" width="600" height="450" />
<img src="https://github.com/M4zek/TransportDesktopApp/blob/master/src/main/resources/ss/vehicle.png" width="600" height="450" />
</br>
To realize the appearance of the application, in addition to CSS styles and built-in JavaFX resources, I used the [JFoenix](https://github.com/sshahine/JFoenix) library.

## Authors
- [M4zek](https://github.com/M4zek)
