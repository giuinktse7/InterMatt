package controllers;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import control.ModalPopup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class LoadListController implements Initializable {

	@FXML private ModalPopup bottomPane;
	@FXML private Button yesButton;
	@FXML private Button cancelButton;
	@FXML private ListView lvLists;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		yesButton.setOnAction(e -> bottomPane.close());
		cancelButton.setOnAction(e -> bottomPane.close());
		updateList();
	}
	
	public void updateList(){
		Path file = Paths.get("saved_shopping_lists.txt");
		List<String> list = new ArrayList<String>();
		try {
			list = Files.readAllLines(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("\nShoppinglistor:");
		for (String string : list){
			if (string.charAt(0)=='#'){
				//sekvens av nästa lista påbörjas
				System.out.println(string);
			}
		}
	}

}
