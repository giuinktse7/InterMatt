package util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import se.chalmers.ait.dat215.project.Order;

public class OrderItem extends HBox {
	
	public OrderItem(Order o, float... percentageWidths) {
		Label lblDate = new Label(o.getDate().toString());
		Label lblOrderNumber = new Label("" + o.getOrderNumber());
		
		this.widthProperty().addListener((obs, oldValue, newValue) -> lblDate.setPrefWidth(newValue.doubleValue() * percentageWidths[0]));
		this.widthProperty().addListener((obs, oldValue, newValue) -> lblOrderNumber.setPrefWidth(newValue.doubleValue() * percentageWidths[1]));
		add(lblDate);
		add(lblOrderNumber);
	}
	
	private void add(Node n) {
		this.getChildren().add(n);
	}
}
