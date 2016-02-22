package controllers;

import java.net.URL;

import java.util.ResourceBundle;
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
	
	private enum View {
		WELCOME, STORE, CREDENTIALS, PURCHASE;
		
		private static View[] values = View.values();
		
		public View previous() {
			return values[Math.max(0, this.ordinal() - 1)];
		}
		
		public View next() {
			return values[(this.ordinal() + 1) % values.length];
		}
	}
	
	private View currentView = View.WELCOME;
	
	public void initialize(URL url, ResourceBundle bundle) {
		nextButton.setOnAction(NEXT_SCREEN);
		prevButton.setOnAction(PREV_SCREEN);
	}
	
	private final EventHandler<ActionEvent> NEXT_SCREEN = e -> {
		//Pane screens[] = {welcomePane, storePane, credentialsPane};
		currentView = currentView.next();
		getPanes()[currentView.ordinal()].toFront();
	};
	
	private final EventHandler<ActionEvent> PREV_SCREEN = e -> {
		//Pane screens[] = {welcomePane, storePane, credentialsPane};
		currentView = currentView.previous();
		getPanes()[currentView.ordinal()].toFront();
	};
	
	private Pane[] getPanes() {
		return new Pane[]{welcomePane, storePane, credentialsPane, purchasePane};
	}

}
