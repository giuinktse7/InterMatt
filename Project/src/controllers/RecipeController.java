package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {

	@FXML private Label pane_title;
	public String user_first = "du";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pane_title.setText("Tack för ditt köp " + user_first + "!");
	}





}
