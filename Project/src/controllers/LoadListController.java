package controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Product;
import control.ModalPopup;
import control.ShoppingListHBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class LoadListController implements Initializable {
	private static LoadListController instance;
	
	@FXML private ModalPopup bottomPane;
	@FXML private Button yesButton;
	@FXML private Button cancelButton;
	@FXML private Label lblListName;
	@FXML private ListView<HBox> lvLists;
	@FXML private ListView<HBox> lvItems;
	private List<ShoppingListHBox> lists;
	private IMatDataHandler db = IMatDataHandler.getInstance();
	private float[] perc = {0.4f, 0.3f, 0.3f};
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		yesButton.setOnAction(e -> bottomPane.close());
		cancelButton.setOnAction(e -> bottomPane.close());
		lvLists.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> generateProductList((ShoppingListHBox)n));
		instance = this;
		updateList();
	}
	
	public static LoadListController getInstnace(){
		return instance;
	}

	public void updateList(){
		/** clears list by creating a new one*/
		lists = new ArrayList<ShoppingListHBox>();
		Path file = Paths.get("saved_shopping_lists.txt");
		/** fallback if file does not exist*/
		if (!Files.exists(file))
			return;
		List<String> linesInFile = new ArrayList<String>();
		try {
			linesInFile = Files.readAllLines(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int index = 0;
		for (String line : linesInFile){
			if (line.charAt(0)=='#'){
				/** New lists starts with # */
				Label lbl = new Label(line.substring(1, line.length()));
				ShoppingListHBox box = new ShoppingListHBox(lbl);
				lists.add(box);
				lvLists.getItems().add(box);
				index++;
			} else{
				/** Lines with product-id and quantity*/
				String[] values = line.split(",");
				int id = Integer.parseInt(values[0]);
				float quantity = Float.parseFloat(values[1]);
				lists.get(index - 1).addLine(id, quantity);;
			}
		}
	}

	public void generateProductList(ShoppingListHBox list){
		lblListName.setText(list.getName());
		lvItems.getItems().clear();
		for (Object o : list.getLines().keySet()){
			float quantity = (float)list.getLines().get(o);
			Product product = db.getProduct((int)o);
			appendListItem(product, quantity);
		}
	}

	public void appendListItem(Product product, float quantity){
		Label lblName = new Label(product.getName());
		lblName.setAlignment(Pos.CENTER_LEFT);
		Label lblQuantity = new Label(quantity+" "+product.getUnitSuffix());
		lblQuantity.setAlignment(Pos.CENTER);
		Label lblPrice = new Label(new DecimalFormat("#.##").format(product.getPrice() * quantity)+":-");
		lblPrice.setAlignment(Pos.CENTER_RIGHT);
		HBox box = new HBox(lblName, lblQuantity, lblPrice);
		box.setAlignment(Pos.CENTER_LEFT);
		lvItems.getItems().add(box);
		lblName.minWidth(200);
		
		/*
		lblName.minWidth((int)(lvItems.getWidth()*perc[0]));
		lblName.maxWidth((int)(lvItems.getWidth()*perc[0]));
		lblQuantity.minWidth((int)(lvItems.getWidth()*perc[1]));
		lblQuantity.maxWidth((int)(lvItems.getWidth()*perc[1]));
		lblPrice.minWidth((int)(lvItems.getWidth()*perc[2]));
		lblPrice.maxWidth((int)(lvItems.getWidth()*perc[2]));
		*/
		System.out.println(box.getWidth() + ", " + lvItems.getWidth());
	}
}