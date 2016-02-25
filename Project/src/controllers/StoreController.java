package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import Util.ShoppingCartHandler;
import Util.SubCategory;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.*;

public class StoreController implements Initializable {

	private static final int PICTURE_WIDTH = 180;

	@FXML private Button gotoShoppingListButton;
	@FXML private Tab startTab;
	@FXML private TabPane mainTabPane;
	
	//Fixes the border for the main TabPane.
	@FXML private Pane borderFixPane;
	@FXML private TilePane content;
	@FXML private TextField txtSearch;
	
	private static IMatDataHandler db = IMatDataHandler.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		initializeSubCategories();
		
		txtSearch.textProperty().addListener(new ChangeListener<String>(){
			@Override
	        public void changed(ObservableValue<? extends String> observable,
	                            String oldValue, String newValue) {
				/**
				 * Causes lag when many results are found.
				 * Try to run on separate thread if possible. 
				 * Look more into this after Friday*/
				search(newValue);
	        }
		});
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}

	private void populateStore(SubCategory subCategory) {
		content.getChildren().clear();
		
		content.getChildren().addAll(subCategory.getProductViews());
	}
	
	private void search(String searchString){
		populateStore(new SubCategory("Search", searchString));
	}
	
	private void initializeSubCategories() {
		Map<Integer, Set<SubCategory>> categories = categorize();
		

		
		TabPane[] superCategories = getTabPanes();
		for (int i : categories.keySet()) {
			TabPane tabPane = superCategories[i];

			for (SubCategory subCategory : categories.get(i)) {
				Tab tab = new Tab(subCategory.getName());
				tab.setOnSelectionChanged(e -> {
					if (tab.isSelected())
						populateStore(subCategory);
				});
				tabPane.getTabs().add(tab);
			}
		}
	}

	private TabPane[] getTabPanes() {
		List<TabPane> tabPanes = new ArrayList<TabPane>();

		for (Tab tab : mainTabPane.getTabs()) {

			if (tab.getContent().getClass().equals(TabPane.class))
				tabPanes.add((TabPane) tab.getContent());
			else {
				Pane content = (Pane) tab.getContent();

				for (Node n : content.getChildren())
					if (n.getClass().equals(TabPane.class)) {
						tabPanes.add((TabPane) n);
						break;
					}
			}
		}

		return tabPanes.toArray(new TabPane[0]);
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
		
		public Map<Integer, Set<SubCategory>> categorize(){
			Map<Integer, Set<SubCategory>> superCategories = new HashMap<Integer, Set<SubCategory>>();
			Set<SubCategory> greens = new HashSet<SubCategory>();
			Set<SubCategory> cabinet = new HashSet<SubCategory>();
			Set<SubCategory> bread = new HashSet<SubCategory>();
			Set<SubCategory> dairy = new HashSet<SubCategory>();
			Set<SubCategory> protein = new HashSet<SubCategory>();
			Set<SubCategory> fridayCuddle = new HashSet<SubCategory>();
			
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

			superCategories.put(1, greens);
			superCategories.put(2, bread);
			superCategories.put(3, protein);
			superCategories.put(4, dairy);
			superCategories.put(5, cabinet);
			superCategories.put(6, fridayCuddle);
			
			return superCategories;
		}
}
