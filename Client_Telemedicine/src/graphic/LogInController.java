
package graphic;

import Objects.*;
import java.io.IOException;
import communication.Client_Object;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;


public class LogInController {
    

	Client_Object start_cl = Client_Init.getCl();;
	User u = new User();
	private static Client cl = new Client();
    
    @FXML
    private PasswordField passwordlogin;

    @FXML
    private Button loginbutton;

    @FXML
    private TextField usernamelogin;
    
    
    @FXML
    public void login_User(ActionEvent event) {
        Window owner = loginbutton.getScene().getWindow();
        if (usernamelogin.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Error!", "Please enter your username.");
            return;
        }
        if (passwordlogin.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Error!", "Please enter your password.");
            return;
        }

        String username = usernamelogin.getText();
        String password = passwordlogin.getText();
        start_cl.send_election('b');
        String conf = start_cl.send_login(username, password);
        if (conf.equalsIgnoreCase("ok c")) {
        	infoMessage("Successfull log in !!", null, "Message");
        	cl.setName(username);
        	start_cl.send_election('c');
      	  	int id = start_cl.search_client(cl.getName());
    		cl.setId(id);
            try{
              Parent root = FXMLLoader.load(getClass().getResource("menuUser.fxml"));
              Scene scene = new Scene(root);
              Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
              stage.setScene(scene);
              stage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
		} else if (conf.equalsIgnoreCase("The user does not exist")) {
			infoMessage("Please enter correct username or password", null, "Failed");
            return;
		} 
    }
    
    @FXML
    public void go_ToRegister(ActionEvent event) {
        try{
              Parent root = FXMLLoader.load(getClass().getResource("register.fxml"));
              Scene scene = new Scene(root);
              Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
              stage.setScene(scene);
              stage.show();
            } catch(IOException e) {
                e.printStackTrace();
            }
    }
    public static void infoMessage(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static void showAlert(Alert.AlertType alertType, Window owner, String title, String message ) {
       Alert alert = new Alert(alertType);
       alert.setTitle(title);
       alert.setHeaderText(null);
       alert.setContentText(message);
       alert.initOwner(owner);
       alert.show();
    }

	public static Client getCl() {
		return cl;
	}
    
}
