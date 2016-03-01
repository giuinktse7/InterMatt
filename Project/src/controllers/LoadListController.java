package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import control.ModalPopup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class LoadListController implements Initializable {

	@FXML private ModalPopup bottomPane;
	@FXML private Button yesButton;
	@FXML private Button cancelButton;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		yesButton.setOnAction(e -> bottomPane.close());
		cancelButton.setOnAction(e -> bottomPane.close());
	}

}
