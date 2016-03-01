package controllers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import control.ModalPopup;
import control.NavigationButton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import util.BindingGroup;
import util.ContentView;
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
	@FXML private ModalPopup purchaseHistoryPopup;
	@FXML private ModalPopup loadListPopup;
	@FXML private StoreController storePaneController;
	@FXML private CredentialsController credentialsPaneController;
	@FXML private PurchaseController purchasePaneController;
	@FXML private PurchaseHistoryController purchaseHistoryPopupController;
	@FXML private LoadListController loadListPopupController;
	@FXML private ShoppingCartController shoppingCartController;

	// Recipe
	@FXML private ModalPopup recipePopup;
	@FXML private RecipeController recipeController;
	
	@FXML private StackPane wrapperStackPane;
	@FXML private VBox mainContentWrapper;
	
	Map<Pane, ContentView> views = new HashMap<Pane, ContentView>();
	
	ViewDisplay viewDisplay;
	
	private ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();

	public void showRecipePopup(){
		recipePopup.show();
	}
	
	public void initialize(URL url, ResourceBundle bundle) {
		configurePopupStackPane();
		viewDisplay = new ViewDisplay(contentPane);
		NavigationButton.setViewDisplay(viewDisplay);
		
		addToContentView(storePane);
		addToContentView(credentialsPane);
		addToContentView(purchasePane);
		
		getView(storePane).setNext(getView(credentialsPane));
		getView(credentialsPane).setNext(getView(purchasePane));
		getView(credentialsPane).setPrevious(getView(storePane));
		getView(purchasePane).setPrevious(getView(credentialsPane));
		
		viewDisplay.show(getView(storePane));
		prevButton.setOnAction(event -> viewDisplay.previous());
		nextButton.setOnAction(event -> viewDisplay.next());
		
		navButton1.initialize(getView(storePane), event -> viewDisplay.show(getView(storePane)), navButton2);
		navButton2.initialize(getView(credentialsPane), event -> viewDisplay.show(getView(credentialsPane)), navButton3);
		navButton3.initialize(getView(purchasePane), event -> viewDisplay.show(getView(purchasePane)), navButton4);
		navButton4.initialize(new ContentView(null), event -> { }, null);
		
		purchaseHistoryButton.setOnAction(event -> { purchaseHistoryPopupController.update(); purchaseHistoryPopup.show(); });
		purchaseHistoryPopupController.setCloseAction(() -> purchaseHistoryPopup.hide());
		shoppingCartController.getShoppingListButton().setOnAction(e -> loadListPopup.show());


		purchasePaneController.mainController = this;

		//Setup navigation-button bindings
		initiateNavigationButtons();
	}
	
	private final BooleanBinding CART_NONEMPTY = Bindings.createBooleanBinding(() -> !cartHandler.emptyProperty().get(), cartHandler.emptyProperty());
	private final BooleanBinding CART_EMPTY = Bindings.createBooleanBinding(() -> cartHandler.emptyProperty().get(), cartHandler.emptyProperty());
	private void initiateNavigationButtons() {
		
		BindingGroup nav2Group = navButton2.getBindingGroup();
		nav2Group.addBinding(CART_NONEMPTY);
		nav2Group.update();
		nav2Group.setOnFalseAction(() -> {
			ContentView store = getView(storePane);
			if (!viewDisplay.getCurrentView().equals(store))
				viewDisplay.show(store);
			
			navButton2.setDisable(true);
		});
		
		BindingGroup nav3Group = navButton3.getBindingGroup();
		for (BooleanBinding binding : credentialsPaneController.getBindings())
			nav3Group.addBinding(binding);
		
		nav3Group.update();
		
		BindingGroup nav4Group = navButton4.getBindingGroup();
		nav4Group.addBinding(CART_NONEMPTY.and(CART_EMPTY));
		nav4Group.update();
	}
	
	private void configurePopupStackPane() {
		ModalPopup.initialize(wrapperStackPane, mainContentWrapper);
	}
	
	/** Auxiliary method for creating the ContentViews */
	private void addToContentView(Pane pane) {
		views.put(pane, new ContentView(pane));
	}
	
	/** Auxiliary method for getting a ContentView from the Map of views */
	private ContentView getView(Pane pane) {
		return views.get(pane);
	}
}