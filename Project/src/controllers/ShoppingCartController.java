package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import control.ProductHBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import util.ShoppingCartHandler;

public class ShoppingCartController implements Initializable {

	@FXML private Button gotoShoppingListButton;
	@FXML private Button saveShoppingListButton;
	

	@FXML private Label lblTotalCost;
	@FXML private ListView<ProductHBox> cart;
	
	private IMatDataHandler db = IMatDataHandler.getInstance();
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		cart.getProperties().put("selectOnFocusGain", false);
		// introduced between 8u20 and 8u40b7
		// with this, testfailures back to normal
		cart.getProperties().put("selectFirstRowByDefault", false);
		
		ShoppingCartHandler.getInstance().setCart(cart);
		ShoppingCartHandler.getInstance().passLabel(lblTotalCost);
		
		cart.setOnDragOver(event -> {
			if (event.getGestureSource() != cart && event.getDragboard().hasString()) {
				event.acceptTransferModes(TransferMode.MOVE);
			}
			
			event.consume();
		});
		
		cart.setOnDragEntered(event -> {
			if (event.getGestureSource() != cart && event.getDragboard().hasString())
				cart.getStyleClass().add("drop-it-here");
			event.consume();
		});
		
		cart.setOnDragExited(event -> {
			event.consume();
		});
		
		cart.setOnDragDropped(event -> {
			Dragboard drag = event.getDragboard();
            boolean success = false;
            if (drag.hasString()) {
                success = true;
                
                String[] info = drag.getString().split(" ");
                int productID = Integer.parseInt(info[0]);
                double quantity = Double.parseDouble(info[1]);
                ShoppingCartHandler.getInstance().addProduct(db.getProduct(productID), quantity);
            }
            /* let the source know whether the string was successfully 
             * transferred and used */
            event.setDropCompleted(success);
            
            event.consume();
		});
	}

	public Button getShoppingListButton() {
		return gotoShoppingListButton;
	}
	
	public Button getSaveListButton(){
		return saveShoppingListButton;
	}
}
