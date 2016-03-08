package control;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class SmartTextField extends AnchorPane {

	Pane validationRect;
	TextField textField;
	Label errorLabel;
	Polygon triangle;
	Label asterix;
	
	String normalColor = "#90cf7d";
	String errorColor = "#e55933";
	
	//Should be the same as prefHeight for the SmartTextFields in FXML.
	private final double defaultTextFieldHeight = 40;
	
	public SmartTextField() {
		super();
		validationRect = new Pane();
		//validationRect.setMouseTransparent(true);
		validationRect.setStyle("-fx-background-color: " + normalColor);
		validationRect.setPrefWidth(7);
		validationRect.setMinWidth(7);
		validationRect.setMaxWidth(7);
		AnchorPane.setLeftAnchor(validationRect, 0d);
		AnchorPane.setTopAnchor(validationRect, 0d);
		
		textField = new TextField();
		textField.getStyleClass().add("smart-text-field");
		textField.prefWidth(this.getPrefWidth());
		textField.prefHeight(this.getPrefHeight());
		AnchorPane.setTopAnchor(textField, 0d);
		AnchorPane.setLeftAnchor(textField, validationRect.getPrefWidth());
		AnchorPane.setRightAnchor(textField, 0d);
		
		errorLabel = new Label("N/A");
		errorLabel.setVisible(false);
		errorLabel.getStyleClass().add("error-label");
		AnchorPane.setTopAnchor(errorLabel, 45d);
		AnchorPane.setLeftAnchor(errorLabel, 7d);
		
		triangle = new Polygon(0, 0, 28, 0, 28, defaultTextFieldHeight - 14);
		triangle.setMouseTransparent(true);
		triangle.setVisible(false);
		AnchorPane.setTopAnchor(triangle, 0d);
		AnchorPane.setRightAnchor(triangle, 0d);
		triangle.setFill(Color.rgb(223, 58, 58, 1));
		
		asterix = new Label("*");
		asterix.setVisible(false);
		asterix.setMouseTransparent(true);
		asterix.getStyleClass().add("asterix-label");
		AnchorPane.setTopAnchor(asterix, -6d);
		AnchorPane.setRightAnchor(asterix, 2d);
		
		//Manage the validation rectangle dimensions
		textField.heightProperty().addListener((obs, o, n) -> {
				validationRect.setPrefHeight(n.doubleValue());
			
		});
		this.widthProperty().addListener((obs, o, n) -> textField.setPrefWidth(n.doubleValue() - validationRect.getPrefWidth()));
		
		getChildren().addAll(textField, validationRect, errorLabel, triangle, asterix);
	}
	
	public void initialize(double height, String prompt, String errorText) {
		textField.setPromptText(prompt);
		textField.setPrefHeight(height);
		setErrorText(errorText);
	}
	
	public void initialize(String prompt, String errorText) {
		initialize(defaultTextFieldHeight, prompt, errorText);
	}
	
	/** Gets the text of the wrapped TextField */
	public String getText() {
		return this.textField.getText();
	}
	
	/** Returns the wrapped TextField */
	public TextField get() {
		return this.textField;
	}
	
	public void setErrorText(String text) {
		this.errorLabel.setText(text);
	}
	
	/** Sets the text of the wrapped TextField */
	public void setText(String text) {
		this.textField.setText(text);
	}
	
	public void update(boolean valid) {
		if (valid) {
			errorLabel.setVisible(false);
			validationRect.setStyle("-fx-background-color: " + normalColor);
			textField.getStyleClass().remove("bad-input");
			textField.getStyleClass().add("good-input");
			asterix.setVisible(false);
			triangle.setVisible(false);
			
		} else {
			errorLabel.setVisible(true);
			validationRect.setStyle("-fx-background-color: " + errorColor);
			textField.getStyleClass().remove("good-input");
			textField.getStyleClass().add("bad-input");
			asterix.setVisible(true);
			triangle.setVisible(true);
		}
	}
}
