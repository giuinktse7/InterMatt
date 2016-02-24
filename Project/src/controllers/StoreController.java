package controllers;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import Util.ProductType;
import Util.ShoppingCartHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.*;

public class StoreController implements Initializable {

	private static final int pictureWidth = 180;
	private static final int pictureHeight = 160;
	
	
	@FXML private Button gotoShoppingListButton;
	@FXML private Tab startTab;
	@FXML private TabPane mainTabPane;
	
	//Fixes the border for the main TabPane.
	@FXML private Pane borderFixPane;
	@FXML private TilePane content;
	
	private static IMatDataHandler db = IMatDataHandler.getInstance();
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		populateStore(ProductType.getProducts(ProductType.FRUIT));
	}

	
	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	private void populateStore(Set<Product> products) {
		
		for (Product product : products)
			content.getChildren().add(getProductDisplay(product));
	}
	
	private Node getProductDisplay(Product product) {
		ShoppingCartHandler cart = ShoppingCartHandler.getInstance();
		
		Label title = new Label(product.getName());
		title.getStyleClass().add("title-label");
		ImageView image = new ImageView(db.getFXImage(product, pictureWidth, pictureHeight));
		Label priceLabel = new Label(product.getPrice() + " " + product.getUnit());
		AnchorPane.setLeftAnchor(priceLabel, 0d);
		
		AnchorPane infoBox = new AnchorPane(priceLabel);
		
		
		Button button = new Button("Köp");
		button.setOnAction(e -> cart.addProduct(product));
		VBox display = new VBox(title, image, infoBox);
		button.setPrefSize(pictureWidth, 40);
		display.setAlignment(Pos.CENTER);
		display.getStyleClass().add("item-display");
		display.setPadding(new Insets(14, 35, 14, 35));
		display.getChildren().add(button);
		return display;
	}
}
