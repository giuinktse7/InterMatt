package control;

import java.math.RoundingMode;
import java.text.NumberFormat;

import javafx.beans.binding.Bindings;
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
import javafx.scene.text.FontWeight;
import javafx.util.converter.NumberStringConverter;
import se.chalmers.ait.dat215.project.Product;
import util.ShoppingCartHandler;


public class ProductHBox extends HBox {
	private static ShoppingCartHandler cartHandler = ShoppingCartHandler.getInstance();
	
	private Product product;
	private DoubleProperty quantityProperty = new SimpleDoubleProperty(1);
	
	public ProductHBox(Product product, Node... nodes) {
		super(nodes);
		this.product = product;
		
		initialize();
	}
	
	public ProductHBox(Product product) {
		this.product = product;
		initialize();
	}
	
	public DoubleProperty quantityProperty() {
		return this.quantityProperty;
	}
	
	public void setQuantity(int value) {
		quantityProperty.set(value);
	}
	
	public void addQuantity(int value) {
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
		Label name = new Label(product.getName());
		name.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		
		AttributeTextField txtAmount = new AttributeTextField(product, cartHandler.MAX_QUANTITY);
		
		txtAmount.setPrefWidth(65);
		txtAmount.setMinHeight(35);

		Button decAmountBtn = new Button();
		decAmountBtn.setOnAction(e -> { quantityProperty().set(this.quantityProperty().get() - 1); name.requestFocus(); });
		HBox.setMargin(decAmountBtn, new Insets(0, 3, 0, 0));
		decAmountBtn.setStyle("-fx-background-color: transparent;");
		decAmountBtn.setPrefSize(15, 15);
		Image decAmountBtnImage = new Image("resources/minus.png", 15, 15, true, true);
		decAmountBtn.setGraphic(new ImageView(decAmountBtnImage));

		Button incAmountBtn = new Button();
		HBox.setMargin(incAmountBtn, new Insets(0, 0, 0, 3));
		incAmountBtn.setOnAction(e -> this.quantityProperty().set(this.quantityProperty().get() + 1));
		incAmountBtn.setStyle("-fx-background-color: transparent;");
		incAmountBtn.setPrefSize(15, 15);
		Image incAmountBtnImage = new Image("resources/plus.png", 15, 15, true, true);
		incAmountBtn.setGraphic(new ImageView(incAmountBtnImage));
		
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
		Image removeProductImage = new Image("resources/remove.png", 15, 15, true, true);
		Image removeProductImageRed = new Image("resources/remove_red.png", 15, 15, true, true);
		removeProductButton.setGraphic(new ImageView(removeProductImage));
		removeProductButton.hoverProperty().addListener((obs, o, n) -> removeProductButton.setGraphic(new ImageView(n ? removeProductImageRed : removeProductImage)));

		HBox priceWrapperBox = new HBox(lblPrice, removeProductButton);
		priceWrapperBox.setAlignment(Pos.CENTER_RIGHT);

		HBox nameWrapperBox = new HBox(name);
		nameWrapperBox.setAlignment(Pos.CENTER);
		HBox.setHgrow(priceWrapperBox, Priority.ALWAYS);

		this.getChildren().setAll(quantityBox, nameWrapperBox, priceWrapperBox);
		this.setAlignment(Pos.CENTER_LEFT);

		txtAmount.textProperty().addListener((obs, oldValue, newValue) -> {
			decAmountBtn.setDisable(newValue.equals("1"));
			
			if (isDouble(newValue) && !oldValue.equals("")) {
				double oldQuantity = Double.parseDouble(oldValue);
				double newQuantity = Double.parseDouble(newValue);
				double change = (newQuantity - oldQuantity) * product.getPrice();
				cartHandler.addToTotal(change);
			}
		});
		
		lblPrice.textProperty().bind(Bindings.concat(quantityProperty().multiply(product.getPrice()), ":-"));
		txtAmount.setText("1");
		
		//Bind the quantity textField and the quantity of the associated shoppingItem
		NumberFormat format = NumberFormat.getIntegerInstance();
		format.setParseIntegerOnly(true);
		format.setRoundingMode(RoundingMode.DOWN);
		Bindings.bindBidirectional(txtAmount.textProperty(), quantityProperty(), new NumberStringConverter(format));
		removeProductButton.setOnAction(event -> {
			cartHandler.getItems().remove(this);
		});
	}
	
	/** Action to perform when the 'X' icon is pressed. */
	public void setActionOnRemoveClick(EventHandler<ActionEvent> e) {
	}
	
	
	/** Returns true if they represent the same object */
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!obj.getClass().equals(this.getClass())) return false;
			
		ProductHBox that = (ProductHBox) obj;
			
		return this.product.getProductId() == (that.product.getProductId());
	}
	
	private static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

}
