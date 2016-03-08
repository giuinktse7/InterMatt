package controllers;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.javafx.scene.traversal.Direction;

import control.ArrowButton;
import control.ModalPopup;
import control.NavigationButton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Order;
import se.chalmers.ait.dat215.project.ShoppingItem;
import util.BindingGroup;
import util.ContentView;
import util.InformationStorage;
import util.ShoppingCartHandler;
import util.ViewDisplay;

public class MainController implements Initializable {
	@FXML private StackPane contentPane;
	@FXML private ArrowButton prevButton;
	@FXML private ArrowButton nextButton;
//	@FXML private Button purchaseHistoryButton;
	
	//TEST
	@FXML private Button contactButton;
	@FXML private Button historyButton;
	@FXML private Button helpButton;
	
	//Labels for describing the different steps
	@FXML private Label storeLabel;
	@FXML private Label credentialsLabel;
	@FXML private Label purchaseLabel;
	@FXML private Label receiptLabel;
	
	@FXML private TextField searchTextField;
	
	//Used for the drag & drop functionality
	private final int AVG = 1374;

	private static MainController me;
	private static IMatDataHandler db = IMatDataHandler.getInstance();
	
	//Navigation buttons
	@FXML private NavigationButton btnToStore;
	@FXML private NavigationButton btnToCredentials;
	@FXML private NavigationButton btnToPurchase;
	@FXML private NavigationButton btnToReceipt;
	
	private Pane dummyPane = new Pane();
	
	//The close-to-root stuff
	@FXML private Pane shoppingCart;
	@FXML private StackPane wrapperStackPane;
	@FXML private VBox mainContentWrapper;
	
	//The different views
	@FXML private Pane storePane;
	@FXML private Pane credentialsPane;
	@FXML private Pane purchasePane;
	@FXML private Pane receiptPane;
	
	//Popups
	@FXML private ModalPopup purchaseHistoryPopup;
	@FXML private ModalPopup loadListPopup;
	@FXML private ModalPopup saveListPopup;
	@FXML private ModalPopup contactPopup;
	@FXML private ModalPopup helpPopup;
	
	//Controllers
	@FXML private StoreController storePaneController;
	@FXML private CredentialsController credentialsPaneController;
	@FXML private PurchaseController purchasePaneController;
	@FXML private PurchaseHistoryController purchaseHistoryPopupController;
	@FXML private LoadListController loadListPopupController;
	@FXML private SaveListController saveListPopupController;
	@FXML private ShoppingCartController shoppingCartController;
	
	ViewDisplay viewDisplay;
	
	private ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();
	
	public void initialize(URL url, ResourceBundle bundle) {
		prevButton.disable();
		nextButton.disable();
		
		searchTextField.textProperty().addListener(storePaneController.getSearchAction());
		me = this;
		
		//Give the popup-system required panes
		ModalPopup.initialize(wrapperStackPane, mainContentWrapper);
		
		//Start viewDisplay-system
		viewDisplay = new ViewDisplay(contentPane);
		NavigationButton.setViewDisplay(viewDisplay);
		
		//Create the views,
		ContentView storeView = new ContentView(storePane);
		ContentView credentialsView = new ContentView(credentialsPane);
		ContentView purchaseView = new ContentView(purchasePane);
		ContentView receiptView = new ContentView(receiptPane);
		ContentView dummyView = new ContentView(dummyPane);
		
		//and add the views to the viewDisplay
		viewDisplay.addView(storeView);
		viewDisplay.addView(credentialsView);
		viewDisplay.addView(purchaseView);
		viewDisplay.addView(receiptView);
		viewDisplay.addView(dummyView);
		
		//Show the store
		viewDisplay.show(storeView);
		
		//Define necessary bindings
		PURCHASE_VIEW_ACTIVE = activeViewBinding("purchasePane");
		RECEIPT_VIEW_ACTIVE = activeViewBinding("receiptPane");
		
		//Setup bindings that determine how you can move between views
		setCredentialBinds();
		setPurchaseBinds();
		setReceiptBinds();
		
		//Set next & previous relationships
		storeView.setNext(credentialsView);
		credentialsView.setNext(purchaseView);
		credentialsView.setPrevious(storeView);
		purchaseView.setPrevious(credentialsView);
		purchaseView.setNext(receiptView);
		receiptView.setNext(storeView);
		receiptView.setPrevious(null);
		
		//Set actions for the previous & next buttons
		prevButton.setDirection(Direction.LEFT);
		nextButton.setDirection(Direction.RIGHT);
		prevButton.setOnAction(event -> viewDisplay.previous());
		nextButton.setOnAction(event -> {
			//If we are on purchase, going to the next view means finishing the purchase.
			if (viewDisplay.getCurrentView().get().equals(purchaseView))
				sendOrder().handle(event);
			else
				viewDisplay.next();
		});
		
		//Initialize the navigation buttons
		btnToStore.initialize(storeView,show(storeView),btnToCredentials, storeLabel);
		btnToCredentials.initialize(credentialsView, show(credentialsView), btnToPurchase, credentialsLabel);
		btnToPurchase.initialize(purchaseView, show(purchaseView), btnToReceipt, purchaseLabel);
		btnToReceipt.initialize(receiptView, sendOrder(), null, receiptLabel);
		
		//Setup the different popup buttons
//		purchaseHistoryButton.setOnAction(event -> { purchaseHistoryPopupController.update(); purchaseHistoryPopup.show(); });
		shoppingCartController.getShoppingListButton().setOnAction(e -> loadListPopup.show());
		shoppingCartController.getSaveListButton().setOnAction(e -> saveListPopup.show());
		
		//TODO TEST
		contactButton.setOnAction(e -> contactPopup.show());
		historyButton.setOnAction(e -> purchaseHistoryPopup.show());
		helpButton.setOnAction(e -> helpPopup.show());

		//Drag & drop:  resizing of window to 'highlight' the shopping cart.
		purchaseHistoryPopup.setOnDragOver(e -> {
			purchaseHistoryPopup.setMaxWidth(1000 + (nextButton.getScene().getWidth() - AVG) / 2);
			purchaseHistoryPopup.setPrefWidth(1000 + (nextButton.getScene().getWidth() - AVG) / 2);
			purchaseHistoryPopup.getContent().setAlignment(Pos.CENTER_RIGHT);
		});
	}
	
