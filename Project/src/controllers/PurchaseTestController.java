package controllers;

import interfaces.PaymentMethod;
import interfaces.VerifyDateField;
import interfaces.TextFieldValidator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import util.PaymentProperty;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import control.ModalPopup;

public class PurchaseTestController implements Initializable {
	
	
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
	
	PaymentProperty paymentProperty = new PaymentProperty(PaymentMethod.DELIVERY);


	// Credit card expiration year and month
	@FXML private ChoiceBox<String> cb_card_month;
	private List<String> months = Arrays.asList("-- Månad --", "Januari", "Februari", "Mars", "April", "Maj", "Juni", "Juli", "Augusti", "September", "Oktober", "November", "December");
	@FXML private ChoiceBox<String> cb_card_year;
	private List<String> years = Arrays.asList("-- År --", "2016", "2017", "2018");
	@FXML private ChoiceBox<String> cb_delivery_date;
	@FXML private ChoiceBox<String> cb_delivery_time;
	
	// Credit card CVV fields
	@FXML private TextField txt_card_cvv;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setupInputConstraints();
		//payment_mode_changed();
		
		ToggleGroup toggleGroup = new ToggleGroup();
		/*btn_pay_delivery.setToggleGroup(toggleGroup);
		btn_pay_creditcard.setToggleGroup(toggleGroup);
		btn_pay_bill.setToggleGroup(toggleGroup);*/

		toggleGroup.selectedToggleProperty().addListener((ov, toggle, new_toggle) -> {
            if (new_toggle == null) {
                toggle.setSelected(true);

            }
        });
		
		setDeliveryDates();
		
		/** Most simple way to save this stuff */
		cb_delivery_date.selectionModelProperty().addListener(e -> InformationStorage.setDelivery(getDeliveryString()));
		cb_delivery_time.selectionModelProperty().addListener(e -> InformationStorage.setDelivery(getDeliveryString()));
		InformationStorage.setDelivery(getDeliveryString());
	}
	
	private final BooleanBinding BILL_SELECTED = selectedPaymentBinding(PaymentMethod.BILL);
	private final BooleanBinding CREDITCARD_SELECTED = selectedPaymentBinding(PaymentMethod.CREDITCARD);
	private final BooleanBinding DELIVERY_SELECTED = selectedPaymentBinding(PaymentMethod.DELIVERY);
	
	public void setDeliveryDates(){
		List<String> dates = new ArrayList<String>();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, 2);
		
		for (int i = 0; i < 7; i++){
			c.add(Calendar.DATE, 1);
			if (c.getTime().getDay() == 0)
				c.add(Calendar.DATE, 1);
			
			
			String days[] = {"Söndag", "Måndag", "Tisdag", "Onsdag", "Torsdag", "Fredag", "Lördag"};
			String dayOfWeek = days[c.getTime().getDay()];
			
			String str = dayOfWeek + " " + c.getTime().getDate()+"/"+(c.getTime().getMonth() + 1);
			dates.add(str);
			System.out.println(str);
		}
		
		cb_delivery_date.getItems().addAll(dates);
		cb_delivery_date.setValue(dates.get(0));
		List<String> times = Arrays.asList("Morgon (09:00 - 10:30)", "Förmiddag (10:30 - 12:00)", "Eftermiddag (13:00 - 15:00)");
		cb_delivery_time.getItems().addAll(times);
		cb_delivery_time.setValue(times.get(0));
	}
	
	public String getDeliveryString(){
		return cb_delivery_date.getSelectionModel().getSelectedItem() + " " + cb_delivery_time.getSelectionModel().getSelectedItem(); 
	}


	public BooleanBinding getBindings() {
		//If paying by credit card, ensure these things to proceed
		BooleanBinding creditCardRequirements = CREDITCARD_SELECTED.and(ccFieldBinding(txt_cardnr_1)).and(
				ccFieldBinding(txt_cardnr_2)).and(
				ccFieldBinding(txt_cardnr_3)).and(
				ccFieldBinding(txt_cardnr_4)).and(
				cvvFieldBinding(txt_card_cvv)).and(
				//dateFieldBinding(cb_card_year)).and(
				//dateFieldBinding(cb_card_month));
				choiceBoxBinding(cb_card_month));
		
		//If paying by bill, ensure these things to proceed
		BooleanBinding billRequirements = BILL_SELECTED;
		
		//If paying at delivery, ensure these things to proceed
		BooleanBinding deliveryRequirements = DELIVERY_SELECTED;
		
		return creditCardRequirements.or(billRequirements).or(deliveryRequirements);
		
	}

	private final TextFieldValidator GOOD_CC = (txtField) -> (txtField.getText().length() == 4);
	private final TextFieldValidator GOOD_CVV = (txtField) -> (txtField.getText().length() == 3);
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
	
	private BooleanBinding choiceBoxBinding(ChoiceBox<String> choiceBox) {
		BooleanBinding binding = Bindings.createBooleanBinding(() -> choiceBox.getSelectionModel().selectedIndexProperty().get() > 0, choiceBox.getSelectionModel().selectedIndexProperty());
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				choiceBox.getStyleClass().add("bad-input");
			} else {
				choiceBox.getStyleClass().remove("bad-input");
			}
		});
		return binding;
	}

	private BooleanBinding createBinding(TextField textField, TextFieldValidator verifyTextField) {
		return Bindings.createBooleanBinding(() -> verifyTextField.verify(textField), textField.textProperty());
	}

	private BooleanBinding createChoiceBinding(ChoiceBox choiceBox, VerifyDateField verifyDateField) {
		return Bindings.createBooleanBinding(() -> verifyDateField.verify(choiceBox), choiceBox.selectionModelProperty());
	}




	public void payment_mode_changed(){
		pane_pay_creditcard.setVisible(false);
		pane_pay_bill_delivery.setVisible(false);



		if (btn_pay_creditcard.isSelected()) {
			pane_pay_creditcard.toFront();
			pane_pay_creditcard.setVisible(true);
			InformationStorage.setPaymentType(" och har betalat med kort.");
			paymentProperty.setValue(PaymentMethod.CREDITCARD);
		}

		if (btn_pay_bill.isSelected()) {
			pane_pay_bill_delivery.toFront();
			pane_pay_bill_delivery.setVisible(true);
			txt_pay_info.setText("Du betalar med pappersfaktura. Du får fakturan med dina varor och den ska betalas inom 30 dagar efter detta.");
			InformationStorage.setPaymentType(" och du får fakturan med dina varor.");
			paymentProperty.setValue(PaymentMethod.BILL);
		}

		if (btn_pay_delivery.isSelected()){
			pane_pay_bill_delivery.toFront();
			pane_pay_bill_delivery.setVisible(true);
			txt_pay_info.setText("Du betalar vid dörren när varorna har anlänt. Du kan betala med antingen kort eller kontanter.");
			InformationStorage.setPaymentType(" och betalningen sker vid leverans.");
			paymentProperty.setValue(PaymentMethod.DELIVERY);
		}
	}
	
	/** Creates a binding that holds whether or not PaymentMethod <code>paymentMethod</code> is selected. */
	private BooleanBinding selectedPaymentBinding(PaymentMethod paymentMethod) {
		BooleanProperty property = new SimpleBooleanProperty(true);
		paymentProperty.addListener((obs, o, n) -> property.setValue(n.equals(paymentMethod)));
		return Bindings.createBooleanBinding(() -> property.get(), property);
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
}
