package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import interfaces.Performable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.Condition;
import util.ContentView;
import util.ModalPopup;
import util.NavigationButton;
import util.ShoppingCartHandler;
import util.ViewDisplay;

public class MainController implements Initializable {
	@FXML private StackPane contentPane;
	@FXML private Button prevButton;
	@FXML private Button nextButton;
	@FXML private Button purchaseHistoryButton;
	
	//Navigation buttons
	@FXML private NavigationButton navButton1;
	@FXML private NavigationButton navButton2;
	@FXML private NavigationButton navButton3;
	@FXML private NavigationButton navButton4;
	
	@FXML private Pane purchasePane;
	@FXML private Pane credentialsPane;
	@FXML private Pane storePane;
	@FXML private Pane shoppingCart;
	@FXML private Pane purchaseHistoryPane;
	@FXML private StoreController storePaneController;
	@FXML private CredentialsController credentialsPaneController;
	@FXML private PurchaseController purchasePaneController;
	@FXML private PurchaseHistoryController purchaseHistoryPaneController;
	
	@FXML private StackPane wrapperStackPane;
	@FXML private VBox mainContentWrapper;
	
	ViewDisplay display;
	
	private ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();
	
	public void initialize(URL url, ResourceBundle bundle) {
		configurePopupStackPane();
		display = new ViewDisplay(contentPane);
		
		//Setup navigation-button bindings
		initiateNavigationButtons();
		
		ContentView storeView = new ContentView(storePane);
		
		ContentView credentialsView = new ContentView(credentialsPane);
		credentialsView.require(CART_NOT_EMPTY);
		
		ContentView purchaseView = new ContentView(purchasePane);
		
		storeView.setNext(credentialsView);
		
		credentialsView.setNext(purchaseView);
		credentialsView.setPrevious(storeView);
		
		purchaseView.setPrevious(credentialsView);
		
		display.show(storeView);
		prevButton.setOnAction(event -> display.previous());
		nextButton.setOnAction(event -> display.next());
		navButton1.setOnAction(event -> display.show(storeView));
		navButton2.setOnAction(event -> display.show(credentialsView));
		navButton3.setOnAction(event -> display.show(purchaseView));
		navButton4.setOnAction(event -> display.show(storeView));
		
		
		purchaseHistoryPane.setMaxWidth(Double.MAX_VALUE);
		purchaseHistoryPane.setMaxHeight(Double.MAX_VALUE);
		
		ModalPopup historyPopup = new ModalPopup(purchaseHistoryPane);
		purchaseHistoryButton.setOnAction(event -> historyPopup.show());
		purchaseHistoryPaneController.setCloseAction(() -> historyPopup.hide());
	}
	private final Condition CART_NOT_EMPTY = new Condition(() -> { return !cartHandler.isEmpty(); }, print("You can not proceed with an empty cart!"));
	
	private final BooleanBinding CART_NONEMPTY = Bindings.createBooleanBinding(() -> !cartHandler.emptyProperty().get(), cartHandler.emptyProperty());
	
	private void initiateNavigationButtons() {
		navButton2.addBinding(CART_NONEMPTY);
		navButton2.update();
		
		for (BooleanBinding binding : credentialsPaneController.getBindings())
			navButton3.addBinding(binding);
		
		navButton3.update();
	}
	
	private static final Performable print(String s) {
		return () -> System.out.println(s);
	}
	
	private void configurePopupStackPane() {
		ModalPopup.initialize(wrapperStackPane, mainContentWrapper);
	}
}