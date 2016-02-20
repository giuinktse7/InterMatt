package controllers;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
		new Thread(r).start();
		populateStore();
	}

	
	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	Runnable r = () -> {
		while(mainTabPane.getHeight() == 0)
			try { Thread.sleep(100); }
			catch (InterruptedException e) { e.printStackTrace(); }
		
		AnchorPane.setTopAnchor(borderFixPane, mainTabPane.getTabMaxHeight() + 11);
	};
	
	
	private void populateStore() {
		List<Product> veggies = db.getProducts(ProductCategory.VEGETABLE_FRUIT);
		List<Product> fruits = db.getProducts(ProductCategory.FRUIT);
		for (Product product : veggies)
			content.getChildren().add(getProductDisplay(product));
		for (Product product : fruits)
			content.getChildren().add(getProductDisplay(product));
	}
	
	private Node getProductDisplay(Product product) {
		Label title = new Label(product.getName());
		title.getStyleClass().add("title-label");
		ImageView image = new ImageView(db.getFXImage(product, pictureWidth, pictureHeight));
		Label priceLabel = new Label(product.getPrice() + " " + product.getUnit());
		AnchorPane.setLeftAnchor(priceLabel, 0d);
		
		AnchorPane infoBox = new AnchorPane(priceLabel);
		
		
		Button button = new Button("Köp");
		VBox display = new VBox(title, image, infoBox);
		button.setPrefSize(pictureWidth, 40);
		display.setAlignment(Pos.CENTER);
		display.getStyleClass().add("item-display");
		display.setPadding(new Insets(14, 35, 14, 35));
		display.getChildren().add(button);
		return display;
	}
}
