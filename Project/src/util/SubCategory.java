package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.*;

public class SubCategory {
	
	private static final int PICTURE_WIDTH = 180;
	
	private String name;
	private Set<Product> products;
	private List<Node> productViews;
	private static Map<Integer, Node> allProductViews;
	private static IMatDataHandler db = IMatDataHandler.getInstance();
	
	public SubCategory(String name) {
		this.name = name;
		products = new HashSet<Product>();
		productViews = new ArrayList<Node>();
	}
	
	public SubCategory(String name, ProductCategory... categories){
		this.name = name;
		products = new HashSet<Product>();
		productViews = new ArrayList<Node>();
		addProducts(categories);
	}
	
	public SubCategory(Node p, String searchString){
		this.name = searchString;
		products = new HashSet<Product>();
		productViews = new ArrayList<Node>();
		List<Product> productsFound = db.findProducts(searchString);
		for (Product product : productsFound){
			addProduct(product);
		}
	}
	
	public static List<Node> find(String searchString) {
		List<Node> views = new ArrayList<Node>();
		for (Product p : db.findProducts(searchString))
			views.add(allProductViews.get(p.getProductId()));
			
		return views;
	}
	
	public static void initializeProductViews() {
		allProductViews = new HashMap<Integer, Node>();
		
		for (Product product : IMatDataHandler.getInstance().getProducts())
			allProductViews.put(product.getProductId(), getProductDisplay(product));
	}
	
	public void addProducts(ProductCategory... categories) {
		Set<Product> products = new HashSet<Product>();
		
		for (ProductCategory category : categories) {
			products.addAll(IMatDataHandler.getInstance().getProducts(category));
			for (Product product : db.getProducts(category))
				productViews.add(getProductDisplay(product));
		}
		
		products.addAll(products);
	}
	
	public void addProduct(Product product) {
		products.add(product);
		productViews.add(getProductDisplay(product));
	}
	

	/** Does not remove products from view right now. Solve! */
	public void removeProduct(Product product) {
		products.remove(product);
	}
	
	public Set<Product> getProducts() {
		return products;
	}
	
	public void addProducts(int... productIds) {
		for (int id : productIds) {
			Product product = db.getProduct(id);
			products.add(product);
			productViews.add(getProductDisplay(product));
		}
	}
	
	public List<Node> getProductViews() {
		return productViews;
	}
	
	public String getName() {
		return this.name;
	}
	
	private static Node getProductDisplay(Product product) {
		ShoppingCartHandler cart = ShoppingCartHandler.getInstance();

		Label title = new Label(product.getName());
		title.getStyleClass().add("title-label");
		ImageView image = new ImageView(preserveRatio(product, PICTURE_WIDTH));
		Label priceLabel = new Label(product.getPrice() + " " + product.getUnit());
		AnchorPane.setLeftAnchor(priceLabel, 0d);

		AnchorPane infoBox = new AnchorPane(priceLabel);

		Button button = new Button("Köp");
		button.setOnAction(e -> cart.addProduct(product));
		VBox display = new VBox(title, image, infoBox);
		button.setPrefSize(PICTURE_WIDTH, 40);
		display.setAlignment(Pos.CENTER);
		display.getStyleClass().add("item-display");
		display.setPadding(new Insets(14, 35, 14, 35));
		display.getChildren().add(button);
		return display;
	}
	
	private static Image preserveRatio(Product product, int newWidth) {
		Image rawImage = db.getFXImage(product);
		double ratio = rawImage.getWidth() / newWidth;
		return db.getFXImage(product, newWidth, rawImage.getHeight() / ratio);
	}
}
