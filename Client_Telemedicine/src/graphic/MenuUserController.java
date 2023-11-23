/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;

import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import Objects.*;
import communication.Client_Object;
import Bitalino.BitalinoDemo;

public class MenuUserController implements Initializable {

	@FXML
	private TabPane TabPane;

	@FXML
	private Tab ListTests, AddTest;

	@FXML
	private TextField mac_Address;

	@FXML
	private TableView<Test> testTable;

	@FXML
	private TableColumn<Test, String> signalCol;

	@FXML
	private TableColumn<Test, Date> dateTest;

	@FXML
	private TableColumn<Test, Integer> freqCol, idCol;

	@FXML
	private Button seeTestButton, addTestButton, log_outButton;

	@FXML
	private ToggleGroup frequency, type_signal;

	@FXML
	private RadioButton emg_S, ecg_S, hundredH, tenH, thousandH;

	@FXML
	private Label nameError, medCardError;

	@FXML
	private Pane addPatient_pane;

	private static Test t = new Test();
	BitalinoDemo demo = new BitalinoDemo();
	Client_Object start_cl = Client_Init.getCl();
	Client cl = new Client();
	private ArrayList<Test> array_t = new ArrayList<>();
	private ObservableList<Test> tests = FXCollections.observableArrayList();

	@FXML
	private void go_to_addPatient(ActionEvent event) {
		TabPane.getSelectionModel().select(AddTest);
	}

	@FXML
	private void go_to_ListTest(ActionEvent event) {
		tests.addAll(array_t);
		testTable.setEditable(true);
		testTable.setItems(tests);
		TabPane.getSelectionModel().select(ListTests);
	}

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
			Logger.getLogger(MenuUserController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	public void log_out(ActionEvent event) {
		start_cl.releaseResources();
		System.exit(0);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		cl = LogInController.getCl();
		dateTest.setCellValueFactory(new PropertyValueFactory<Test, Date>("date"));
		signalCol.setCellValueFactory(new PropertyValueFactory<Test, String>("signal"));
		freqCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("frequence"));
		idCol.setCellValueFactory(new PropertyValueFactory<Test, Integer>("id"));
		start_cl.send_election('j');
		array_t = start_cl.receive_test_List(cl.getId());
		tests.addAll(array_t);
		testTable.setEditable(true);
		testTable.setItems(tests);
		// Creation of radioButtons groups
		type_signal = new ToggleGroup();
		emg_S.setToggleGroup(type_signal);
		ecg_S.setToggleGroup(type_signal);

		frequency = new ToggleGroup();
		tenH.setToggleGroup(frequency);
		hundredH.setToggleGroup(frequency);
		thousandH.setToggleGroup(frequency);
	}

	@FXML
	public void addTestButtonPushed(ActionEvent event) throws NotBoundException, SQLException {

		String te;
		if (!mac_Address.getText().isEmpty() && (emg_S.isSelected() || ecg_S.isSelected())
				&& (thousandH.isSelected() || hundredH.isSelected() || tenH.isSelected())) {

			if (emg_S.isSelected()) {
				if (tenH.isSelected()) {
					te = demo.bital(mac_Address.getText(), 10, 0);
					t.setSignal("EMG");
					t.setFrequence(10);
				} else if (hundredH.isSelected()) {
					te = demo.bital(mac_Address.getText(), 100, 0);
					t.setSignal("EMG");
					t.setFrequence(100);
				} else {
					te = demo.bital(mac_Address.getText(), 1000, 0);
					t.setSignal("EMG");
					t.setFrequence(1000);
				}
			} else {
				if (tenH.isSelected()) {
					te = demo.bital(mac_Address.getText(), 10, 1);
					t.setSignal("ECG");
					t.setFrequence(10);
				} else if (hundredH.isSelected()) {
					te = demo.bital(mac_Address.getText(), 100, 1);
					t.setSignal("ECG");
					t.setFrequence(100);
				} else {
					te = demo.bital(mac_Address.getText(), 1000, 1);
					t.setSignal("ECG");
					t.setFrequence(1000);
				}
			}
			t.setDate(Date.valueOf(LocalDate.now()));
			t.setColumn(te);
			start_cl.send_election('g');
			start_cl.send_column(t);
			start_cl.send_election('f');
			start_cl.send_test(cl.getName(), t);
			tests.add(t);

			try {
				Parent root;
				root = FXMLLoader.load(getClass().getResource("showPatient.fxml"));
				Scene scene = new Scene(root);
				Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Missing or incompatible data");
			alert.setHeaderText("Please, fulfill all the sections correctly");
			alert.showAndWait();
		}
		clearData();
	}

	public static void infoMessage(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
	
	public void clearData() {
		this.type_signal.selectToggle(emg_S);
		this.frequency.selectToggle(tenH);
	}

	public static Test getT() {
		return t;
	}
}
