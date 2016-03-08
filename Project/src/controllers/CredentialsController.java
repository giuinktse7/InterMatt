package controllers;

import interfaces.TextFieldValidator;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import util.InformationStorage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import control.SmartTextField;

public class CredentialsController implements Initializable {

	private static IMatDataHandler db = IMatDataHandler.getInstance();
	
	@FXML
	private SmartTextField txtLastname;
	@FXML
	private SmartTextField txtFirstname;
	@FXML
	private SmartTextField txtSSN;
	@FXML
	private SmartTextField txtAdress;
	@FXML
	private SmartTextField txtPostalcode;
	@FXML
	private SmartTextField txtCity;
	@FXML
	private SmartTextField txtEmail;
	
	@FXML private Label lblErrorName, lblErrorLastname;

	@FXML
	private CheckBox cb_save_credentials;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtFirstname.initialize("Förnamn", "Vi behöver ditt namn för att slutföra köpet.");
		txtLastname.initialize("Efternamn", "Vi behöver ditt efternamn.");
		txtAdress.initialize("Gatuadress", "Vart ska vi leverera beställningen?");
		txtPostalcode.initialize("Postkod", "Fem siffror.");
		txtCity.initialize("Stad", "Vilken stad bor du i?");
		txtEmail.initialize("Email", "Vad är din mailadress?");
		txtSSN.initialize("Personnummer", "Ange ditt personnummer på formen ååmmddxxxx");
		
		// We need to store the firstname even if user choses to not save data. Used in receipt.
		// Not really pretty way to store the name but...
		txtFirstname.get().textProperty().addListener((observable, oldValue, newValue ) -> {
			InformationStorage.setFirtsName(newValue);
				});

		txtSSN.get().textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtSSN.setText(oldValue);
				newValue = oldValue;
			}
			if (newValue.length() > 10) {
				txtSSN.setText(txtSSN.getText().substring(0, 10));
			}
			// if (newValue.length() == 10){
			// select next button maybe..
			// }
		});

		txtPostalcode.get().textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtPostalcode.setText(oldValue);
				newValue = oldValue;
			}
			if (newValue.length() > 5) {
				txtPostalcode.setText(txtPostalcode.getText().substring(0, 5));
			}
			if (newValue.length() == 5) {
				txtCity.requestFocus();
			}
		});
	}

	public void save_user_data() {
		if (cb_save_credentials.isSelected()) {
			db.getCustomer().setLastName(txtLastname.getText());
			db.getCustomer().setFirstName(txtFirstname.getText());
			db.getCustomer().setPhoneNumber(txtSSN.getText());
			db.getCustomer().setAddress(txtAdress.getText());
			db.getCustomer().setPostCode(txtPostalcode.getText());
			db.getCustomer().setPostAddress(txtCity.getText());
			db.getCustomer().setEmail(txtEmail.getText());
			System.out.println("Saving user data");
		}
	}

	public void restore_user_data() {
		System.out.println("Restoring user data for: " + db.getCustomer().getFirstName());
		txtLastname.setText(db.getCustomer().getLastName());
		txtFirstname.setText(db.getCustomer().getFirstName());
		txtSSN.setText(db.getCustomer().getPhoneNumber());
		txtAdress.setText(db.getCustomer().getAddress());
		txtPostalcode.setText(db.getCustomer().getPostCode());
		txtCity.setText(db.getCustomer().getPostAddress());
		txtEmail.setText(db.getCustomer().getEmail());
	}

	private BooleanBinding emptyTextFieldBinding(TextField textField) {
		BooleanBinding binding = createBinding(textField, NON_EMPTY);
		addErrorHandling(binding, textField);
		return binding;
	}
	
	private BooleanBinding emptySmartTextFieldBinding(SmartTextField smartTextField) {
		BooleanBinding binding = createBinding(smartTextField.get(), NON_EMPTY);
		binding.addListener((obs, o, n) -> smartTextField.update(n));
		return binding;
	}
	
	/**
	 * 
	 * @param binding the bind
	 * @param textField the associated TextField
	 */
	private void addErrorHandling(BooleanBinding binding, TextField textField) {
		binding.addListener((obs, oldValue, newValue) -> {
			Label errorLabel = (Label) textField.getUserData();
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
				if (errorLabel != null)
					errorLabel.setVisible(true);
			} else {
				textField.getStyleClass().remove("bad-input");
				if (errorLabel != null)
					errorLabel.setVisible(false);
			}

		});
	}

	private BooleanBinding ssnFieldBinding(TextField textField) {
		BooleanBinding binding = createBinding(textField, GOOD_SSN);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			} else {
				textField.getStyleClass().remove("bad-input");
			}
		});
		return binding;
	}

	private BooleanBinding mailFieldBinding(TextField textField) {
		BooleanBinding binding = createBinding(textField, GOOD_MAIL);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			} else {
				textField.getStyleClass().remove("bad-input");
			}

		});
		return binding;
	}
	
	private BooleanBinding postalFieldBinding(TextField textField) {
		BooleanBinding binding = createBinding(textField, GOOD_POSTAL);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			} else {
				textField.getStyleClass().remove("bad-input");
			}

		});
		return binding;
	}
	
	private BooleanBinding validationBinding(SmartTextField smartTextField, TextFieldValidator validator) {
		BooleanBinding binding = createBinding(smartTextField.get(), validator);
		binding.addListener((obs, o, n) -> smartTextField.update(n));
		return binding;
	}
	
	private BooleanBinding postalCodeBinding(SmartTextField smartTextField) {
		BooleanBinding binding = createBinding(smartTextField.get(), GOOD_POSTAL);
		binding.addListener((obs, o, n) -> smartTextField.update(n));
		return binding;
	}
	
	private BooleanBinding validateEmailBinding(SmartTextField smartTextField) {
		BooleanBinding binding = createBinding(smartTextField.get(), GOOD_MAIL);
		binding.addListener((obs, o, n) -> smartTextField.update(n));
		return binding;
	}
	
	public BooleanBinding getBindings() {
		return validationBinding(txtLastname, NON_EMPTY).
				and(validationBinding(txtFirstname, NON_EMPTY)).
						and(validationBinding(txtSSN, GOOD_SSN)).
				and(validationBinding(txtAdress, NON_EMPTY)).
				and(validationBinding(txtPostalcode, GOOD_POSTAL)).
						and(validationBinding(txtCity, NON_EMPTY)).
						and(validationBinding(txtEmail, GOOD_MAIL));
	}

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	private final TextFieldValidator GOOD_MAIL = (txtField) -> (pattern.matcher(txtField.getText()).matches());
	private final TextFieldValidator GOOD_POSTAL = (txtField) -> (txtField.getText().length() == 5);
	private final TextFieldValidator GOOD_SSN = (txtField) -> (txtField.getText().length() == 10);
	// || Integer.parseInt(txtField.getText().substring(0, 2)) <= 97)
	private final TextFieldValidator NON_EMPTY = (txtField) -> (!txtField.textProperty().get().equals(""));

	private BooleanBinding createBinding(TextField textField, TextFieldValidator verifyTextField) {
		return Bindings.createBooleanBinding(() -> verifyTextField.verify(textField), textField.textProperty());
	}

}
