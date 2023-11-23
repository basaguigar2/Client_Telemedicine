/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Objects.*;
import communication.Doctor_Object;

public class MenuUserControllerDoc implements Initializable {

	@FXML
	private TableView<Client> clientTable;

	@FXML
	private TableColumn<Client, String> nameCol1;

	@FXML
	private TableColumn<Client, Integer> idCol1;

	@FXML
	private Button seeTestButton1, log_outButton1;

	@FXML
	private Label nameError;

	@FXML
	private Pane addPatient_pane;

	private static Client cl= new Client();
	Doctor_Object start_doc = Doctor_Init.getDoc();
	Doctor doc = null;
	private ArrayList<Client> array_t = new ArrayList<>();
	private ObservableList<Client> clients = FXCollections.observableArrayList();


	@FXML
	public void seeTestButtonPushed(ActionEvent event) {

		try {
			if (this.clientTable.getSelectionModel().getSelectedItem() != null) {
			cl = this.clientTable.getSelectionModel().getSelectedItem();
			Parent root = FXMLLoader.load(getClass().getResource("menuUser2.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			}else {
				infoMessage("Select one client", null, "Failed");
	            return;
			}
		} catch (IOException ex) {
			Logger.getLogger(MenuUserControllerDoc.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void log_out(ActionEvent event) {
		start_doc.releaseResources();
		System.exit(0);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		doc = LogInController.getDoc();
		nameCol1.setCellValueFactory(new PropertyValueFactory<Client, String>("name"));
		idCol1.setCellValueFactory(new PropertyValueFactory<Client, Integer>("id"));
		start_doc.send_election('d');
		array_t = start_doc.receive_client_List(doc.getId());
		clients.addAll(array_t);
		clientTable.setEditable(true);
		clientTable.setItems(clients);
	}

	public static void infoMessage(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
	
	public static Client getCl() {
		return cl;
	}
}
