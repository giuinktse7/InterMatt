package Util;

import java.text.DecimalFormat;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingCart;
import se.chalmers.ait.dat215.project.ShoppingItem;

public class ShoppingCartHandler {

	private static final int MAX_QUANTITY = 999;
	
	private ListView<Node> cart;
	public static ShoppingCartHandler instance = new ShoppingCartHandler();
	private static ShoppingCart dbCart  = IMatDataHandler.getInstance().getShoppingCart();

	private ShoppingCartHandler() {
	}

	public void setCart(ListView<Node> cart) {
		this.cart = cart;
	}
	
	public boolean isEmpty() {
		return cart.getItems().size() == 0;
	}

	public static ShoppingCartHandler getInstance() {
		return instance;
	}

	/** Adds the product to the shopping cart */
	public void addProduct(Product product) {
		Pane container = getProductContainer(product);
		cart.getItems().add(container);
	}

	/**
	 * Constructs a Pane for the given product.
	 * @param p the product which values will be used.
	 * @return a Pane containing information about the product.
	 */
	private Pane getProductContainer(Product p) {
		HBox leftBox, middleBox, rightBox;

		Label name = new Label(p.getName());
		leftBox = new HBox(name);
		leftBox.setAlignment(Pos.CENTER_LEFT);

		TextField txtAmount = new TextField();
		txtAmount.setPrefWidth(100);
		
		Label unitLabel = new Label(p.getUnitSuffix());
		unitLabel.setMouseTransparent(true);
		
		StackPane quantityPane = new StackPane(txtAmount, unitLabel);
		quantityPane.setAlignment(Pos.CENTER_RIGHT);
		
		// only integers are allowed as amount
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
				String text = newValue;
	            if (!newValue.matches("\\d*"))
	            	text = text.replaceAll("[^\\d]", "");
	            
	            int intValue = Integer.parseInt(text);
	            intValue = Math.min(MAX_QUANTITY, intValue);
	            txtAmount.setText("" + intValue);
	        });

		Button decAmountBtn = new Button("-");
		decAmountBtn.setOnAction(changeTextValue(txtAmount, -1));
		HBox.setMargin(decAmountBtn, new Insets(0, 3, 0, 0));
		
		Button incAmountBtn = new Button("+");
		incAmountBtn.setOnAction(changeTextValue(txtAmount, 1));
		HBox.setMargin(incAmountBtn, new Insets(0, 0, 0, 3));
		
		middleBox = new HBox(decAmountBtn, quantityPane, incAmountBtn);
		middleBox.setAlignment(Pos.CENTER);

		Label lblPrice = new Label(p.getPrice() + ":-");
		ImageView removeProductIcon = new ImageView();
		rightBox = new HBox(lblPrice, removeProductIcon);
		rightBox.setAlignment(Pos.CENTER_RIGHT);

		ProductContainer container = new ProductContainer(middleBox, null, rightBox, null, leftBox);
		
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			container.setAmount(newValue);
			
			if (newValue.equals("1"))
				name.requestFocus();
			
			decAmountBtn.setDisable(newValue.equals("1"));
				
			String price = new DecimalFormat("0.#").format(p.getPrice() * Integer.parseInt(newValue));
			lblPrice.setText((price + ":-"));
		});
		
		HBox.setHgrow(rightBox, Priority.ALWAYS);
		
		txtAmount.setText("1");

		return container;
	}

	/** Event that changes the value of a TextField by <code>change</code>.
	 * If textField contains an int, changes the value of it. Otherwise, sets the textField's value to 1 */
	private static EventHandler<ActionEvent> changeTextValue(TextField textField, int change) {
		return event -> {
			String text = textField.getText();
			if (isInteger(text)) {
				int value = Integer.parseInt(text);
				textField.setText(Integer.toString(value + change));
			}
			else
				textField.setText("1");
		};
	}
	
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) { return false; }
	}
}