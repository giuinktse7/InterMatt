package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class LoadListController implements Initializable {

	@FXML private AnchorPane popup;
	@FXML private Button yesButton;
	@FXML private Button cancelButton;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		
	}
	
	public Node getRoot() {
		return this.popup;
	}
	
	public Button getYesButton() {
		return yesButton;
	}
	
	public Button getCancelButton() {
		return cancelButton;
	}

}
