/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package graphic;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.util.ResourceBundle;
import Objects.Test;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ShowPatientController implements Initializable{
    
    @FXML
    private TextField dateTextField, signalTextField, freqTextField;
    
    @FXML
    private TextArea TestTextArea;
    
    @FXML
    private Button backButton;
    private Test selectedTest;    
    @FXML
    public void backtoMenu(ActionEvent event) throws SocketException {
    	Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("menuUser.fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
    }
    
    public void setInfoTest(Test t) {
        this.selectedTest = t;
        this.dateTextField.setText(selectedTest.getDate().toString());
        this.signalTextField.setText(selectedTest.getSignal());
        this.freqTextField.setText(""+selectedTest.getFrequence());
        this.TestTextArea.setText(selectedTest.getColumn());
        this.dateTextField.setEditable(false);
        this.signalTextField.setEditable(false);
        this.freqTextField.setEditable(false);
        this.TestTextArea.setEditable(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.backButton.setDisable(false);
        selectedTest = MenuUserController.getT();
        setInfoTest(selectedTest);
    }
}
