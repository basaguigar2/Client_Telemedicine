/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;


import java.io.IOException;
import java.sql.SQLException;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import communication.Client_Object;
import Objects.*;


public class RegisterController{
	
    Client_Object start_cl = Client_Init.getCl();
    User u = new User();
    @FXML
    private PasswordField password, repeatpassword;

    @FXML
    private Button register, backButton;

    @FXML
    private TextField username, emailtxt;

    private boolean check = true;
    private static SceneChanger sc;

    @FXML
    public void RegisterUser(ActionEvent event) throws IOException, SQLException {
        Window window = register.getScene().getWindow();

        if ((username.getText().isEmpty())) {
            showAlert(Alert.AlertType.ERROR, window, "Error!", "Please enter your username");
            return;
        }

        if (password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, window, "Error!", "Please enter your password");
            return;
        }

        if (repeatpassword.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, window, "Error!", "Please enter your password repeated");

            return;
        }

        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.repeatpassword.getText();
        
        if (!password.equals(confirmPassword)) {
            check = false;
            showAlert(Alert.AlertType.ERROR, window, "Error!", "The passwords introduced do not match");
            return;
        } else {
            check = true;
        }
        if (check == true) {
        	u = new User(username,password,1);
            try {
            	start_cl.send_election('a');
                String conf = start_cl.send_registerCL(u);
                if (conf.length() != 2) {
                	infoMessage("ERROR, existing user", null, "Failed");
                } else if (conf.length() > 2 && conf.length()< 15) {
                	infoMessage("ERROR, no doctors available", null, "Failed");
                }else {
                    Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void backtoMenu(ActionEvent event) {
        sc = new SceneChanger();
        sc.changeScenes(event, "logIn.fxml");
    }

    public static void infoMessage(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
