package controllers;

import interfaces.VerifyTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import se.chalmers.ait.dat215.project.IMatDataHandler;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CredentialsController implements Initializable {

	private static IMatDataHandler db = IMatDataHandler.getInstance();

	@FXML private TextField txtLastname;
	@FXML private TextField txtFirstname;
	@FXML private TextField txtSSN;
	@FXML private TextField txtAdress;
	@FXML private TextField txtPostalcode;
	@FXML private TextField txtCity;
	@FXML private TextField txtEmail;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txtLastname.setText(db.getCustomer().getLastName());
		txtFirstname.setText(db.getCustomer().getFirstName());
		txtSSN.setText(db.getCustomer().getPhoneNumber());
		txtAdress.setText(db.getCustomer().getAddress());
		txtPostalcode.setText(db.getCustomer().getPostCode());
		txtCity.setText(db.getCustomer().getPostAddress());


		txtPostalcode.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtPostalcode.setText(oldValue);
				newValue = oldValue;
			}
			if (newValue.length() > 5){
				txtPostalcode.setText(txtPostalcode.getText().substring(0,5));
			}
			if (newValue.length() == 5){
				txtCity.requestFocus();
			}
		});

		txtSSN.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\d*")) {
				txtSSN.setText(oldValue);
				newValue = oldValue;
			}
			if (newValue.length() > 10){
				txtSSN.setText(txtSSN.getText().substring(0,10));
			}
			if (newValue.length() == 10){
				// select next button maybe..
			}
		});

	}

	public void save_user_data(){
		db.getCustomer().setLastName(txtLastname.getText());
		db.getCustomer().setFirstName(txtFirstname.getText());
		db.getCustomer().setPhoneNumber(txtSSN.getText());
		db.getCustomer().setAddress(txtAdress.getText());
		db.getCustomer().setPostCode(txtPostalcode.getText());
		db.getCustomer().setPostAddress(txtCity.getText());
	}



	private BooleanBinding emptyTextFieldBinding(TextField textField ) {
		BooleanBinding binding = createBinding(textField, NON_EMPTY );
		binding.addListener((obs, oldValue, newValue) -> { 
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			} else {
				textField.getStyleClass().remove("bad-input");
			}
			
		});
		return binding;
	}

	private BooleanBinding ssnFieldBinding(TextField textField){
		BooleanBinding binding = createBinding(textField, GOOD_SSN);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			}else{
				textField.getStyleClass().remove("bad-input");
			}

		});
		return binding;
	}

	private BooleanBinding mailFieldBinding(TextField textField){
		BooleanBinding binding = createBinding(textField, GOOD_MAIL);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			}else{
				textField.getStyleClass().remove("bad-input");
			}

		});
		return binding;
	}

	private BooleanBinding postalFieldBinding(TextField textField){
		BooleanBinding binding = createBinding(textField, GOOD_POSTAL);
		binding.addListener((obs, oldValue, newValue) -> {
			if (!newValue) {
				textField.getStyleClass().add("bad-input");
			}else{
				textField.getStyleClass().remove("bad-input");
			}

		});
		return binding;
	}


	
	public BooleanBinding[] getBindings() {
		return new BooleanBinding[] { emptyTextFieldBinding(txtLastname), emptyTextFieldBinding(txtFirstname),
				ssnFieldBinding(txtSSN), emptyTextFieldBinding(txtAdress), postalFieldBinding(txtPostalcode),
				emptyTextFieldBinding(txtCity)
				, mailFieldBinding(txtEmail)
		};
	}

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"  + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern = Pattern.compile(EMAIL_PATTERN);

	private final VerifyTextField GOOD_MAIL = (txtField) -> ( pattern.matcher(txtField.getText()).matches()  );
	private final VerifyTextField GOOD_POSTAL = (txtField) -> ( txtField.getText().length() == 5 );
	private final VerifyTextField GOOD_SSN = (txtField) -> (txtField.getText().length() == 10 );
	//|| Integer.parseInt(txtField.getText().substring(0, 2)) <= 97)
	private final VerifyTextField NON_EMPTY = (txtField) -> ( !txtField.textProperty().get().equals("")) ;



	private BooleanBinding createBinding (TextField textField, VerifyTextField verifyTextField){
		return Bindings.createBooleanBinding(()->verifyTextField.verify(textField), textField.textProperty() );
	}


}
