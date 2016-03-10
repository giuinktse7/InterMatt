package control;

import interfaces.Action;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

public class ModalPopup extends AnchorPane {

	private static StackPane motherPane;
	private FlowPane content;
	private static Node mainProgramContent;
	private static StackPane container;
	private Animation appearAnimation, disappearAnimation, appearShadeAnimation, disappearShadeAnimation;

	private static Pane shade;

	// TODO Temporary. Replace with a better solution.
	private Action exitAction = null;

	private boolean first = true;

	public ModalPopup(FlowPane content) {
		if (content != null) {
			this.getChildren().add(content);
			this.content = content;
			Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
		}
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
		if (first) {
			setDefaultFade();
			
			container.getChildren().clear();
			container.getChildren().add(this);

			setMaxWidth(Double.MAX_VALUE);
			setMaxHeight(Double.MAX_VALUE);

			this.content = (FlowPane) getChildren().get(0);
			AnchorPane.setLeftAnchor(content, 0d);
			AnchorPane.setRightAnchor(content, 0d);
			AnchorPane.setTopAnchor(content, 0d);
			AnchorPane.setBottomAnchor(content, 0d);

			// Setup the exit button
			AnchorPane topPane = ((AnchorPane) ((Pane) content.getChildren().get(0)).getChildren().get(0));
			Button exitButton = new Button();
			exitButton.setStyle("-fx-background-color: transparent;");
			exitButton.getStyleClass().add("modal-popup-exit-button");

			Image exitImage = new Image("resources/exit.png", 36, 36, true, true);
			exitButton.setGraphic(new ImageView(exitImage));

			AnchorPane.setRightAnchor(exitButton, 7d);
			AnchorPane.setTopAnchor(exitButton, 0d);
			topPane.getChildren().add(exitButton);
			exitButton.setOnAction(event -> this.close());

			prefWidth(container.getPrefWidth());
			prefHeight(container.getPrefHeight());

			first = false;
			
			this.setOnMousePressed(e -> {
				if (!content.getChildren().get(0).hoverProperty().get())
					close();
			});
		}

		if (!container.getChildren().get(0).equals(this)) {
			container.getChildren().clear();
			container.getChildren().add(this);
		}

		mainProgramContent.toBack();
		toFront();
		appearAnimation.play();
		appearShadeAnimation.play();
	}

	/** Closes the popup. */
	public void close() {
		disappearAnimation.play();
		disappearShadeAnimation.play();
		disappearAnimation.setOnFinished(event -> mainProgramContent.toFront());
		if (exitAction != null)
			exitAction.call();
	}

	/** Equivalent to <code>close()</code>. */
	public void hide() {
		close();
	}

	/** should only be called at program startup. */
	public static void initialize(StackPane container, Pane mainContent, Pane shade) {
		ModalPopup.container = container;
		ModalPopup.mainProgramContent = mainContent;
		ModalPopup.shade = shade;
	}

	private void setDefaultFade() {
		appearAnimation = new FadeTransition(Duration.millis(300), this.getChildren().get(0));
		FadeTransition fade = (FadeTransition) appearAnimation;
		fade.setFromValue(0);
		fade.setToValue(1);

		disappearAnimation = new FadeTransition(Duration.millis(300), this.getChildren().get(0));
		fade = (FadeTransition) disappearAnimation;
		fade.setFromValue(1);
		fade.setToValue(0);

		appearShadeAnimation = new FadeTransition(Duration.millis(300), shade);
		fade = (FadeTransition) appearShadeAnimation;
		fade.setFromValue(0);
		fade.setToValue(1);

		disappearShadeAnimation = new FadeTransition(Duration.millis(300), shade);
		fade = (FadeTransition) disappearShadeAnimation;
		fade.setFromValue(1);
		fade.setToValue(0);
	}

	// TODO TEMP: REMOVE FOR SOMETHING BETTER
	public void setOnExit(Action action) {
		this.exitAction = action;
	}

	public void cutRight(double amount) {
		this.setMaxHeight(this.getHeight() - amount);
	}

	public FlowPane getContent() {
		return this.content;
	}
}
