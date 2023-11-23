/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import Objects.*;
import communication.Doctor_Object;

public class MenuDoctorController implements Initializable {

	@FXML
	private TableView<Test> testTable;

	@FXML
	private TableColumn<Test, String> signalCol;

	@FXML
	private TableColumn<Test, Date> dateTest;

	@FXML
	private TableColumn<Test, Integer> freqCol, idCol;

	@FXML
	private Button seeTestButton, back_button;

	@FXML
	private Label nameError, medCardError;

	private static Test t = new Test();
	Doctor_Object start_doc = Doctor_Init.getDoc();
	Client cl = new Client();
	private ArrayList<Test> array_t = new ArrayList<>();
	private ObservableList<Test> tests = FXCollections.observableArrayList();

	@FXML
	public void seeTestButtonPushed(ActionEvent event) {
		Parent root;
		try {
			if (this.testTable.getSelectionModel().getSelectedItem() != null) {
				t = this.testTable.getSelectionModel().getSelectedItem();
				root = FXMLLoader.load(getClass().getResource("showPatient.fxml"));
				Scene scene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			} else {
				infoMessage("Select one test", null, "Failed");
	            return;
			}
		} catch (IOException ex) {
			Logger.getLogger(MenuDoctorController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void back_button(ActionEvent event) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("menuUser1.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cl = MenuUserControllerDoc.getCl();
		dateTest.setCellValueFactory(new PropertyValueFactory<Test, Date>("date"));
		signalCol.setCellValueFactory(new PropertyValueFactory<Test, String>("signal"));
		freqCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("frequence"));
		idCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("id"));
		start_doc.send_election('j');
		array_t = start_doc.receive_test_List(cl.getId());
		tests.addAll(array_t);
		testTable.setEditable(true);
		testTable.setItems(tests);
	}

	public static void infoMessage(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
	
	public static Test getT() {
		return t;
	}
}
