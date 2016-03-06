package util;

import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ShoppingItem;

public class ComparableShoppingItem extends ShoppingItem {

	public ComparableShoppingItem(Product product) {
		super(product);
	}
	
	public ComparableShoppingItem(Product product, double amount) {
		super(product);
	}

	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!obj.getClass().equals(ComparableShoppingItem.class)) return false;
			
		ComparableShoppingItem that = (ComparableShoppingItem) obj;
		return this.getProduct().equals(that.getProduct());
	}
}
