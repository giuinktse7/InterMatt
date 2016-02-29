package util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.converter.NumberStringConverter;
import se.chalmers.ait.dat215.project.Product;

public class ShoppingCartHandler {
	private static final int MAX_QUANTITY = 999;

	private Label lblTotalCost;
	
	private ListView<ProductHBox> cart;
	private BooleanProperty emptyProperty = new SimpleBooleanProperty(true);
	
	public static ShoppingCartHandler instance = new ShoppingCartHandler();

	// Used to unfocus textField when text is set to 1 through usage of minus
	// button
	private Node dummyNode = new Label();

	private ShoppingCartHandler() {
	}
	
	public BooleanProperty emptyProperty() {
		return this.emptyProperty;
	}

	public void setCart(ListView<ProductHBox> cart) {
		this.cart = cart;
		cart.getItems().addListener((Change<? extends ProductHBox> c) -> {
			emptyProperty.set(cart.getItems().size() == 0);
		});
	}
	
	public void passLabel(Label lblTotalCost){
		this.lblTotalCost = lblTotalCost;
	}

	public boolean isEmpty() {
		return emptyProperty.get();
	}

	public static ShoppingCartHandler getInstance() {
		return instance;
	}
	
	public void updateTotalCost(){
		float cost = 0;
		ObservableList<ProductHBox> list = cart.getItems();
		for (ProductHBox box : list){
			cost += box.getQuantity() * box.getProduct().getPrice();
		}
		lblTotalCost.setText("Totalt: " +new DecimalFormat("#.##").format(cost) + ":-");
	}

	/** Adds the product to the shopping cart */
	public void addProduct(Product product) {
		ProductHBox productBox = getProductBoxFromCart(product);

		if (productBox != null)
			productBox.addQuantity(1);
		else {
			ProductHBox container = getProductContainer(product);
			cart.getItems().add(container);
		}
		updateTotalCost();
	}

	/**
	 * Constructs a Pane for the given product.
	 * 
	 * @param p
	 *            the product which values will be used.
	 * @return a Pane containing information about the product.
	 */
	private ProductHBox getProductContainer(Product p) {
		ProductHBox container;

		Label name = new Label(p.getName());
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		dummyNode = name;
		TextField txtAmount = new TextField();
		txtAmount.setPrefWidth(35);
		txtAmount.setMinHeight(35);

		Button decAmountBtn = new Button("-");
		decAmountBtn.setOnAction(changeTextValue(txtAmount, -1));
		HBox.setMargin(decAmountBtn, new Insets(0, 3, 0, 0));

		Button incAmountBtn = new Button("+");
		incAmountBtn.setOnAction(changeTextValue(txtAmount, 1));
		HBox.setMargin(incAmountBtn, new Insets(0, 0, 0, 3));

		Label unitLabel = new Label(p.getUnitSuffix());
		unitLabel.setFont(new Font(14));
		unitLabel.setMouseTransparent(true);

		StackPane quantityPane = new StackPane(txtAmount, unitLabel);
		StackPane.setAlignment(unitLabel, Pos.CENTER_RIGHT);
		quantityPane.setMinWidth(60);
		HBox quantityBox = new HBox(decAmountBtn, quantityPane, incAmountBtn);
		quantityBox.setAlignment(Pos.CENTER_LEFT);

		Label lblPrice = new Label(p.getPrice() + ":-");
		Button removeProductButton = new Button();
		removeProductButton.setStyle("-fx-background-color: transparent;");
		removeProductButton.setPrefSize(32, 32);
		Image removeProductImage = new Image("resources/remove.png", 20, 20, true, true);
		removeProductButton.setGraphic(new ImageView(removeProductImage));
		
		HBox priceWrapperBox = new HBox(lblPrice, removeProductButton);

		priceWrapperBox.setAlignment(Pos.CENTER_RIGHT);

		HBox nameWrapperBox = new HBox(name);
		nameWrapperBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(nameWrapperBox, Priority.ALWAYS);
		
		container = new ProductHBox(p, quantityBox, nameWrapperBox, priceWrapperBox);
		container.setAlignment(Pos.CENTER_LEFT);

		// only integers are allowed as amount
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			// Disallow empty text & 0 (does not account for several zeroes!)
			if (newValue.equals("") || newValue.equals("0"))
				txtAmount.setText("1");
			else {
				String text = newValue;
				if (!newValue.matches("\\d*"))
					text = text.replaceAll("[^\\d]", "");

				int intValue = Integer.parseInt(text);
				intValue = Math.min(MAX_QUANTITY, intValue);
				txtAmount.setText("" + intValue);
			}
			decAmountBtn.setDisable(newValue.equals("1"));
		});

		Bindings.bindBidirectional(txtAmount.textProperty(), container.quantityProperty(), new NumberStringConverter());
		//Post formattering fungerar ej med bindings. Skall vi visa pris avrundat till .## måste vi skriva om
		lblPrice.textProperty().bind(Bindings.concat(container.quantityProperty().multiply(Math.round(p.getPrice())), 
				":-"));
		txtAmount.setText("1");
		removeProductButton.setOnAction(event -> {cart.getItems().remove(container); updateTotalCost();});
		return container;
	}

	/**
	 * Event that changes the value of a TextField by <code>change</code>. If
	 * textField contains an int, changes the value of it. Otherwise, sets the
	 * textField's value to 1
	 */
	private EventHandler<ActionEvent> changeTextValue(TextField textField, int change) {
		return event -> {
			String text = textField.getText();
			if (isInteger(text)) {
				int value = Integer.parseInt(text);
				textField.setText(Integer.toString(value + change));
			} else
				textField.setText("1");

			if (textField.getText().equals("1"))
				dummyNode.requestFocus();
			

			updateTotalCost();
		};
	}

	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private ProductHBox getProductBoxFromCart(Product product) {
		ObservableList<ProductHBox> productsInCart = cart.getItems();

		for (Node node : productsInCart) {
			ProductHBox box = (ProductHBox) node;
			if (box.getProduct().equals(product))
				return box;
		}

		return null;
	}
}