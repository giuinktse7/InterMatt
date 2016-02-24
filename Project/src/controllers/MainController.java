package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import Util.ScreenTransition;
import Util.ShoppingCartHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController implements Initializable {
	@FXML private StackPane contentPane;
	@FXML private Button prevButton;
	@FXML private Button nextButton;
	
	@FXML private Pane purchasePane;
	@FXML private Pane credentialsPane;
	@FXML private VBox storePane;
	@FXML private BorderPane welcomePane;
	@FXML private AnchorPane shoppingCart; 
	@FXML private CredentialsController credentialsPaneController;
	@FXML private PurchaseController purchasePaneController;
	
	private ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();
	private enum View {
		STORE, CREDENTIALS, PURCHASE;
		
		private static View[] values = View.values();
		
		public View previous() {
			return values[this.ordinal() == 0 ? values.length - 1 : this.ordinal() - 1];
		}
		
		public View next() {
			return values[(this.ordinal() + 1) % values.length];
		}
	}
	
	private View currentView = View.STORE;
	
	public void initialize(URL url, ResourceBundle bundle) {
		storePane.toFront();
		nextButton.setOnAction(NEXT_SCREEN);
		prevButton.setOnAction(PREV_SCREEN);
	}
	
	private final EventHandler<ActionEvent> NEXT_SCREEN = e -> {
		//Pane screens[] = {welcomePane, storePane, credentialsPane};
		currentView = currentView.next();
		//getPanes()[currentView.ordinal()].toFront();
		getScreenTransitions()[currentView.ordinal()].run(getPanes()[currentView.ordinal()]);
	};
	
	private final EventHandler<ActionEvent> PREV_SCREEN = e -> {
		//Pane screens[] = {welcomePane, storePane, credentialsPane};
		currentView = currentView.previous();
		getPanes()[currentView.ordinal()].toFront();
		
	};
	
	private Pane[] getPanes() {
		return new Pane[]{storePane, credentialsPane, purchasePane};
	}
	
	private ScreenTransition[] getScreenTransitions(){
		return new ScreenTransition[]{
				//Replace with comments to perform checks
				pane -> pane.toFront(), //SHOW_STORE_VIEW, 
				pane -> pane.toFront(), //SHOW_CREDENTIALS_VIEW,
				pane -> pane.toFront() //SHOW_PURCHASE_VIEW
		};
	}
	private final ScreenTransition SHOW_CREDENTIALS_VIEW = pane -> {
		if (!cartHandler.isEmpty())
			pane.toFront();
		else {
			currentView = currentView.previous();
		}
	};
	
	private final ScreenTransition SHOW_PURCHASE_VIEW = pane -> {
		if (credentialsPaneController.verifyInput()){
			pane.toFront();
		}
		else {
			currentView = currentView.previous();
		}
	};
	
	private final ScreenTransition SHOW_STORE_VIEW = pane -> {
		System.out.println(purchasePaneController.verifyInput());
		if (purchasePaneController.verifyInput()){
			pane.toFront();
		}
		else {
			currentView = currentView.previous();
		}
	};
}