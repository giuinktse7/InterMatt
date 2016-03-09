package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

import se.chalmers.ait.dat215.project.IMatDataHandler;
import util.InformationStorage;
import util.ShoppingCartHandler;

public class RecipeController implements Initializable {

	private static RecipeController instance;
	@FXML private Label pane_title;
	@FXML private Label lblPrice;
	@FXML private Label lblTimeOfDelivery;
	@FXML private Label lblPaymentType;
	@FXML private Button btnBackToStore;
	@FXML private Button btnExit;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		btnExit.setOnMouseClicked(e -> System.exit(0));
		//btnBackToStore.setOnMouseClicked(e -> );	
	}

	public static RecipeController getInstance(){
		return instance;
	}

	public  void setTitleText(String name){
		pane_title.setText("Tack för ditt köp " + name + "!");
	}

	public void setPriceText(float price){
		lblPrice.setText(new DecimalFormat("#.##").format(price)+":-");
	}
	
	public void setDeliveryTimeText(String deliveryTime){
		lblPaymentType.setText(deliveryTime);
	}
	
	public void setPaymentText(String paymentType){
		lblTimeOfDelivery.setText(paymentType);
	}




}
