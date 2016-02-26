package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Pane;
import util.ModalPopup;
import util.ShoppingCartHandler;

public class ShoppingCartController implements Initializable {

	@FXML private Button gotoShoppingListButton;
	@FXML private ListView<Node> cart;
	@FXML private Label lblTotalCost;
	
	private Pane listPane;
	private LoadListController controller;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		ShoppingCartHandler.getInstance().setCart(cart);
		ShoppingCartHandler.getInstance().passLabel(lblTotalCost);
		
		gotoShoppingListButton.setOnAction(openListLoadView());
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	private ColorAdjust createGreyOutEffect() {
		ColorAdjust greyOut = new ColorAdjust();
		//greyOut.setSaturation(-0.5);
		greyOut.setBrightness(-0.2);
		return greyOut;
	}
	
	private void loadListView() throws IOException {
		FXMLLoader view = new FXMLLoader();
		view.setLocation(this.getClass().getResource("../fxml/load_list.fxml"));
		
		this.listPane = view.load();
		
		this.controller = view.getController();
	}
	
	private EventHandler<ActionEvent> openListLoadView() {
		return e -> {
			try {
			loadListView();
			} catch(IOException exception) {
				exception.printStackTrace();
			}
			ModalPopup loadListPopup = new ModalPopup(listPane);
		};
	}
}
