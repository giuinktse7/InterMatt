package control;

import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import se.chalmers.ait.dat215.project.Product;

public class AttributeTextField extends TextField {

	private static final String unitsWithDouble = "'kg', 'hg', 'g', 'l'";
	private double maxValue;
	private double minValue = 0;
	private boolean allowDoubles;

	public AttributeTextField(Product product, double maxValue) {
		this.getStyleClass().add("attribute-text-field");
		allowDoubles = unitsWithDouble.contains("'" + product.getUnitSuffix() + "'");
		this.maxValue = maxValue;
		setMinWidth(25);
		
		//Maybe not use? Might not be necessary..
		addEventFilter(KeyEvent.KEY_TYPED, numeric_Validation(5));
	}

	private boolean isAllowedValue(String value) {
		if (allowDoubles)
			return isDouble(value);
		else
			return isInteger(value);
	}

	private boolean lessThanMax(String value) {
		if (allowDoubles)
			return Double.parseDouble(value) < maxValue;
		else
			return Integer.parseInt(value) < maxValue;
	}

	private boolean moreThanMin(String value) {
		if (allowDoubles)
			return Double.parseDouble(value) >= minValue;
		else
			return Integer.parseInt(value) >= minValue;
	}

	public void replaceText(int start, int end, String text) {
		String oldValue = getText();
		if ((!text.matches("[A-Za-z]") && !text.matches("[-\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+"))
				|| (text.equals(".") && allowDoubles)) {
			super.replaceText(start, end, text);
		}
		if (isAllowedValue(getText()) && !lessThanMax(getText())) {
			setText(allowDoubles ? Double.toString(maxValue) : Integer.toString((int) maxValue));
			positionCaret(oldValue.length());
		}
		if (isAllowedValue(getText()) && !moreThanMin(getText())) {
			setText("1");
			positionCaret(oldValue.length());
		}
	}

	public void replaceSelection(String text) {
		String oldValue = getText();
		if ((!text.matches("[A-Za-z]") && !text.matches("[-\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+")
				|| (!isDouble(text) && text.charAt(text.length() - 1) != '.')) || (text.equals(".") && allowDoubles)) {
			super.replaceSelection(text);
		}
		if (isAllowedValue(getText()) && !lessThanMax(getText())) {
			setText(allowDoubles ? Double.toString(maxValue) : Integer.toString((int) maxValue));
			positionCaret(oldValue.length());
		}
		if (isAllowedValue(getText()) && !moreThanMin(getText())) {
			setText("1");
			positionCaret(oldValue.length());
		}
	}

	public EventHandler<KeyEvent> numeric_Validation(final Integer max_Lengh) {
		return e -> {
			TextField textField = (TextField) e.getSource();
			if (e.getCharacter().equals(" "))
				e.consume();
			
			
			else if (e.getCharacter().matches("[0-9.]")) {
				if (textField.getText().contains(".") && e.getCharacter().matches("[.]")) {
					e.consume();
				} else if (textField.getText().length() == 0 && e.getCharacter().matches("[.]")) {
					e.consume();
				}
			} else {
				e.consume();
			}
		};
	}

	private static boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}
}