package controllers;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {
	
	private enum View {
		WELCOME, STORE, CHECKOUT, PURCHASE
	}
	
	@FXML private HBox rootPane;
	@FXML private StackPane contentPane;
	@FXML private Button prevButton;
	@FXML private Button nextButton;
	
	@SuppressWarnings("unused")
	private View prevView = View.WELCOME;
	@SuppressWarnings("unused")
	private View nextView = View.STORE;
	
	
	@FXML private VBox storePane;
	@FXML private BorderPane welcomePane;
	@FXML private AnchorPane shoppingCart;
	@FXML private ShoppingCartController shoppingCartController;
	@FXML private StoreController storePaneController;
	
	
	public void initialize(URL url, ResourceBundle bundle) {
		bringToFront(View.STORE);
	}
	
	private void bringToFront(View view) {
		
		switch(view) {
		case WELCOME:
			prevView = View.WELCOME;
			nextView = View.STORE;
			prevButton.setOnAction(e -> welcomePane.toFront());
			nextButton.setOnAction(e -> storePane.toFront());
			welcomePane.toFront();
			
			
			break;
		case STORE:
			prevView = View.WELCOME;
			nextView = View.CHECKOUT;
			prevButton.setOnAction(e -> welcomePane.toFront());
			nextButton.setOnAction(e -> storePane.toFront());
			
			storePane.toFront();
			break;
			
			default:
				break;
		}
	}

}
