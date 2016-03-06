package util;

import java.util.ArrayList;
import java.util.List;

import control.CartItem;
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
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingItem;

public class ShoppingCartHandler {
	public final int MAX_QUANTITY = 50;

	private Label lblTotalCost;
	private DoubleProperty totalCost = new SimpleDoubleProperty(0);

	private ListView<CartItem> cart;
	private BooleanProperty emptyProperty = new SimpleBooleanProperty(true);

	public static ShoppingCartHandler instance = new ShoppingCartHandler();
	//private static IMatDataHandler db = IMatDataHandler.getInstance();

	private ShoppingCartHandler() {

	}

	public BooleanProperty emptyProperty() {
		return this.emptyProperty;
	}

	public void setCart(ListView<CartItem> cart) {
		this.cart = cart;
		cart.getItems().addListener((Change<? extends CartItem> c) -> {
			emptyProperty.set(cart.getItems().size() == 0);
		});

		totalCost.addListener(
				(obs, oldValue, newValue) -> lblTotalCost.setText(String.format("Totalt: %.2f", newValue.doubleValue()) + ":-"));

		cart.getItems().addListener(UPDATE_TOTAL_COST);
	}
	
	public ObservableList<CartItem> getItems() {
		return this.cart.getItems();
	}

	private ListChangeListener<CartItem> UPDATE_TOTAL_COST = c -> {
		while (c.next()) {
			for (CartItem box : c.getAddedSubList()) {
				addToTotal(box.getQuantity() * box.getProduct().getPrice());
			}
			
			if (c.wasRemoved())
				for (CartItem box : c.getRemoved())
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
	public void addProduct(Product product, double quantity) {
		CartItem productBox = getProductBoxFromCart(product);

		if (productBox != null)
			productBox.addQuantity(quantity);
		else {
			CartItem box = new CartItem(product);
			box.setQuantity(quantity);
			cart.getItems().add(box);
		}
	}
	
	/** Adds the product to the shopping cart */
	public void addProduct(Product product) {
		addProduct(product, 1);
	}

	private CartItem getProductBoxFromCart(Product product) {
		ObservableList<CartItem> productsInCart = cart.getItems();

		for (Node node : productsInCart) {
			CartItem box = (CartItem) node;
			if (box.getProduct().equals(product))
				return box;
		}

		return null;
	}
	
	public void addToTotal(double value) {
		totalCost.set(totalCost.get() + value);
	}
	
	public void clearCart() {
		this.cart.getItems().clear();
		totalCost.set(0);
	}
	
	/** Returns the cart's items as a List of ShoppingItems. */
	public List<ShoppingItem> getCartItems() {
		List<ShoppingItem> items = new ArrayList<ShoppingItem>();
		cart.getItems().forEach(box -> {
			ShoppingItem item = new ShoppingItem(box.getProduct(), box.getQuantity());
			items.add(item);
		});
		
		return items;
	}
}