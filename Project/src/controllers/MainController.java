package controllers;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {
	
	
	private enum View {
		WELCOME, STORE, CHECKOUT, PURCHASE
	}
	
	@FXML private HBox rootPane;
	@FXML private StackPane contentPane;
	@FXML private AnchorPane loadListPane;
	@FXML private Button prevButton;
	@FXML private Button nextButton;
	
	private View prevView = View.WELCOME;
	private View nextView = View.STORE;
	
	
	@FXML private AnchorPane shoppingCart;
	@FXML ShoppingCartController shoppingCartController;
	@FXML LoadListController loadListPaneController;
	
	public void initialize(URL url, ResourceBundle bundle) {
		shoppingCartController.getShoppingListButton().setOnAction((e) -> {
		});
		
		bringToFront(View.WELCOME);
	}
	
	private void bringToFront(View view) {
		EventHandler<ActionEvent> nextAction = (e) -> {}; 
		EventHandler<ActionEvent> previousAction = (e) -> {};
		
		String id = "";
		
		switch(view) {
		case WELCOME:
			prevView = View.WELCOME;
			nextView = View.STORE;
			previousAction = (e) -> bringToFront(prevView);
			nextAction = (e) -> bringToFront(nextView);
			id = "welcomePane";
			
			break;
		case STORE:
			prevView = view.WELCOME;
			nextView = View.CHECKOUT;
			previousAction = (e) -> bringToFront(prevView);
			nextAction = (e) -> bringToFront(nextView);
			id = "storePane";
			break;
			
			default:
				break;
		}
		
		prevButton.setOnAction(previousAction);
		nextButton.setOnAction(nextAction);
		
		for (int i = 0; i < contentPane.getChildren().size(); ++i) {
			Node child = contentPane.getChildren().get(i);
			
			boolean b = id.equals(child.getId());
			child.setDisable(!b);
			child.setVisible(b);
		}
	}

}
