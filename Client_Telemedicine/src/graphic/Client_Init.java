/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;

import communication.Client_Object;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client_Init extends Application {

	private static Client_Object cl = null;
    @Override
    public void start(Stage stage) throws Exception {
    		cl = new Client_Object();
    		cl.connection_client();
            Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

	public static Client_Object getCl() {
		return cl;
	}
}
