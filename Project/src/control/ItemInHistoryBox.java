package control;

import java.text.DecimalFormat;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Order;
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingItem;

/** Box that is used for displaying Products in the "purchase history" view. */
public class ItemInHistoryBox extends HBox {
	private static IMatDataHandler db = IMatDataHandler.getInstance();
	
	public static final double HEIGHT = 80;
	
	private static final String TINTED_BACKGROUND_COLOR = "#F3F1A2";
	private static final String BACKGROUND_COLOR = "white";
	private static final int PICTURE_WIDTH = 65;
	
	
	//Holds a Product and the amount
	private ShoppingItem item;

	public ItemInHistoryBox(ShoppingItem item) {
		this.item = item;
		initialize();
		hoverProperty().addListener(TINT_BACKGROUND);
	}

	/** Returns the ShoppingItem associated with this box. */
	public ShoppingItem getItem() {
		return this.item;
	}
	
	/** Returns the Product associated with this box. Note: Use getItem() if you also require amount. */
	public Product getProduct() {
		return this.item.getProduct();
	}
	
	private void initialize() {
		Product product = item.getProduct();
		
		//Load image
		ImageView picture = new ImageView(preserveRatio(product, PICTURE_WIDTH));
		HBox.setMargin(picture, new Insets(0, 21, 0, 7));
		
		getStyleClass().add("item-in-history-box");
		setFillHeight(true);
		setAlignment(Pos.CENTER_LEFT);
		
		
		//Setup name label
		Label lblName = new Label(product.getName());
		lblName.setPrefWidth(140);
		lblName.setAlignment(Pos.CENTER_LEFT);
		lblName.getStyleClass().add("item-name");
		
		//Setup quantity label
		//Remove trailing zeros
		String amount = new DecimalFormat("0.####").format(item.getAmount());
		Label lblQuantity = new Label(String.format("%s %s", amount, product.getUnitSuffix()));
		lblQuantity.setPrefWidth(65);
		lblQuantity.setAlignment(Pos.CENTER);
		HBox.setHgrow(lblQuantity, Priority.NEVER);
		lblQuantity.getStyleClass().add("item-quantity");
		
		Label lblPrice = new Label(Double.toString(item.getTotal()) + " kr");
		HBox.setHgrow(lblPrice, Priority.ALWAYS);
		lblPrice.setAlignment(Pos.CENTER_RIGHT);
		lblPrice.getStyleClass().add("item-price");
		
		//Make sure the labels get the appropriate height
		lblName.setPrefHeight(HEIGHT);
		lblQuantity.setPrefHeight(HEIGHT);
		lblPrice.setPrefHeight(HEIGHT);
		
		getChildren().addAll(picture, lblName, lblQuantity, lblPrice);
	}
	
	private final ChangeListener<Boolean> TINT_BACKGROUND = (obs, oldValue, isHovered) -> {
		if (isHovered)
			this.setStyle("-fx-background-color: " + TINTED_BACKGROUND_COLOR + ";");
		else
			this.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
	};
	
	private static Image preserveRatio(Product product, int newWidth) {
		Image rawImage = db.getFXImage(product);
		double ratio = rawImage.getWidth() / newWidth;
		return db.getFXImage(product, newWidth, rawImage.getHeight() / ratio);
	}
}
