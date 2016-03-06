package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import se.chalmers.ait.dat215.project.*;
import util.SubCategory;

public class StoreController implements Initializable {

	@FXML
	private Button gotoShoppingListButton;
	@FXML
	private Tab startTab;
	@FXML
	private TabPane mainTabPane;
	@FXML
	private ScrollPane scrollPane;
	@FXML
	private Tab invisibleTab;
	
	IMatDataHandler db = IMatDataHandler.getInstance();

	private final String[] tabStyleClasses = { "greens-tab-pane", "meat-tab-pane", "dairy-tab-pane", "drinks-tab-pane",
			"cabinet-tab-pane", "friday-cuddle-tab-pane", };

	// content.getUserData() holds the previous value of prefColumns.
	@FXML
	private TilePane content;
	@FXML
	private TextField txtSearch;
	@FXML
	private Label lblSearchResult;
	
	private SubCategory frequentlyBought = new SubCategory("frequently bought");

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		initializeSubCategories();

		//Takes care of most frequently bought
		loadFrequentlyBought();
		
		ScrollPane.positionInArea(content, 0, 0, 800, 800, 0, new Insets(50, 0, 0, 0), HPos.CENTER, VPos.TOP, true);
		invisibleTab.setDisable(true);
		
		content.setUserData(content.getPrefColumns());

		int ITEM_WIDTH = 255;

		content.prefColumnsProperty().addListener((obs, o, n) -> {
			content.setUserData(o.intValue());
			refreshContentMargin();
		});

