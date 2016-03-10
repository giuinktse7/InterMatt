package control;

import java.text.DecimalFormat;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import se.chalmers.ait.dat215.project.Product;
import util.ShoppingCartHandler;

public class CartItem extends HBox {
	private static ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();

	private Product product;
	private DoubleProperty quantityProperty = new SimpleDoubleProperty(1);

	public CartItem(Product product, Node... nodes) {
		super(nodes);
		this.product = product;

		initialize();
	}

	public CartItem(Product product) {
		this.product = product;
		initialize();
	}

	public DoubleProperty quantityProperty() {
		return this.quantityProperty;
	}

	public void setQuantity(double value) {
		quantityProperty.set(value);
	}

	public void addQuantity(double value) {
		quantityProperty.set(quantityProperty.get() + value);
	}

	public double getQuantity() {
		return quantityProperty.get();
	}

	public Product getProduct() {
		return this.product;
	}

	/**
	 * Auxiliary method for <code>setAmount(int amount)</code>;
	 * 
	 * @see <code>setAmount(int amount)</code>
	 * @param amount
	 *            product quantity.
	 * @post Sets the product amount to the specified amount if it could be
	 *       converted to an Integer. Otherwise, the method does nothing.
	 */
	public void setAmount(String amount) {
		try {
			setQuantity(Integer.parseInt(amount));
		} catch (NumberFormatException nfe) {
		}
	}

	private void initialize() {
		this.setFocusTraversable(false);
		this.getStyleClass().add("cart-item");
		Label name = new Label(product.getName());
		name.getStyleClass().add("name-label");
		
		AttributeTextField txtAmount = new AttributeTextField(product, cartHandler.MAX_QUANTITY);
		
		txtAmount.setPrefWidth(65);
		txtAmount.setMinHeight(35);
		
		
		Button decAmountBtn = new Button();
		decAmountBtn.setOnAction(e -> { quantityProperty().set(Math.max(1, this.quantityProperty().get() - 1)); name.requestFocus(); });
		HBox.setMargin(decAmountBtn, new Insets(0, 3, 0, 0));
		decAmountBtn.setStyle("-fx-background-color: transparent;");
		decAmountBtn.setPrefSize(24, 24);
		Image decAmountBtnImage = new Image("resources/minus.png", 24, 24, true, true);
		decAmountBtn.setGraphic(new ImageView(decAmountBtnImage));
		
		Image decAmountBtnDisabled = new Image("resources/minus_disabled.png", 24, 24, true, true);
		decAmountBtn.setGraphic(new ImageView(decAmountBtnImage));
		decAmountBtn.hoverProperty().addListener((obs, o, n) -> decAmountBtn.setGraphic(new ImageView(n ? decAmountBtnDisabled : decAmountBtnImage)));

		Button incAmountBtn = new Button();
		HBox.setMargin(incAmountBtn, new Insets(0, 0, 0, 3));
		incAmountBtn.setOnAction(e -> this.quantityProperty().set(this.quantityProperty().get() + 1));
		incAmountBtn.setStyle("-fx-background-color: transparent;");
		incAmountBtn.setPrefSize(24, 24);
		Image incAmountBtnImage = new Image("resources/plus.png", 24, 24, true, true);
		incAmountBtn.setGraphic(new ImageView(incAmountBtnImage));
		
		Image incAmountBtnDisabled = new Image("resources/plus_disabled.png", 24, 24, true, true);
		incAmountBtn.setGraphic(new ImageView(incAmountBtnImage));
		incAmountBtn.hoverProperty().addListener((obs, o, n) -> incAmountBtn.setGraphic(new ImageView(n ? incAmountBtnDisabled : incAmountBtnImage)));
		
		Label unitLabel = new Label(product.getUnitSuffix());
		unitLabel.setFont(new Font(14));
		unitLabel.setMouseTransparent(true);

		StackPane quantityPane = new StackPane(txtAmount, unitLabel);
		StackPane.setAlignment(unitLabel, Pos.CENTER_RIGHT);
		unitLabel.setPadding(new Insets(0, 2, 0, 0));
		quantityPane.setMinWidth(65);
		HBox quantityBox = new HBox(decAmountBtn, quantityPane, incAmountBtn);
		quantityBox.setAlignment(Pos.CENTER_LEFT);

		Label lblPrice = new Label(product.getPrice() + ":-");
		lblPrice.setFont(new Font(14));
		
		Button removeProductButton = new Button();
		removeProductButton.setStyle("-fx-background-color: transparent;");
		removeProductButton.setPrefSize(15, 15);
		Image removeProductImage = new Image("resources/remove.png", 24, 24, true, true);
		Image removeProductImageRed = new Image("resources/remove_disabled.png", 24, 24, true, true);
		removeProductButton.setGraphic(new ImageView(removeProductImage));
		removeProductButton.hoverProperty().addListener((obs, o, n) -> removeProductButton.setGraphic(new ImageView(n ? removeProductImageRed : removeProductImage)));

		HBox priceWrapperBox = new HBox(lblPrice, removeProductButton);
		priceWrapperBox.setAlignment(Pos.CENTER_RIGHT);

		HBox nameWrapperBox = new HBox(name);
		nameWrapperBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(priceWrapperBox, Priority.ALWAYS);

		this.getChildren().setAll(quantityBox, nameWrapperBox, priceWrapperBox);
		this.setAlignment(Pos.CENTER_LEFT);
		
		quantityProperty.addListener((obs, o, n) -> {
			DecimalFormat f = new DecimalFormat("0.##");
			String k = f.format(n.doubleValue());
			txtAmount.setText(k);
			});
		
		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			decAmountBtn.setDisable(!isDouble(newValue) || Double.parseDouble(newValue) <= 1 );

			//If empty, do nothing
			if (newValue.isEmpty())
				return;
			
			if (newValue.charAt(0) == '.')
				newValue = "0" + newValue;
			
			if (isDouble(newValue)) {
				double oldQuantity = quantityProperty.get();
				double newQuantity = newValue.isEmpty() ? 0 : Double.parseDouble(newValue);
				double change = (newQuantity - oldQuantity) * product.getPrice();
				String fintPris = String.format("%.2f:-", newQuantity * product.getPrice());

				lblPrice.setText(fintPris);
				cartHandler.addToTotal(change);
				quantityProperty.set(Double.parseDouble(newValue));
			}

		});
		
		txtAmount.focusedProperty().addListener((obs, oldValue, newValue) -> {
			String text = txtAmount.getText();
			
			if (!text.isEmpty()) {
				if (text.length() == 1 && text.charAt(0) == '0' )
					text = "1";
			
			if (text.charAt(0) == '.')
				if (text.length() != 1)
					text = "0" + text;
				else
					text = "1";
			} else
				text = "1";
			
			if (text.charAt(text.length() - 1) == '.')
				text = text.substring(0, text.length() - 1);
			
			if (isDouble(text) && Double.parseDouble(text) == 0)
				text = "1";
			
			txtAmount.setText(text);
		});
		
		txtAmount.setText("1");
		
		removeProductButton.setOnAction(event -> {
			cartHandler.getItems().remove(this);
		});
	}

	/** Action to perform when the 'X' icon is pressed. */
	public void setActionOnRemoveClick(EventHandler<ActionEvent> e) {
	}

	/** Returns true if they represent the same object */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (!obj.getClass().equals(this.getClass()))
			return false;

		CartItem that = (CartItem) obj;

		return this.product.getProductId() == (that.product.getProductId());
	}

	public static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return value.charAt(value.length() - 1) != '.' && value.charAt(0) != '.';
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
