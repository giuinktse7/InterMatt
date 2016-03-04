package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

import se.chalmers.ait.dat215.project.IMatDataHandler;
import util.ShoppingCartHandler;

public class RecipeController implements Initializable {

	@FXML private Label pane_title;
	@FXML private Label lblPrice;
	@FXML private Label lblTimeOfDelivery;
	@FXML private Label lblPaymentType;
	@FXML private Button btnBackToStore;
	@FXML private Button btnExit;
	public String user_first = "du";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pane_title.setText("Tack för ditt köp " + user_first + "!");
		btnExit.setOnMouseClicked(e -> System.exit(0));
		//btnBackToStore.setOnMouseClicked(e -> );	
	}
	
	




}