	private final BooleanBinding CART_NONEMPTY = Bindings.createBooleanBinding(() -> !cartHandler.emptyProperty().get(), cartHandler.emptyProperty());
	private BooleanBinding PURCHASE_VIEW_ACTIVE;
	private BooleanBinding RECEIPT_VIEW_ACTIVE;
	
	/** Creates a binding that is true when the active view is the view with "css-id" <code>ID</code> */
	private BooleanBinding activeViewBinding(String ID) {
		SimpleBooleanProperty isActiveView = new SimpleBooleanProperty(false);
		viewDisplay.getCurrentView().addListener((obs, oldValue, newValue) ->  isActiveView.set(viewDisplay.getCurrentView().getID().equals(ID)));
		BooleanBinding binding = Bindings.createBooleanBinding(() -> isActiveView.get(), isActiveView);
		
		return binding;
	}
	
	private void setCredentialBinds() {
		ContentView view = viewDisplay.getView(credentialsPane);
		
		BindingGroup group = btnToCredentials.getBindingGroup();
		group.addBinding(CART_NONEMPTY.and(not(RECEIPT_VIEW_ACTIVE)));
		group.getState().addListener((obs, o, n) -> {
			ContentView storeView = viewDisplay.getView(storePane);
			ContentView receiptView = viewDisplay.getView(storePane);
			ContentView currentView = viewDisplay.getCurrentView().getValue();
			
			if (not(CART_NONEMPTY).get() && !currentView.equals(storeView) && !currentView.equals(receiptView))
				viewDisplay.show(storeView);
		
		btnToCredentials.setDisable(true);
		});
		
		view.getBindingGroup().setAll(group.getBinds());
	}
	
	/** Event that shows the view <code>view</code> */
	private EventHandler<ActionEvent> show(ContentView view) {
		return e -> viewDisplay.show(view);
	}
	
	/** Is called when a purchase has completed. */
	private EventHandler<ActionEvent> sendOrder() {
		return e -> {
			viewDisplay.show(viewDisplay.getView(receiptPane));
			
			ShoppingCartHandler handler = ShoppingCartHandler.getInstance();
			List<ShoppingItem> items = handler.getCartItems();
			
			items.forEach(item -> db.getShoppingCart().addItem(item));


			db.placeOrder();
			handler.clearCart();
			db.shutDown();
			
			//---------------//
			List<Order> orders = IMatDataHandler.getInstance().getOrders();
			
			Collections.sort(orders, (o1, o2) -> {
				return (int) (o2.getDate().getTime() - o1.getDate().getTime());
			});
			
			float totalPrice = 0;
			for (ShoppingItem item : orders.get(0).getItems()){
				totalPrice += item.getTotal();
			}

			RecipeController.getInstance().setTitleText(InformationStorage.getFirstName());
			RecipeController.getInstance().setPriceText(totalPrice);
			RecipeController.getInstance().setDeliveryTimeText(InformationStorage.getDelivery());
			RecipeController.getInstance().setPaymentText(InformationStorage.getPaymentType());
		};
	}
	
	private void setPurchaseBinds() {
		ContentView view = viewDisplay.getView(purchasePane);
		BindingGroup group = btnToPurchase.getBindingGroup();
		group.addBindings(credentialsPaneController.getBindings().and(not(RECEIPT_VIEW_ACTIVE)));
		
		view.getBindingGroup().setAll(group.getBinds());
	}

	private void setReceiptBinds() {
		ContentView view = viewDisplay.getView(receiptPane);
		BindingGroup group = btnToReceipt.getBindingGroup();
		group.addBinding(purchasePaneController.getBindings().and(PURCHASE_VIEW_ACTIVE));
		
		view.getBindingGroup().setAll(group.getBinds());
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
	
	public ArrowButton getArrowButton(Direction direction) {
		if (direction == Direction.LEFT)
			return prevButton;
		else
			return nextButton;
	}
	
	public BooleanBinding not(BooleanBinding binding) {
		return binding.not();
	}
}