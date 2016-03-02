package control;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.scene.traversal.Direction;

import javafx.beans.value.ChangeListener;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.ContentView;

public class ArrowButton extends Button {
	
	private static final int ENABLED = 0;
	private static final int DISABLED = 1;
	
	/** Should not listen to this object, but rather to ContentViews. Used to make sure prev/next works as intended. */
	private List<ChangeListener<? super Boolean>> helpListeners = new ArrayList<ChangeListener<? super Boolean>>();

	
	private ImageView[] images = new ImageView[2];
	
	public ArrowButton() {
		getStyleClass().add("navigation-button");
		disabledProperty().addListener((obs, oldValue, newValue) -> {System.out.println("Updating img!"); updateImage(); });
	}
	
	public void setDirection(Direction Dir) {
		String path = "";
		
		if (Dir == Direction.LEFT)
			path = "resources/leftarrow";
		else
			path = "resources/rightarrow";
		
		setEnabledImage(String.format("%s.png", path), 90);
		setDisabledImage(String.format("%s_disabled.png", path), 90);
		
		updateImage();
	}
	
	public void setEnabledImage(String path, int prefWidth) {
		Image image = new Image(path, prefWidth, 64, true, true);
		images[ENABLED] = new ImageView(image);
	}
	
	public void setDisabledImage(String path, int prefWidth) {
		Image image = new Image(path, prefWidth, 64, true, true);
		images[DISABLED] = new ImageView(image);
	}
	
	private void updateImage() {
		ImageView graphic = isDisabled() ? images[DISABLED] : images[ENABLED];
		
		setGraphic(graphic);
	}
	
	/** Should not listen to this object, but rather to ContentViews. Used to make sure prev/next works as intended. */
	public List<ChangeListener<? super Boolean>> getHelpListeners() {
		return this.helpListeners;
	}
	
	public void enable() {
		this.setDisable(false);
	}
	
	public void disable() {
		this.setDisable(true);
	}
}
