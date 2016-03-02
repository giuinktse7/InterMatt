package controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import control.ModalPopup;
import control.ShoppingListHBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

public class LoadListController implements Initializable {

	@FXML private ModalPopup bottomPane;
	@FXML private Button yesButton;
	@FXML private Button cancelButton;
	@FXML private Label lblListName;
	@FXML private ListView<HBox> lvLists;
	@FXML private ListView<HBox> lvItems;
	private List<ShoppingListHBox> lists;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		yesButton.setOnAction(e -> bottomPane.close());
		cancelButton.setOnAction(e -> bottomPane.close());
		updateList();
	}
	
	public void updateList(){
		lists = new ArrayList<ShoppingListHBox>();
		Path file = Paths.get("saved_shopping_lists.txt");
		if (!Files.exists(file))
			return;
		List<String> list = new ArrayList<String>();
		try {
			list = Files.readAllLines(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		int index = -1;
		for (String string : list){
			if (string.charAt(0)=='#'){
				Label lbl = new Label(string.substring(1, string.length()));
				ShoppingListHBox box = new ShoppingListHBox(lbl);
				box.setOnMouseClicked(e -> generateProductList(box)); //ChangeListener på listView istället
				lists.add(box);
				lvLists.getItems().add(box);
				index++;
			} else{
				String[] values = string.split(",");
				lists.get(index).addLine(values[0], Float.parseFloat(values[1]));
			}
		}
	}
	
	public void generateProductList(ShoppingListHBox list){
		lblListName.setText(list.getName());
		lvItems.getItems().clear();
		for (Object o : list.getLines().keySet()){
			String name = (String)o;
			float quantity = (float)list.getLines().get(name);
			lvItems.getItems().add(new HBox(new Label(name), new Label(quantity+"")));
		}
	}

}
