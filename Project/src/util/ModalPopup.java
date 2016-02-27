package util;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ModalPopup extends FlowPane {

	BooleanProperty mouseInsideContent = new SimpleBooleanProperty();
	
	private static Node mainProgramContent;
	private static StackPane container;
	private Animation appearAnimation, disappearAnimation;
	
	public ModalPopup(Node content) {
		if (content != null)
			this.getChildren().add(content);
		
		setDefaultFade();
		setDropShadow();
	}
	
	public ModalPopup() {
		this(null);
	}
	
	private void setDropShadow() {
		DropShadow dropShadow = new DropShadow();
		 dropShadow.setRadius(5.0);
		 dropShadow.setOffsetX(3.0);
		 dropShadow.setOffsetY(3.0);
		 dropShadow.setColor(Color.color(0.4, 0.5, 0.5));
		 setEffect(dropShadow);
	}
	
	/** If not set, uses a default fade */
	public void setAppearAnimation(Animation animation) {
		this.appearAnimation = animation;
	}
	
	/** If not set, uses a default fade */
	public void setDisappearAnimation(Animation animation) {
		this.disappearAnimation = animation;
	}
	
	public void show() {
		setMaxWidth(Double.MAX_VALUE);
		setMaxHeight(Double.MAX_VALUE);
		
		if (!container.getChildren().contains(this)) {
		container.getChildren().add(0, this);
		}
		
		prefWidth(container.getPrefWidth());
		prefHeight(container.getPrefHeight());
		toFront();
		appearAnimation.play();
		
		/*for ()
		Bindings.createBooleanBinding(() -> {  }, );*/
		
		this.setOnMouseClicked(e -> close());
	}
	
	/** Closes the popup. */
	public void close() {
		disappearAnimation.play();
		disappearAnimation.setOnFinished(event -> mainProgramContent.toFront());
	}
	
	/** Equivalent to <code>close()</code>. */
	public void hide() {
		close();
	}
	
	/** should only be called at program startup. */
	public static void initialize(StackPane container, Pane mainContent) {
		ModalPopup.container = container;
		ModalPopup.mainProgramContent = mainContent;
	}
	
	private void setDefaultFade() {
		appearAnimation = new FadeTransition(Duration.millis(300), this);
		FadeTransition fade = (FadeTransition) appearAnimation;
		fade.setFromValue(0);
		fade.setToValue(1);
		
		disappearAnimation = new FadeTransition(Duration.millis(300), this);
		fade = (FadeTransition) disappearAnimation;
		fade.setFromValue(1);
		fade.setToValue(0);
	}
}
