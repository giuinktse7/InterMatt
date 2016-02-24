package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import Util.ShoppingCartHandler;
import Util.SubCategory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
	
	private static IMatDataHandler db = IMatDataHandler.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		initializeSubCategories();
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}

	private void populateStore(SubCategory subCategory) {
		content.getChildren().clear();
		
		content.getChildren().addAll(subCategory.getProductViews());
	}

	private void initializeSubCategories() {
		Map<Integer, Set<SubCategory>> categories = new HashMap<Integer, Set<SubCategory>>();
		categories.put(0, new HashSet<SubCategory>());
		SubCategory test = new SubCategory("Test");
		test.addProducts(2, 5, 1);
		categories.get(0).add(test);
		
		SubCategory test2 = new SubCategory("Works!");
		test2.addProducts(5, 17, 55, 67, 33, 81, 22, 23, 29);
		categories.get(0).add(test2);
		

		
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
}
