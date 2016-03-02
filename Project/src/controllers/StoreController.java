package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import se.chalmers.ait.dat215.project.*;
import util.SubCategory;

public class StoreController implements Initializable {

	@FXML private Button gotoShoppingListButton;
	@FXML private Tab startTab;
	@FXML private TabPane mainTabPane;
	@FXML private ScrollPane scrollPane;
	
	private final String[] tabStyleClasses = {"start-tab-pane", "greens-tab-pane", "bakery-tab-pane", "meat-tab-pane", "dairy-tab-pane", "cabinet-tab-pane", "friday-cuddle-tab-pane", };

	// Fixes the border for the main TabPane.
	@FXML private Pane borderFixPane;
	@FXML private TilePane content;
	@FXML private TextField txtSearch;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		initializeSubCategories();
		int ITEM_WIDTH = 252;
		//IntegerProperty ITEM_WIDTH = scrollPane.widthProperty().divide(252);
		content.widthProperty().addListener((obs, o, n) -> {
			if (n.doubleValue() % ITEM_WIDTH != 0) 
				content.maxWidthProperty().set(n.doubleValue() - n.doubleValue() % ITEM_WIDTH); 
			System.out.println(n.doubleValue()); 
			});
		txtSearch.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				/** Changes selected tab to "start" */
				mainTabPane.getSelectionModel().select(0);
				search(newValue);
			}
		});
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}

	private void populateStore(SubCategory subCategory) {
		populateStore(subCategory.getProductViews());
	}

	private void populateStore(List<Node> productViews) {
		content.getChildren().clear();

		content.getChildren().addAll(productViews);
	}

	private void search(String searchString) {
		if (searchString.equals(""))
			return;
		populateStore(SubCategory.find(searchString));
	}

	private void initializeSubCategories() {
		Map<Integer, Set<SubCategory>> categories = categorize();

		TabPane[] superCategories = getTabPanes();
		for (int i : categories.keySet()) {
			TabPane tabPane = superCategories[i];
			tabPane.getStyleClass().add(tabStyleClasses[i]);
			for (SubCategory subCategory : categories.get(i)) {
				Tab tab = new Tab(subCategory.getName());

				tab.setOnSelectionChanged(e -> {
					if (tab.isSelected())
						populateStore(subCategory);
				});
				tabPane.getTabs().add(tab);
			}
			mainTabPane.getTabs().get(i).setOnSelectionChanged(e -> {
				populateStore((SubCategory) categories.get(i).toArray()[0]);
			});
			;
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

	// Frukt & Gr�nt
	private final SubCategory berries = new SubCategory("B�r", ProductCategory.BERRY);
	private final SubCategory fruits = new SubCategory("Frukter", ProductCategory.CITRUS_FRUIT,
			ProductCategory.EXOTIC_FRUIT, ProductCategory.FRUIT, ProductCategory.MELONS);
	private final SubCategory vegitables = new SubCategory("Gr�nsaker", ProductCategory.VEGETABLE_FRUIT,
			ProductCategory.CABBAGE);
	private final SubCategory herbs = new SubCategory("�rter", ProductCategory.HERB);
	private final SubCategory roots = new SubCategory("Rotfrukter", ProductCategory.ROOT_VEGETABLE,
			ProductCategory.POTATO_RICE);
	private final SubCategory pod = new SubCategory("Baljv�xter", ProductCategory.POD);
	private final SubCategory nuts = new SubCategory("N�tter & Fr�n", ProductCategory.NUTS_AND_SEEDS);
	// K�tt & Fisk
	private final SubCategory meat = new SubCategory("K�tt", ProductCategory.MEAT);
	private final SubCategory fish = new SubCategory("Fisk", ProductCategory.FISH);
	// Mejeri
	private final SubCategory dairies = new SubCategory("Mejeri", ProductCategory.DAIRIES);
	// Br�d & bakverk
	private final SubCategory breads = new SubCategory("Br�d", ProductCategory.BREAD);
	// Skafferi
	private final SubCategory powderStuff = new SubCategory("Torrvaror", ProductCategory.FLOUR_SUGAR_SALT);
	private final SubCategory pasta = new SubCategory("Pasta", ProductCategory.PASTA);
	// Fredagsmys
	private final SubCategory coldDrinks = new SubCategory("Kalla drycker", ProductCategory.COLD_DRINKS);
	private final SubCategory hotDrinks = new SubCategory("Varma drycker", ProductCategory.HOT_DRINKS);
	private final SubCategory sweets = new SubCategory("S�tsaker", ProductCategory.SWEET);

	public Map<Integer, Set<SubCategory>> categorize() {
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