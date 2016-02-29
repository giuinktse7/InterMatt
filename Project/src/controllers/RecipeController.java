package controllers;

import interfaces.Action;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import se.chalmers.ait.dat215.project.IMatDataHandler;
import util.ModalPopup;

import java.net.URL;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {

    @FXML private ModalPopup bottomPane;
    @FXML private Button closeButton;
    @FXML private Pane contentPane;
    @FXML private VBox tableBox;


    private IMatDataHandler db = IMatDataHandler.getInstance();

    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setCloseAction(Action c) {
        closeButton.setOnAction(e -> bottomPane.close());
    }

}