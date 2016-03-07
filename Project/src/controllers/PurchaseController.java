package controllers;

//import interfaces.VerifyDateField;
import interfaces.VerifyDateField;
import interfaces.VerifyTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import sun.applet.Main;
import util.InformationStorage;

import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import control.ModalPopup;

public class PurchaseController implements Initializable {

	
	// Payment toggle buttons
	@FXML private ToggleButton btn_pay_creditcard;
	@FXML private ToggleButton btn_pay_delivery;
	@FXML private ToggleButton btn_pay_bill;

	// Payment panes
	@FXML private HBox pane_pay_creditcard;
	@FXML private HBox pane_pay_bill_delivery;

	// Information about payment with bill or delivery
	@FXML private Text txt_pay_info;

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
		payment_mode_changed();

		ToggleGroup toggleGroup = new ToggleGroup();
		btn_pay_delivery.setToggleGroup(toggleGroup);
		btn_pay_creditcard.setToggleGroup(toggleGroup);
		btn_pay_bill.setToggleGroup(toggleGroup);

		toggleGroup.selectedToggleProperty().addListener((ov, toggle, new_toggle) -> {
            if (new_toggle == null) {
                toggle.setSelected(true);

            }
        });
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


	public BooleanBinding getBindings() {
		return  ccFieldBinding(txt_cardnr_1).and(
				ccFieldBinding(txt_cardnr_2)).and(
				ccFieldBinding(txt_cardnr_3)).and(
				ccFieldBinding(txt_cardnr_4)).and(
				cvvFieldBinding(txt_card_cvv)).and(
				dateFieldBinding(cb_card_year)).and(
				dateFieldBinding(cb_card_month));
	}

	private final VerifyTextField GOOD_CC = (txtField) -> (txtField.getText().length() == 4);
	private final VerifyTextField GOOD_CVV = (txtField) -> (txtField.getText().length() == 3);
	private final VerifyDateField GOOD_DATE = (choice) -> (choice.getSelectionModel().getSelectedIndex() > 0);

	private BooleanBinding ccFieldBinding(TextField textField) {
		BooleanBinding binding = createBinding(textField, GOOD_CC);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			} else {
				textField.getStyleClass().remove("bad-input");
			}
		});
		return binding;
	}

	private BooleanBinding cvvFieldBinding(TextField textField) {
		BooleanBinding binding = createBinding(textField, GOOD_CVV);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			} else {
				textField.getStyleClass().remove("bad-input");
			}
		});
		return binding;
	}

	private BooleanBinding dateFieldBinding(ChoiceBox choiceBox) {
		BooleanBinding binding = createChoiceBinding(choiceBox, GOOD_DATE);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				choiceBox.getStyleClass().add("bad-input");
			} else {
				choiceBox.getStyleClass().remove("bad-input");
			}
		});
		return binding;
	}

	private BooleanBinding createBinding(TextField textField, VerifyTextField verifyTextField) {
		return Bindings.createBooleanBinding(() -> verifyTextField.verify(textField), textField.textProperty());
	}

	private BooleanBinding createChoiceBinding(ChoiceBox choiceBox, VerifyDateField verifyDateField) {
		return Bindings.createBooleanBinding(() -> verifyDateField.verify(choiceBox), choiceBox.selectionModelProperty());
	}


	public boolean verifyInput(){
		if (btn_pay_creditcard.isSelected()){
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
			return true;
		}else{
			return true;
		}
	}



	public void payment_mode_changed(){
		pane_pay_creditcard.setVisible(false);
		pane_pay_bill_delivery.setVisible(false);



		if (btn_pay_creditcard.isSelected()) {
			pane_pay_creditcard.toFront();
			pane_pay_creditcard.setVisible(true);
			InformationStorage.setPaymentType(" och har betalat med kort.");
		}

		if (btn_pay_bill.isSelected()) {
			pane_pay_bill_delivery.toFront();
			pane_pay_bill_delivery.setVisible(true);
			txt_pay_info.setText("Du betalar med pappersfaktura. Den skickas hem till dig och ska betalas inom 30 dagar. Detta är inte bra för miljön. Tänk på träden Hjördis. Illa.");
			InformationStorage.setPaymentType(" och du får fakturan skickad till dig inom kort.");
		}

		if (btn_pay_delivery.isSelected()){
			pane_pay_bill_delivery.toFront();
			pane_pay_bill_delivery.setVisible(true);
			txt_pay_info.setText("Du betalar vid dörren när varorna har anlänt. Du kan betala med antingen kort eller kontanter.");
			InformationStorage.setPaymentType(" och betalningen sker vid leverans.");
		}
	}



}
