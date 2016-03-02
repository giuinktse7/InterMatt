package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import util.ShoppingCartHandler;

import java.net.URL;
import java.util.ResourceBundle;

import control.ModalPopup;

public class RecipeController implements Initializable {

    @FXML private ModalPopup bottomPane;
    @FXML private Button closeButton;
    @FXML private Pane contentPane;
    @FXML private VBox tableBox;
	
	public void initialize(URL location, ResourceBundle resources) {
		setCloseAction();
	}

	private void setCloseAction() {
		bottomPane.setOnExit(() -> {
			MainController.get().finishPurchase();
			ShoppingCartHandler.getInstance().clearCart();
		});
		closeButton.setOnAction(e -> {
			bottomPane.close();
		});
	}

}