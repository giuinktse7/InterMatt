package Util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Product;
import se.chalmers.ait.dat215.project.ProductCategory;

public enum ProductType {

	FRUIT, MEAT, FISH, VEGETABLES;

	private static Map<ProductType, Set<Product>> productSets;

	/** Initialise the product sets */
	public static void initialize() {
		productSets = new HashMap<ProductType, Set<Product>>();
		for (ProductType type : ProductType.values())
			productSets.put(type, new HashSet<Product>());
		
		addProducts(FRUIT, ProductCategory.FRUIT, ProductCategory.CITRUS_FRUIT, ProductCategory.EXOTIC_FRUIT);
	}
	
	public static void addProducts(ProductType type, ProductCategory... categories) {
		Set<Product> products = new HashSet<Product>();
		
		for (ProductCategory category : categories)
			products.addAll(IMatDataHandler.getInstance().getProducts(category));
		
		productSets.put(type, products);
	}
	
	public static void addProduct(ProductType type, Product product) {
		productSets.get(type).add(product);
	}
	
	public static void removeProduct(ProductType type, Product product) {
		productSets.get(type).remove(product);
	}
	
	public static Set<Product> getProducts(ProductType fruit) {
		return productSets.get(fruit);
	}
}
