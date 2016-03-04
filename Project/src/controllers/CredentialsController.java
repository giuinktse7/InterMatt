package controllers;

import interfaces.VerifyTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CredentialsController implements Initializable {

	@FXML private TextField txtLastname;
	@FXML private TextField txtFirstname;
	@FXML private TextField txtSSN;
	@FXML private TextField txtAdress;
	@FXML private TextField txtPostalcode;
	@FXML private TextField txtCity;
	@FXML private TextField txtEmail;

	public String user_last = "", user_first = "", user_SSN = "", user_adress = "", user_postal = "", user_city= "", user_email= "";


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
//
//	public boolean verifyInput(){
//		if (txtLastname.getText().isEmpty()) return false;
//		if (txtFirstname.getText().isEmpty()) return false;
//		if (txtSSN.getText().length() != 10 ||
//				Integer.parseInt(txtSSN.getText().substring(0, 2)) <= 97) return false;
//		if (txtAdress.getText().isEmpty()) return false;
//		if (txtPostalcode.getText().length() != 5) return false;
//		if (txtCity.getText().isEmpty()) return false;
//		if (Pattern.matches("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}", txtEmail.getText())) return false;
//
//		user_last = txtLastname.getText();
//		user_first = txtFirstname.getText();
//		user_SSN = txtSSN.getText();
//		user_adress =txtAdress.getText();
//		user_postal = txtPostalcode.getText();
//		user_city = txtCity.getText();
//		user_email = txtEmail.getText();
//
//		System.out.println("USER LAST: " + user_last);
//
//
//		return true;
//	}

	public void save_user_data(){

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
	
	public BooleanBinding[] getBindings() {
		return new BooleanBinding[] { emptyTextFieldBinding(txtLastname), emptyTextFieldBinding(txtFirstname),
				emptyTextFieldBinding(txtSSN), emptyTextFieldBinding(txtAdress), emptyTextFieldBinding(txtPostalcode),
				emptyTextFieldBinding(txtCity), emptyTextFieldBinding(txtEmail)
		};
	}

	private final VerifyTextField NON_EMPTY = (txtField) -> ( !txtField.textProperty().get().equals("")) ;

	private BooleanBinding createBinding (TextField textField, VerifyTextField verifyTextField){
		return Bindings.createBooleanBinding(()->verifyTextField.verify(textField), textField.textProperty() );
	}


}
