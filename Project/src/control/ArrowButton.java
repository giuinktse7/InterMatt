package control;

import com.sun.javafx.scene.traversal.Direction;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArrowButton extends Button {
	
	private static final int ENABLED = 0;
	private static final int DISABLED = 1;
	
	private ImageView[] images = new ImageView[2];
	
	public ArrowButton() {
		getStyleClass().add("navigation-button");
		disabledProperty().addListener((obs, oldValue, newValue) -> updateImage());
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
	

	
	public void enable() {
		this.setDisable(false);
	}
	
	public void disable() {
		this.setDisable(true);
	}
}