		content.getChildren().addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> c) {
				if (!c.getList().isEmpty())
					refreshContentMargin();
			}
		});
		scrollPane.widthProperty().addListener((obs, o, n) -> {
			content.setPrefColumns((int) (n.doubleValue() / ITEM_WIDTH));
			content.maxWidthProperty().set(ITEM_WIDTH * content.getPrefColumns());
		});
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	private void loadFrequentlyBought() {
		List<Order> orders = db.getOrders();
		List<ShoppingItem> items = new ArrayList<ShoppingItem>();
		double[] amounts = new double[500];
		
		orders.forEach(order -> {
			order.getItems().forEach(item -> {
				amounts[item.getProduct().getProductId()] += item.getAmount();
			});
		});
		
		for (int i = 0; i < amounts.length; ++i)
			if (amounts[i] > 0)
				items.add(new ShoppingItem(db.getProduct(i), amounts[i]));
		
		//Sort by amount
		Collections.sort(items, (o1, o2) -> {
			ShoppingItem item1 = (ShoppingItem) o1;
			ShoppingItem item2 = (ShoppingItem) o2;
			
			return (int) (item2.getAmount() - item1.getAmount());
		});
		
		for (ShoppingItem item : items) {
			System.out.printf("%s : %.2f\n", item.getProduct().getName(), item.getAmount());
		}
		
		int stopAt = Math.min(12, items.size());
		
		for (int i = 0; i < stopAt; ++i)
			frequentlyBought.addProduct(items.get(i).getProduct());
	}

	private void refreshContentMargin() {
		int stopAt = Math.max((int) content.getUserData(), content.getPrefColumns());
		for (int i = 0; i < stopAt; ++i) {
			Insets insets;
			if (i < content.getPrefColumns())
				insets = new Insets(21, 0, 0, 0);
			else
				insets = new Insets(0, 0, 0, 0);
			
			if (content.getChildren().size() > i && content.getChildren().get(i) != null)
				TilePane.setMargin(content.getChildren().get(i), insets);
		}
	}

	public ChangeListener<String> getSearchAction() {
		return (obs, oldValue, newValue) -> {
			/** Changes selected tab to "search" */
			mainTabPane.getSelectionModel().select(invisibleTab);
			List<Node> nodes = searchForItems(newValue);
			if (nodes != null) {
				populateStore(nodes);
				lblSearchResult
						.setText(String.format("%d varor matchar sökningen: \"%s\"", nodes.size(), newValue.trim()));
			}
		};
	}

	private void populateStore(SubCategory subCategory) {
		populateStore(subCategory.getProductViews());
	}

	private void populateStore(List<Node> productViews) {
		content.getChildren().clear();
		content.getChildren().addAll(productViews);
	}

	private List<Node> searchForItems(String searchString) {
		searchString = searchString.trim();
		if (searchString.equals(""))
			return null;

		return SubCategory.find(searchString);
	}

	private void initializeSubCategories() {
		Map<Integer, Set<SubCategory>> categories = categorize();

		TabPane[] superCategories = getTabPanes();
		for (int i : categories.keySet()) {
			TabPane tabPane = superCategories[i];
			tabPane.getStyleClass().add(tabStyleClasses[i]);
			
			for (SubCategory subCategory : categories.get(i)) {
				Tab tab = new Tab(subCategory.getName());
				tab.getStyleClass().add("sub-tab");

				tab.setOnSelectionChanged(e -> {
					if (tab.isSelected())
						populateStore(subCategory);
				});
				tabPane.getTabs().add(tab);
			}
			
			//i + 1 to avoid the 'Start' tab
			mainTabPane.getTabs().get(i + 1).setOnSelectionChanged(e -> {
				populateStore((SubCategory) categories.get(i).toArray()[0]);
			});
			
		}
		
		Tab searchTab = mainTabPane.getTabs().get(0);
		searchTab.setOnSelectionChanged(e -> {
			populateStore(frequentlyBought);
		});
	}

	private TabPane[] getTabPanes() {
		List<TabPane> tabPanes = new ArrayList<TabPane>();

		// Loop through main tab-pane to find subt-tabs
		for (Tab tab : mainTabPane.getTabs()) {
			// Is there a subTabPane here?
			Node tabContent = tab.getContent();
			// The sub-tabPanes are wrapped in a FlowPane
			if (tabContent.getClass().equals(FlowPane.class)) {
				Node firstChild = ((FlowPane) tabContent).getChildren().get(0);
				if (firstChild.getClass().equals(TabPane.class))
					tabPanes.add((TabPane) firstChild);
			}
		}

		return tabPanes.toArray(new TabPane[0]);
	}

	// Frukt & Grönt
	private final SubCategory berries = new SubCategory("Bär", ProductCategory.BERRY);
	private final SubCategory fruits = new SubCategory("Frukter", ProductCategory.CITRUS_FRUIT,
			ProductCategory.EXOTIC_FRUIT, ProductCategory.FRUIT, ProductCategory.MELONS);
	private final SubCategory vegitables = new SubCategory("Grönsaker", ProductCategory.VEGETABLE_FRUIT,
			ProductCategory.CABBAGE);
	private final SubCategory herbs = new SubCategory("Örter", ProductCategory.HERB);
	private final SubCategory roots = new SubCategory("Rotfrukter", ProductCategory.ROOT_VEGETABLE,
			ProductCategory.POTATO_RICE);
	private final SubCategory pod = new SubCategory("Baljväxter", ProductCategory.POD);
	private final SubCategory nuts = new SubCategory("Nötter & Frön", ProductCategory.NUTS_AND_SEEDS);
	// K�tt & Fisk
	private final SubCategory meat = new SubCategory("Kött", ProductCategory.MEAT);
	private final SubCategory fish = new SubCategory("Fisk", ProductCategory.FISH);
	// Mejeri
	private final SubCategory dairies = new SubCategory("Mejeri", ProductCategory.DAIRIES);
	// Bröd & bakverk
	private final SubCategory breads = new SubCategory("Bröd", ProductCategory.BREAD);
	// Skafferi
	private final SubCategory powderStuff = new SubCategory("Torrvaror", ProductCategory.FLOUR_SUGAR_SALT);
	private final SubCategory pasta = new SubCategory("Pasta", ProductCategory.PASTA);
	// Fredagsmys
	private final SubCategory coldDrinks = new SubCategory("Kalla drycker", ProductCategory.COLD_DRINKS);
	private final SubCategory hotDrinks = new SubCategory("Varma drycker", ProductCategory.HOT_DRINKS);
	private final SubCategory sweets = new SubCategory("Sötsaker", ProductCategory.SWEET);

	public Map<Integer, Set<SubCategory>> categorize() {
		Map<Integer, Set<SubCategory>> superCategories = new HashMap<Integer, Set<SubCategory>>();
		Set<SubCategory> greens = new HashSet<SubCategory>();
		Set<SubCategory> cabinet = new HashSet<SubCategory>();
		Set<SubCategory> dairy = new HashSet<SubCategory>();
		Set<SubCategory> protein = new HashSet<SubCategory>();
		Set<SubCategory> fridayCuddle = new HashSet<SubCategory>();
		Set<SubCategory> drinks = new HashSet<SubCategory>();

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
		drinks.add(coldDrinks);
		drinks.add(hotDrinks);
		cabinet.add(breads);
		cabinet.add(powderStuff);
		cabinet.add(pasta);
		fridayCuddle.add(sweets);

		superCategories.put(0, greens);
		superCategories.put(1, protein);
		superCategories.put(2, dairy);
		superCategories.put(3, drinks);
		superCategories.put(4, cabinet);
		superCategories.put(5, fridayCuddle);

		return superCategories;
	}
}