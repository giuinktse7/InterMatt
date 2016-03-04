package controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.javafx.scene.traversal.Direction;

import control.ArrowButton;
import control.ModalPopup;
import control.NavigationButton;
import interfaces.Action;
import interfaces.MultiAction;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.ShoppingItem;
import util.BindingGroup;
import util.ContentView;
import util.ShoppingCartHandler;
import util.ViewDisplay;

public class MainController implements Initializable {
	@FXML private StackPane contentPane;
	@FXML private ArrowButton prevButton;
	@FXML private ArrowButton nextButton;
	@FXML private Button purchaseHistoryButton;
	
	private final int AVG = 1374;

	private static MainController me;
	private static IMatDataHandler db = IMatDataHandler.getInstance();
	
	//Navigation buttons
	@FXML private NavigationButton btnToStore;
	@FXML private NavigationButton btnToCredentials;
	@FXML private NavigationButton btnToPurchase;
	@FXML private NavigationButton navButton4;
	
	private Pane dummyPane = new Pane();
	@FXML private Pane purchasePane;
	@FXML private Pane credentialsPane;
	@FXML private Pane storePane;
	@FXML private Pane shoppingCart;
	@FXML private ModalPopup purchaseHistoryPopup;
	@FXML private ModalPopup loadListPopup;
	@FXML private ModalPopup saveListPopup;
	@FXML private StoreController storePaneController;
	@FXML private CredentialsController credentialsPaneController;
	@FXML private PurchaseController purchasePaneController;
	@FXML private PurchaseHistoryController purchaseHistoryPopupController;
	@FXML private LoadListController loadListPopupController;
	@FXML private SaveListController saveListPopupController;
	@FXML private ShoppingCartController shoppingCartController;
	@FXML private SplitPane splitPane;

	// Recipe
	@FXML private Pane recipePane;

	@FXML private StackPane wrapperStackPane;
	@FXML private VBox mainContentWrapper;
	
	ViewDisplay viewDisplay;
	
	private ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();
	
	public void initialize(URL url, ResourceBundle bundle) {
		leftButton = this.prevButton;
		nextButton.setDirection(Direction.RIGHT);
		
		nextButton.setDisable(true);
		prevButton.setDisable(true);
		
		me = this;
		configurePopupStackPane();
		viewDisplay = new ViewDisplay(contentPane);
		NavigationButton.setViewDisplay(viewDisplay);
		
		ContentView storeView = new ContentView(storePane);
		ContentView credentialsView = new ContentView(credentialsPane);
		ContentView purchaseView = new ContentView(purchasePane);
		ContentView recipeView = new ContentView(recipePane);
		ContentView dummyView = new ContentView(dummyPane);
		
		viewDisplay.addView(storeView);
		viewDisplay.addView(credentialsView);
		viewDisplay.addView(purchaseView);
		viewDisplay.addView(recipeView);
		viewDisplay.addView(dummyView);
		
		//Set next & previous relationships
		storeView.setNext(credentialsView);
		credentialsView.setNext(purchaseView);
		credentialsView.setPrevious(storeView);
		purchaseView.setPrevious(credentialsView);
		purchaseView.setNext(recipeView);

		
		//Show the store
		viewDisplay.show(storeView);
		
		//Set actions for the previous & next buttons
		prevButton.setDirection(Direction.LEFT);
		prevButton.setOnAction(event -> viewDisplay.previous());
		nextButton.setOnAction(event -> { viewDisplay.next(); });
		
		//Initialize the navigation buttons
		btnToStore.initialize(storeView, event -> viewDisplay.show(storeView), btnToCredentials);
		btnToCredentials.initialize(credentialsView, event -> viewDisplay.show(credentialsView), btnToPurchase);
		btnToPurchase.initialize(purchaseView, event -> viewDisplay.show(purchaseView), navButton4);
		navButton4.initialize(new ContentView(null), event -> { }, null);
		
		//Setup the different popup buttons
		purchaseHistoryButton.setOnAction(event -> { purchaseHistoryPopupController.update(); purchaseHistoryPopup.show(); });
		purchaseHistoryPopupController.setCloseAction(() -> purchaseHistoryPopup.hide());
		shoppingCartController.getShoppingListButton().setOnAction(e -> loadListPopup.show());
		shoppingCartController.getSaveListButton().setOnAction(e -> saveListPopup.show());



		//Setup validations
		setupStoreValidation();
		setupCredentialsViewValidation();
		setupPurchaseViewValidation();
		setupNavButton4Valiation();

		purchaseHistoryPopup.setOnDragOver(e -> {
			purchaseHistoryPopup.setMaxWidth(1000 + (nextButton.getScene().getWidth() - AVG) / 2);
			purchaseHistoryPopup.setPrefWidth(1000 + (nextButton.getScene().getWidth() - AVG) / 2);
			purchaseHistoryPopup.getContent().setAlignment(Pos.CENTER_RIGHT);
	});
	}
	
