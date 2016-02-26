package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

public class PurchaseController implements Initializable {
	@FXML private RadioButton rbPayByCard;
	
	// 4 credit card fields.
	@FXML private TextField txt_cardnr_1;
	@FXML private TextField txt_cardnr_2;
	@FXML private TextField txt_cardnr_3;
	@FXML private TextField txt_cardnr_4;

	// Credit card expiration year and month
	@FXML private ChoiceBox<String> cb_card_month;
	private List<String> months = Arrays.asList("-- Månad --", "Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September", "Oktober", "November", "December");
	@FXML private ChoiceBox<String> cb_card_year;
	private List<String> years = Arrays.asList("-- År --", "2016", "2017", "2018");

	// Credit card CVV fields
	@FXML private TextField txt_card_cvv;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupInputConstraints();
	}
	
	private void setupInputConstraints() {
		// Fill expiration year / month with values. Select first.
				cb_card_month.getItems().addAll(months);
				cb_card_month.setValue(months.get(0));
				cb_card_year.getItems().addAll(years);
				cb_card_year.setValue(years.get(0));


				// Listeners on the credit card fields
				// Limit max length to 4 characters. When filled focus next fields
				// Do not allow non-numeric characters

				txt_cardnr_1.textProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue.matches("\\d*")) {
						txt_cardnr_1.setText(oldValue);
						newValue = oldValue;
					}

		            if (newValue.length() > 4){
						txt_cardnr_1.setText(txt_cardnr_1.getText().substring(0,4));
					}

					if (newValue.length() == 4){
						txt_cardnr_2.requestFocus();
					}
		        });

				txt_cardnr_2.textProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue.matches("\\d*")) {
						txt_cardnr_2.setText(oldValue);
						newValue = oldValue;
					}
					if (newValue.length() > 4){
						txt_cardnr_2.setText(txt_cardnr_2.getText().substring(0,4));
					}
					if (newValue.length() == 4){
						txt_cardnr_3.requestFocus();
					}
				});

				txt_cardnr_3.textProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue.matches("\\d*")) {
						txt_cardnr_3.setText(oldValue);
						newValue = oldValue;
					}
					if (newValue.length() > 4){
						txt_cardnr_3.setText(txt_cardnr_3.getText().substring(0,4));
					}
					if (newValue.length() == 4){
						txt_cardnr_4.requestFocus();
					}
				});

				txt_cardnr_4.textProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue.matches("\\d*")) {
						txt_cardnr_4.setText(oldValue);
						newValue = oldValue;
					}
					if (newValue.length() > 4){
						txt_cardnr_4.setText(txt_cardnr_4.getText().substring(0,4));
					}
					if (newValue.length() == 4){
						cb_card_year.requestFocus();
					}
				});


				// Listeners for arrow keys to navigate between the credit card fields
				txt_cardnr_1.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.RIGHT){
						txt_cardnr_2.requestFocus();
					}
				});

				txt_cardnr_2.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.RIGHT){
						txt_cardnr_3.requestFocus();
					}else if (event.getCode() == KeyCode.LEFT){
						txt_cardnr_1.requestFocus();
					}
				});

				txt_cardnr_3.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.RIGHT){
						txt_cardnr_4.requestFocus();
					}else if (event.getCode() == KeyCode.LEFT){
						txt_cardnr_2.requestFocus();
					}
				});

				txt_cardnr_4.setOnKeyPressed(event -> {
					if (event.getCode() == KeyCode.LEFT){
						txt_cardnr_3.requestFocus();
					}
				});

				txt_card_cvv.textProperty().addListener((observable, oldValue, newValue) -> {
					if (!newValue.matches("\\d*")) {
						txt_card_cvv.setText(oldValue);
						newValue = oldValue;
					}
					if (newValue.length() > 3){
						txt_card_cvv.setText(txt_card_cvv.getText().substring(0,3));
					}
					if (newValue.length() == 3){
						// Select Fortsättknapp maybe....
					}
				});
	}

	public final boolean verifyInput(){
		if(rbPayByCard.selectedProperty().getValue()){
			if (txt_cardnr_1.getText().length() != 4) return false;
			if (txt_cardnr_2.getText().length() != 4) return false;
			if (txt_cardnr_3.getText().length() != 4) return false;
			if (txt_cardnr_4.getText().length() != 4) return false;
			 
			if (cb_card_year.getSelectionModel().getSelectedIndex() == 0 || 
					cb_card_month.getSelectionModel().getSelectedIndex() == 0) return false;
			
			int year = Integer.parseInt(cb_card_year.getSelectionModel().getSelectedItem().toString());
			int month = cb_card_month.getSelectionModel().getSelectedIndex();
			Calendar c = new GregorianCalendar(year, month, -1);
			Calendar today = Calendar.getInstance();
			if (!c.getTime().after(today.getTime())) return false;
			if (txt_card_cvv.getText().length() != 3) return false;
		}
		return true;
	}



}
