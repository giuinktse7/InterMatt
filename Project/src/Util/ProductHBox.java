package Util;

import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import se.chalmers.ait.dat215.project.Product;

public class ProductHBox extends HBox {

	private Product product;
	private IntegerProperty amountProperty;

	public IntegerProperty quantityProperty() {
		return this.amountProperty;
	}
	
	public void setQuantity(int value) {
		amountProperty.set(value);
	}
	
	public void addQuantity(int value) {
		amountProperty.set(amountProperty.get() + value);
	}
	
	public int getQuantity() {
		return amountProperty.get();
	}
	
	public ProductHBox(Product product, Node... nodes) {
		super(nodes);
		this.product = product;
		this.amountProperty = new SimpleIntegerProperty(1);
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
	
	/** Returns true if they represent the same object */
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!obj.getClass().equals(this.getClass())) return false;
			
		ProductHBox that = (ProductHBox) obj;
			
		return this.product.getProductId() == (that.product.getProductId());
	}

}
