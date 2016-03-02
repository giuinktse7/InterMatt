package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import control.ProductHBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import util.ShoppingCartHandler;

public class ShoppingCartController implements Initializable {

	@FXML private Button gotoShoppingListButton;
	@FXML private Button saveShoppingListButton;
	

	@FXML private Label lblTotalCost;
	@FXML private ListView<ProductHBox> cart;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		ShoppingCartHandler.getInstance().setCart(cart);
		ShoppingCartHandler.getInstance().passLabel(lblTotalCost);
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	public Button getSaveListButton(){
		return saveShoppingListButton;
	}
}
