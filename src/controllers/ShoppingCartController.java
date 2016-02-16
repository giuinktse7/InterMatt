package controllers;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ShoppingCartController implements Initializable {

	@FXML Button gotoShoppingListButton;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
	}

	
	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
}
