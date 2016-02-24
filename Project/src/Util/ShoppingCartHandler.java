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
import javafx.scene.layout.FlowPane;
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
	
	//Used to unfocus textField when text is set to 1 through usage of minus button
	private Node dummyNode = new Label();

	private ShoppingCartHandler() {
	}

	public void setCart(ListView<Node> cart) {
		this.cart = cart;
		
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
	/*private Pane getProductContainer(Product p) {
		HBox leftBox, middleBox, rightBox;

		Label name = new Label(p.getName());
		middleBox = new HBox(name);
		HBox.setHgrow(middleBox, Priority.ALWAYS);

		dummyNode = name;
		TextField txtAmount = new TextField();
		
		Label unitLabel = new Label(p.getUnitSuffix());
		unitLabel.setMouseTransparent(true);
		
		txtAmount.prefWidthProperty().bind(unitLabel.widthProperty().add(35));
		
		StackPane quantityPane = new StackPane(txtAmount, unitLabel);
		StackPane.setAlignment(unitLabel, Pos.CENTER_RIGHT);
		// only integers are allowed as amount
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			//Disallow empty text & 0 (does not account for several zeroes!)
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
	        });

		Button decAmountBtn = new Button("-");
		decAmountBtn.setOnAction(changeTextValue(txtAmount, -1));
		HBox.setMargin(decAmountBtn, new Insets(0, 3, 0, 0));
		
		Button incAmountBtn = new Button("+");
		incAmountBtn.setOnAction(changeTextValue(txtAmount, 1));
		HBox.setMargin(incAmountBtn, new Insets(0, 0, 0, 3));
		
		leftBox = new HBox(decAmountBtn, quantityPane, incAmountBtn);
		HBox.setHgrow(quantityPane, Priority.ALWAYS);

		Label lblPrice = new Label(p.getPrice() + ":-");
		ImageView removeProductIcon = new ImageView();
		rightBox = new HBox(lblPrice, removeProductIcon);
		rightBox.setAlignment(Pos.CENTER_RIGHT);

		ProductContainer container = new ProductContainer(middleBox, null, rightBox, null, leftBox);
		
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			container.setAmount(newValue);
			
			decAmountBtn.setDisable(newValue.equals("1"));
				
			String price = new DecimalFormat("0.#").format(p.getPrice() * Integer.parseInt(newValue));
			lblPrice.setText((price + ":-"));
		});
		
		HBox.setHgrow(rightBox, Priority.ALWAYS);
		
		txtAmount.setText("1");
		
		txtAmount.requestFocus();
		unitLabel.requestFocus();

		return container;
	}*/
	
	private Pane getProductContainer(Product p) {
		ProductHBox container;
		
		Label name = new Label(p.getName());

		dummyNode = name;
		TextField txtAmount = new TextField();
		txtAmount.setPrefWidth(45);
		txtAmount.setMinHeight(35);
		
		Button decAmountBtn = new Button("-");
		decAmountBtn.setOnAction(changeTextValue(txtAmount, -1));
		HBox.setMargin(decAmountBtn, new Insets(0, 3, 0, 0));
		
		Button incAmountBtn = new Button("+");
		incAmountBtn.setOnAction(changeTextValue(txtAmount, 1));
		HBox.setMargin(incAmountBtn, new Insets(0, 0, 0, 3));
		
		Label unitLabel = new Label(p.getUnitSuffix());
		unitLabel.setMouseTransparent(true);
		
		StackPane quantityPane = new StackPane(txtAmount, unitLabel);
		StackPane.setAlignment(unitLabel, Pos.CENTER_RIGHT);
		HBox quantityBox = new HBox(decAmountBtn, quantityPane, incAmountBtn);
		quantityBox.setAlignment(Pos.CENTER_LEFT);

		
		Label lblPrice = new Label(p.getPrice() + ":-");
		lblPrice.setPrefWidth(45);
		
		HBox priceWrapperBox = new HBox(lblPrice);
		priceWrapperBox.setAlignment(Pos.CENTER_RIGHT);
		
		container = new ProductHBox(quantityBox, name, priceWrapperBox);
		container.setSpacing(7);
		container.setAlignment(Pos.CENTER_LEFT);
		
		// only integers are allowed as amount
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			//Disallow empty text & 0 (does not account for several zeroes!)
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
			
			container.setAmount(newValue);
				
			decAmountBtn.setDisable(newValue.equals("1"));
					
			String price = new DecimalFormat("0.#").format(p.getPrice() * Integer.parseInt(newValue));
			lblPrice.setText((price + ":-"));
	        });
		
		txtAmount.setText("1");

		return container;
	}

	/** Event that changes the value of a TextField by <code>change</code>.
	 * If textField contains an int, changes the value of it. Otherwise, sets the textField's value to 1 */
	private EventHandler<ActionEvent> changeTextValue(TextField textField, int change) {
		return event -> {
			String text = textField.getText();
			if (isInteger(text)) {
				int value = Integer.parseInt(text);
				textField.setText(Integer.toString(value + change));
			}
			else
				textField.setText("1");
			
			if (textField.getText().equals("1"))
				dummyNode.requestFocus();
		};
	}
	
	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) { return false; }
	}
}