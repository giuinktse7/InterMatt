package controllers;

import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import util.ShoppingCartHandler;
import control.ModalPopup;
import control.ProductHBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SaveListController implements Initializable {

	@FXML private ModalPopup bottomPane;
	@FXML private Button btnSave;
	@FXML private Button btnCancel;
	@FXML private TextField txtListName;

	private ShoppingCartHandler sch = ShoppingCartHandler.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		btnSave.setOnAction(e -> {
			List<String> lines = new ArrayList<String>();
			lines.add("#"+txtListName.getText());
			for (ProductHBox item : sch.getItems()){
				lines.add(item.getProduct().getName()+","+item.getQuantity());
			}

			Path file = Paths.get("saved_shopping_lists.txt");
			
			try {
				List<String> existingLines = Files.readAllLines(file);
				
				lines.addAll(existingLines);
				Files.write(file, lines, Charset.forName("UTF-8"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			bottomPane.close();
		});
		btnCancel.setOnAction(e -> bottomPane.close());
	}

}
