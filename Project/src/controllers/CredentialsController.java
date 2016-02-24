package controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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

}
