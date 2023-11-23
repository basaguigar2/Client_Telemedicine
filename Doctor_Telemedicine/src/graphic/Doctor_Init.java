/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;

import java.util.ArrayList;

import communication.Doctor_Object;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Doctor_Init extends Application {

	private static Doctor_Object doc = null;

	@Override
	public void start(Stage stage) throws Exception {
		doc = new Doctor_Object();
		doc.connection_client();
		Parent root = FXMLLoader.load(getClass().getResource("logIn.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static Doctor_Object getDoc() {
		return doc;
	}
}
