package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class CredentialsController implements Initializable {

	@FXML private TextField txtLastname;
	@FXML private TextField txtFirstname;
	@FXML private TextField txtSSN;
	@FXML private TextField txtAdress;
	@FXML private TextField txtPostalcode;
	@FXML private TextField txtCity;
	@FXML private TextField txtEmail;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public boolean verifyInput(){
		if (txtLastname.getText().isEmpty()) return false;
		if (txtFirstname.getText().isEmpty()) return false;
		if (txtSSN.getText().length() != 10 ||
				Integer.parseInt(txtSSN.getText().substring(0, 2)) <= 97) return false;
		if (txtAdress.getText().isEmpty()) return false;
		if (txtPostalcode.getText().length() != 5) return false;
		if (txtCity.getText().isEmpty()) return false;
		
		if (Pattern.matches("[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}", txtEmail.getText())) return false;
		return true;
	}
	
	private BooleanBinding emptyTextFieldBinding(TextField textField ) {
		BooleanBinding binding = Bindings.createBooleanBinding(() -> !textField.textProperty().get().equals(""), textField.textProperty());
		binding.addListener((obs, oldValue, newValue) -> { 
			if (!newValue) {
				textField.setPromptText("Efternamn tack!");
				textField.setStyle("-fx-prompt-text-fill: rgba(255, 64, 64, 0.6)");
			} else {
				textField.setPromptText("");
			}
			
		});
		return binding;
	}
	
	public BooleanBinding[] getBindings() {
		return new BooleanBinding[] { emptyTextFieldBinding(txtLastname), emptyTextFieldBinding(txtFirstname) };
	}

}
