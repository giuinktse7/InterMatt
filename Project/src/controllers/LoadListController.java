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
import util.ShoppingCartHandler;
import control.ModalPopup;
import control.ShoppingListHBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class LoadListController implements Initializable {
	private static LoadListController instance;
	
	@FXML private ModalPopup bottomPane;
	@FXML private Button btnLoad;
	@FXML private Button btnCancel;
	@FXML private Label lblListName;
	@FXML private ListView<HBox> lvLists;
	@FXML private ListView<HBox> lvItems;
	private List<ShoppingListHBox> lists;
	private static IMatDataHandler db = IMatDataHandler.getInstance();
	private ShoppingCartHandler sch = ShoppingCartHandler.getInstance(); 
	private float[] perc = {0.4f, 0.3f, 0.3f};
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		btnLoad.setOnAction(e -> loadToChart());
		btnCancel.setOnAction(e -> bottomPane.close());
		lvLists.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> generateProductList((ShoppingListHBox)n));
		instance = this;
		updateList();
	}
	
	public static LoadListController getInstnace(){
		return instance;
	}
	
	public void loadToChart(){
		ShoppingListHBox list = (ShoppingListHBox)lvLists.getSelectionModel().getSelectedItem();
		for (Object o : list.getLines().keySet()){
				float quantity = (float)list.getLines().get(o);
				Product product = db.getProduct((int)o);
				sch.addProduct(product, quantity);
		}
		
		bottomPane.close();
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
		
		AnchorPane ap = new AnchorPane();

		Label lblName = new Label(product.getName());
		AnchorPane.setLeftAnchor(lblName, 0d);
		lblName.setPrefWidth(120);
		lblName.setAlignment(Pos.CENTER_LEFT);

		DecimalFormat format = new DecimalFormat("0.#");
		String amount = format.format(quantity).replace('.', ',');
		Label lblQuantity = new Label(String.format("%s %s", amount, product.getUnitSuffix()));
		AnchorPane.setLeftAnchor(lblQuantity, 160d);
		lblQuantity.setPrefWidth(60);
		lblQuantity.setAlignment(Pos.CENTER_LEFT);

		Label lblPrice = new Label(new DecimalFormat("#.##").format(product.getPrice() * quantity)+":-");
		lblPrice.setContentDisplay(ContentDisplay.RIGHT);
		AnchorPane.setLeftAnchor(lblPrice, 240d);
		lblPrice.setPrefWidth(90);
		lblPrice.setAlignment(Pos.CENTER_RIGHT);

		ap.getChildren().addAll(lblName, lblQuantity, lblPrice);
		HBox box = new HBox(ap);
		
		lvItems.getItems().add(box);
	}
}