package control;

import java.text.DecimalFormat;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingItem;

/** Box that is used for displaying Products in the "purchase history" view. */
public class ItemInHistoryBox extends AnchorPane {
	private static IMatDataHandler db = IMatDataHandler.getInstance();

	public static final double HEIGHT = 80;
	
	private static final String TINTED_BACKGROUND_COLOR = "#f4f8f1";
	private static final String BACKGROUND_COLOR = "white";
	private static final int PICTURE_WIDTH = 65;

	// Holds a Product and the amount
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

	/**
	 * Returns the Product associated with this box. Note: Use getItem() if you
	 * also require amount.
	 */
	public Product getProduct() {
		return this.item.getProduct();
	}

	private void initialize() {
		Product product = item.getProduct();

		// Load image
		ImageView picture = new ImageView(preserveRatio(product, PICTURE_WIDTH));
		AnchorPane.setLeftAnchor(picture, 14d);

		getStyleClass().add("item-in-history-box");

		// Setup name label
		Label lblName = new Label(product.getName());
		AnchorPane.setLeftAnchor(lblName, 66d);
		lblName.setPrefWidth(190);
		lblName.setAlignment(Pos.CENTER_LEFT);
		lblName.getStyleClass().add("item-name");
		lblName.setOnMouseClicked(event -> {
			// TODO
		});

		// Setup quantity label
		// Remove trailing zeros
		String amount = formatZeros(item.getAmount());
		Label lblQuantity = new Label(String.format("%s %s", amount, product.getUnitSuffix()));
		AnchorPane.setLeftAnchor(lblQuantity, 257d);
		lblQuantity.setPrefWidth(60);
		lblQuantity.setAlignment(Pos.CENTER_LEFT);
		lblQuantity.getStyleClass().add("item-quantity");

		// Setup price label
		Label lblPrice = new Label(formatZeros(item.getTotal()) + ":-");
		lblPrice.setContentDisplay(ContentDisplay.RIGHT);
		AnchorPane.setRightAnchor(lblPrice, 14d);
		lblPrice.setPrefWidth(118);
		lblPrice.setAlignment(Pos.CENTER_RIGHT);
		lblPrice.getStyleClass().add("item-price");

		Separator separator = new Separator(Orientation.HORIZONTAL);
		AnchorPane.setLeftAnchor(separator, 14d);
		AnchorPane.setRightAnchor(separator, 14d);
		AnchorPane.setBottomAnchor(separator, 0d);

		// Make sure the labels get the appropriate height
		AnchorPane.setTopAnchor(lblName, 0d);
		AnchorPane.setTopAnchor(lblQuantity, 0d);
		AnchorPane.setTopAnchor(lblPrice, 0d);
		AnchorPane.setBottomAnchor(lblName, 0d);
		AnchorPane.setBottomAnchor(lblQuantity, 0d);
		AnchorPane.setBottomAnchor(lblPrice, 0d);

		/*
		 * lblName.setId("red"); lblQuantity.setId("red");
		 * lblPrice.setId("red");
		 */

		getChildren().addAll(picture, lblName, lblQuantity, lblPrice, separator);
		
		setOnDragDetected((event) -> {
			Dragboard db = this.startDragAndDrop(TransferMode.ANY);
			
			ClipboardContent content = new ClipboardContent();
			content.putString(String.format("%d %s", (int) product.getProductId(), amount));
			db.setContent(content);
			
			event.consume();
		});
		
		setOnDragDone(event -> {
			event.consume();
		});
		
		
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
	
	private String formatZeros(double value) {
		String text = new DecimalFormat("0.####").format(value);
		//Get rid of trailing zeros.
		if (text.contains(".")) {
			int i = text.length() - 1;
			while (text.charAt(i) == 0) {
				--i;
			}
			
			text = text.substring(0, i).replace('.',' ').trim();
		}
		
		return text;
	}
}
