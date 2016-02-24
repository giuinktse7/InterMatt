package Util;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import se.chalmers.ait.dat215.project.Product;

public class ProductHBox extends HBox {

	private Product product;
	private int amount;

	public ProductHBox(Node... nodes) {
		super(nodes);
	}

	public Product getPrpoduct() {
		return this.product;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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
			setAmount(Integer.parseInt(amount));
		} catch (NumberFormatException nfe) {
		}
	}

}
