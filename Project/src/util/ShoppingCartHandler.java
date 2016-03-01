package util;

import java.util.List;

import control.ProductHBox;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingItem;

public class ShoppingCartHandler {
	public final int MAX_QUANTITY = 999;

	private Label lblTotalCost;
	private DoubleProperty totalCost = new SimpleDoubleProperty(0);

	private ListView<ProductHBox> cart;
	private BooleanProperty emptyProperty = new SimpleBooleanProperty(true);

	public static ShoppingCartHandler instance = new ShoppingCartHandler();
	private static IMatDataHandler db = IMatDataHandler.getInstance();

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

		totalCost.addListener(
				(obs, oldValue, newValue) -> lblTotalCost.setText(String.format("%.2f", newValue.doubleValue())));

		cart.getItems().addListener(UPDATE_TOTAL_COST);
	}
	
	public ObservableList<ProductHBox> getItems() {
		return this.cart.getItems();
	}

	private ListChangeListener<ProductHBox> UPDATE_TOTAL_COST = c -> {
		while (c.next()) {
			for (ProductHBox box : c.getAddedSubList()) {
				addToTotal(box.getQuantity() * box.getProduct().getPrice());
			}
			
			if (c.wasRemoved())
				for (ProductHBox box : c.getRemoved())
					addToTotal( - box.getQuantity() * box.getProduct().getPrice());
		}
	};

	public void passLabel(Label lblTotalCost) {
		this.lblTotalCost = lblTotalCost;
	}

	public boolean isEmpty() {
		return emptyProperty.get();
	}

	public static ShoppingCartHandler getInstance() {
		return instance;
	}

	/** Adds the product to the shopping cart */
	public void addProduct(Product product) {
		// For database
		ShoppingItem item = getDbShoppingItem(product);
		if (item != null)
			item.setAmount(item.getAmount() + 1);
		else
			db.getShoppingCart().addItem(new ShoppingItem(product, 1));

		ProductHBox productBox = getProductBoxFromCart(product);

		if (productBox != null)
			productBox.addQuantity(1);
		else {
			cart.getItems().add(new ProductHBox(product));
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

	/**
	 * Returns the ShoppingItem with product p. If there is none, returns null.
	 */
	public ShoppingItem getDbShoppingItem(Product p) {
		List<ShoppingItem> items = db.getShoppingCart().getItems();

		for (ShoppingItem item : items)
			//if (item.getProduct().getProductId() == p.getProductId())
			if (item.getProduct().equals(p))
				return item;

		return null;
	}
	
	public void addToTotal(double value) {
		totalCost.set(totalCost.get() + value);
	}
}