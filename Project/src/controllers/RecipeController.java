package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import se.chalmers.ait.dat215.project.IMatDataHandler;
import util.ShoppingCartHandler;

public class RecipeController implements Initializable {

	private static RecipeController instance;
	@FXML private Label pane_title;
	@FXML private Label lblPrice;
	@FXML private Label lblTimeOfDelivery;
	@FXML private Label lblPaymentType;
	@FXML private Button btnBackToStore;
	@FXML private Button btnExit;
	public String user_first = "du";

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		pane_title.setText("Tack för ditt köp " + user_first + "!");
		btnExit.setOnMouseClicked(e -> System.exit(0));
		//btnBackToStore.setOnMouseClicked(e -> );	
	}

	public static RecipeController getInstance(){
		return instance;
	}
	public void setPriceText(float price){
		lblPrice.setText(new DecimalFormat("#.##").format(price)+":-");
	}
	
	public void setDeliveryTimeText(String deliveryTime){
		lblTimeOfDelivery.setText(deliveryTime);
	}
	
	public void setPaymentText(String paymentType){
		lblTimeOfDelivery.setText(paymentType);
	}




}