	private final BooleanBinding CART_NONEMPTY = Bindings.createBooleanBinding(() -> !cartHandler.emptyProperty().get(), cartHandler.emptyProperty());
	private final BooleanBinding CART_EMPTY = Bindings.createBooleanBinding(() -> cartHandler.emptyProperty().get(), cartHandler.emptyProperty());
	
	private void setupStoreValidation() {
		ContentView view = viewDisplay.getView(storePane);
		view.getBindingGroup().setName("storeViewGroup");
		
		view.getBindingGroup().setOnTrueAction(disableButton(prevButton, storePane));
	}
	
	private void setupCredentialsViewValidation() {
		ContentView view = viewDisplay.getView(credentialsPane);
		
		BindingGroup group = btnToCredentials.getBindingGroup();
		group.addBinding(CART_NONEMPTY);
		group.setOnFalseAction(() -> {
			ContentView store = viewDisplay.getView(storePane);
			if (!viewDisplay.getCurrentView().equals(store))
				viewDisplay.show(store);
			
			btnToCredentials.setDisable(true);
		});
		view.getBindingGroup().setName("credentialsViewGroup");
		view.getBindingGroup().addBinding(CART_NONEMPTY);
		
		//If we are on storePane and can't go to credentialsPane, disable nextButton
		view.getBindingGroup().setOnTrueAction(new MultiAction(
				enableButton(nextButton, storePane),
				enableButton(prevButton, purchasePane),
				enableButton(prevButton, credentialsPane)));
		
		view.getBindingGroup().setOnFalseAction(disableButton(nextButton, storePane));
	}
	
	private void setupPurchaseViewValidation() {
		ContentView view = viewDisplay.getView(purchasePane);
		view.getBindingGroup().setName("purchaseViewGroup");
		BindingGroup group = btnToPurchase.getBindingGroup();
		group.addBindings(credentialsPaneController.getBindings());
		
		view.getBindingGroup().setAll(group.getBinds());
		
		//If we are on credentialsPane and can't go to purchasePane, disable nextButton
		view.getBindingGroup().setOnTrueAction(enableButton(nextButton, credentialsPane));
		view.getBindingGroup().setOnFalseAction(disableButton(nextButton, credentialsPane));
	}
	
	// TODO : Just makes it impossible to reach as of now.
	private void setupNavButton4Valiation() {
		ContentView view = viewDisplay.getView(dummyPane);
		BindingGroup group = navButton4.getBindingGroup();
		group.addBinding(CART_NONEMPTY.and(CART_EMPTY));
		
		view.getBindingGroup().setOnFalseAction(disableButton(nextButton, purchasePane));
	}
	
	private void configurePopupStackPane() {
		ModalPopup.initialize(wrapperStackPane, mainContentWrapper);
	}
	
	public void finishPurchase() {
		ShoppingCartHandler handler = ShoppingCartHandler.getInstance();
		List<ShoppingItem> items = handler.getCartItems();
		
		items.forEach(item -> db.getShoppingCart().addItem(item));
		
		handler.clearCart();

		db.placeOrder();
		viewDisplay.show(storePane);
		db.shutDown();
	}

	public void restoreUserData(){
		credentialsPaneController.restore_user_data();
	}

	public void saveUserData(){
		credentialsPaneController.save_user_data();
	}
	
	public static MainController get() {
		return me;
	}
	
	private boolean isCurrentView(Pane pane) {
		return viewDisplay.getCurrentView().getValue().equals(viewDisplay.getView(pane));
	}
	
	/** Disables an ArrowButton if <code>pane</code> is on the currently selected view. */
	public Action disableButton(ArrowButton button, Pane pane) {
		return () -> {
			
			if (isCurrentView(pane))
				button.disable(); 
			};
	}

	/** Enables an ArrowButton if <code>pane</code> is on the currently selected view. */
	public Action enableButton(ArrowButton button, Pane pane) {
		return () -> {
			if (isCurrentView(pane))
				button.enable(); 
			};
	}
	
	public static ArrowButton leftButton;
}