package controllers;

import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import util.ShoppingCartHandler;
import control.ModalPopup;
import control.CartItem;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class SaveListController implements Initializable {

	@FXML private ModalPopup bottomPane;
	@FXML private Button btnSave;
	@FXML private Button btnCancel;
	@FXML private TextField txtListName;

	private ShoppingCartHandler sch = ShoppingCartHandler.getInstance();
	private LoadListController llc = LoadListController.getInstance();
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		btnSave.setOnAction(e -> {
			if (txtListName.getText().equals("") ||  sch.getItems().isEmpty())
				return;
			if (llc.isNameTaken(txtListName.getText())){
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Upptaget namn");
				alert.setHeaderText("Det finns redan en lista med detta namn");
				alert.setContentText("Vill du spara över listan?");
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
				    llc.clearListFromFile(txtListName.getText());
				} else {
				    return;
				}
			}
			
			List<String> lines = new ArrayList<String>();
			lines.add("#"+txtListName.getText());
			for (CartItem item : sch.getItems()){
				lines.add(item.getProduct().getProductId()+","+item.getQuantity());
			}
			
			Path file = Paths.get("saved_shopping_lists.txt");
			try {
				if (!Files.exists(file)){
					file = Files.createFile(file);
				}
				List<String> existingLines = Files.readAllLines(file);

				lines.addAll(existingLines);
				Files.write(file, lines, Charset.forName("UTF-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			bottomPane.close();
			LoadListController.getInstance().updateList();
		});
		btnCancel.setOnAction(e -> bottomPane.close());
	}
}
