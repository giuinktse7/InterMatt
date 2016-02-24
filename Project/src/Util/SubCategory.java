package Util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import se.chalmers.ait.dat215.project.*;

public class SubCategory {

	private String name;
	private Set<Product> products;
	private IMatDataHandler db = IMatDataHandler.getInstance();
	
	public SubCategory(String name) {
		this.name = name;
		products = new TreeSet<Product>();
	}
	
	public void addProducts(SubCategory type, ProductCategory... categories) {
		Set<Product> products = new HashSet<Product>();
		
		for (ProductCategory category : categories)
			products.addAll(IMatDataHandler.getInstance().getProducts(category));
		
		products.addAll(products);
	}
	
	public void addProduct(SubCategory type, Product product) {
		products.add(product);
	}
	
	public void removeProduct(SubCategory type, Product product) {
		products.remove(product);
	}
	
	public Set<Product> getProducts(SubCategory fruit) {
		return products;
	}
	
	public void addProducts(int... productIds) {
		for (int id : productIds)
			products.add(db.getProduct(id));
	}
	
	public String getName() {
		return this.name;
	}
}
