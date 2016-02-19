package controllers;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;

public class StoreController implements Initializable {

	@FXML private Button gotoShoppingListButton;
	@FXML private Tab startTab;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
	}

	
	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	private final EventHandler<ActionEvent> START_CLICKED = (event) -> {
		
	};
}
