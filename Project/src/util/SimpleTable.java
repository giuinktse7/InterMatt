package util;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Order;

public class SimpleTable {

	private final float PERCENTAGE_WIDTHS[] = {0.7f, 0.3f};
	
	private VBox node;
	private IMatDataHandler db = IMatDataHandler.getInstance();
	
	public SimpleTable(VBox node, String... titles) {
		this.node = node;
		
		if (titles.length > 0)
			setupTitles(titles);
	}
	
	public void addOrder(Order order) {
		node.getChildren().add(new OrderItem(order, PERCENTAGE_WIDTHS));
	}
	
	private void setupTitles(String... titles) {
		HBox box = new HBox();
		
		for(int i = 0; i < titles.length; ++i) {
			final int k = i;
			String title = titles[i];
			Label l = new Label(title);
			box.getChildren().add(l);
			if (i < PERCENTAGE_WIDTHS.length) {
				box.widthProperty().addListener((obs, oldValue, newValue) -> l.setPrefWidth(newValue.doubleValue() * PERCENTAGE_WIDTHS[k]));
			}
		}
		
		node.getChildren().add(box);
		
	}
}
