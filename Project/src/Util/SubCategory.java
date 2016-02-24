package Util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
	
	public SubCategory(String name, ProductCategory... categories){
		this.name = name;
		addProducts(categories);
	}
	
	public void addProducts(ProductCategory... categories) {
		Set<Product> products = new HashSet<Product>();
		
		for (ProductCategory category : categories)
			products.addAll(IMatDataHandler.getInstance().getProducts(category));
		
		products.addAll(products);
	}
	
	public void addProduct(Product product) {
		products.add(product);
	}
	
	public void removeProduct(Product product) {
		products.remove(product);
	}
	
	public Set<Product> getProducts() {
		return products;
	}
	
	public void addProducts(int... productIds) {
		for (int id : productIds)
			products.add(db.getProduct(id));
	}
	
	public String getName() {
		return this.name;
	}
	
	//Frukt & Grönt
	private final SubCategory berries = new SubCategory("Bär", ProductCategory.BERRY);
	private final SubCategory fruits = new SubCategory("Frukter", ProductCategory.CITRUS_FRUIT, ProductCategory.EXOTIC_FRUIT, ProductCategory.FRUIT, ProductCategory.MELONS);
	private final SubCategory vegitables = new SubCategory("Grönsaker", ProductCategory.VEGETABLE_FRUIT, ProductCategory.CABBAGE);
	private final SubCategory herbs = new SubCategory("Örter", ProductCategory.HERB);
	private final SubCategory roots = new SubCategory("Rotfrukter", ProductCategory.ROOT_VEGETABLE, ProductCategory.POTATO_RICE);
	private final SubCategory pod = new SubCategory("Baljväxter", ProductCategory.POD);
	private final SubCategory nuts = new SubCategory("Nötter & Frön", ProductCategory.NUTS_AND_SEEDS);
	//Kött & Fisk
	private final SubCategory meat = new SubCategory("Kött", ProductCategory.MEAT);
	private final SubCategory fish = new SubCategory("Fisk", ProductCategory.FISH);
	//Mejeri
	private final SubCategory dairies = new SubCategory("Mejeri", ProductCategory.DAIRIES);
	//Bröd & bakverk
	private final SubCategory breads = new SubCategory("Bröd", ProductCategory.BREAD);
	//Skafferi
	private final SubCategory powderStuff = new SubCategory("Torrvaror", ProductCategory.FLOUR_SUGAR_SALT);
	private final SubCategory pasta = new SubCategory("Pasta", ProductCategory.PASTA);
	//Fredagsmys
	private final SubCategory coldDrinks = new SubCategory("Kalla drycker", ProductCategory.COLD_DRINKS);
	private final SubCategory hotDrinks = new SubCategory("Varma drycker", ProductCategory.HOT_DRINKS);
	private final SubCategory sweets = new SubCategory("Sötsaker", ProductCategory.SWEET);
	
	public void categorize(){
		Map<Integer, Set<SubCategory>> superCategories = new TreeMap<Integer, Set<SubCategory>>();
		Set<SubCategory> greens = new TreeSet<SubCategory>();
		Set<SubCategory> cabinet = new TreeSet<SubCategory>();
		Set<SubCategory> bread = new TreeSet<SubCategory>();
		Set<SubCategory> dairy = new TreeSet<SubCategory>();
		Set<SubCategory> protein = new TreeSet<SubCategory>();
		Set<SubCategory> fridayCuddle = new TreeSet<SubCategory>();
		
		greens.add(berries);
		greens.add(fruits);
		greens.add(vegitables);
		greens.add(herbs);
		greens.add(roots);
		greens.add(pod);
		greens.add(nuts);
		protein.add(meat);
		protein.add(fish);
		dairy.add(dairies);
		bread.add(breads);
		cabinet.add(powderStuff);
		cabinet.add(pasta);
		fridayCuddle.add(coldDrinks);
		fridayCuddle.add(hotDrinks);
		fridayCuddle.add(sweets);

		superCategories.put(0, greens);
		superCategories.put(0, protein);
		superCategories.put(0, dairy);
		superCategories.put(0, bread);
		superCategories.put(0, cabinet);
		superCategories.put(0, fridayCuddle);
	}
}
