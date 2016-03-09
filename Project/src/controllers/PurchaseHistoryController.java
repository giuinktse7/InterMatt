package controllers;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

import control.ItemInHistoryBox;
import control.ModalPopup;
import control.OrderOverviewBox;
import interfaces.Action;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import se.chalmers.ait.dat215.project.Order;
import se.chalmers.ait.dat215.project.ShoppingItem;

public class PurchaseHistoryController implements Initializable {
	
	@FXML private ModalPopup bottomPane;
	@FXML private Pane contentPane;
	@FXML private ListView<Node> productListView;
	@FXML private ListView<Node> ordersList;
	@FXML private Label lblTotalPrice;
	@FXML private Label lblDate;
	@FXML private Label lblOrderID;
	
	
	private IMatDataHandler db = IMatDataHandler.getInstance();
	
	public void initialize(URL location, ResourceBundle resources) {
		ordersList.setOrientation(Orientation.HORIZONTAL);
	}
	
	public void update() {
		
		List<Order> orders = db.getOrders();
		
		Collections.sort(orders, (o1, o2) -> {
			return (int) (o2.getDate().getTime() - o1.getDate().getTime());
		});
		
		//Populate the order overview list
		ordersList.getItems().clear();

		// Setup the first vertical separator
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		
		orders.forEach(order -> {
			OrderOverviewBox orderBox = new OrderOverviewBox(order);
			//orderBox.setOnMouseClicked(e -> displayOrder(orderBox));
			ordersList.getItems().add(orderBox);
			});
		
		ordersList.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
			displayOrder(((OrderOverviewBox) n));
		});
		
		//Show the first order if there is one
		if (!ordersList.getItems().isEmpty()) {
			OrderOverviewBox box = new OrderOverviewBox(((OrderOverviewBox) ordersList.getItems().get(0)).getOrder());
			displayOrder(box);
			ordersList.getSelectionModel().select(0);
		}
	}
	
	private void displayOrder(OrderOverviewBox orderBox) {
		
		List<Node> productBoxes = orderBox.getProductBoxes();
		if (!productListView.getItems().equals(productBoxes))
			productListView.getItems().setAll(productBoxes);
		
		lblDate.setText(orderBox.getDate());
		lblTotalPrice.setText(String.format("%.2f:-", orderBox.getTotalPrice()).replace('.', ','));
		lblOrderID.setText(String.format("Order ID: %d", orderBox.getOrder().getOrderNumber()));
	}
	
	public ModalPopup getRoot() {
		return this.bottomPane;
	}
}