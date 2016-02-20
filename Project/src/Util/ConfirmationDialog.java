package Util;
/*
 * Taken from https://s3.amazonaws.com/baanthaispa/ConfirmationDialog.zip, 17/02/2016
 * Modified to fit needs
 */


import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/** Displays a confirmation dialog to which a yes or no response is expected. */
public class ConfirmationDialog {
	final private Stage dialog = new Stage(StageStyle.TRANSPARENT);
	final private Pane content;
	final private Button yesButton;
	final private Button noButton;
	private Parent ownerRoot;
	final private Delta dragDelta = new Delta();
	final private Window owner;

	final private ObjectProperty<Effect> ownerEffectProperty = new SimpleObjectProperty<>();
	final private BooleanProperty isDraggableProperty = new SimpleBooleanProperty();
	final private ObjectProperty<Side> sideProperty = new SimpleObjectProperty<Side>();
	final private ReadOnlyBooleanWrapper confirmedProperty = new ReadOnlyBooleanWrapper();
	final private ObjectProperty<EventHandler<ActionEvent>> onYesProperty = new SimpleObjectProperty<>();
	final private ObjectProperty<EventHandler<ActionEvent>> onNoProperty = new SimpleObjectProperty<>();
	
	private boolean canDrag = false;


	/**
	 * Create a new confirmation dialog.
	 * 
	 * @param owner
	 *            the window which owns this dialog (the parent of the dialog).
	 * @param question
	 *            a question the dialog will ask confirmation for (should have a
	 *            yes or no answer).
	 */
	public ConfirmationDialog(Window owner, Pane content, Button yesButton, Button noButton) {
		this.yesButton = yesButton;
		this.noButton = noButton;
		this.content = content;
		
		this.owner = owner;
		
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(owner);

		yesButton.setDefaultButton(true);
		noButton.setCancelButton(true);
		
		ownerRoot = owner.getScene().getRoot();
		
		dialog.setScene(createScene());

		isDraggableProperty.addListener(DRAG_CHANGE_LISTENER);
		isDraggableProperty.set(true);

		sideProperty.addListener(SIDE_CHANGE_LISTENER);
	}
	
	public void setYesButtonAction(EventHandler<ActionEvent> e) {
		MultiEvent<ActionEvent> multiEvent = new MultiEvent<ActionEvent>();
		multiEvent.addEvent(YES_HANDLER);
		multiEvent.addEvent(e);
		yesButton.setOnAction(multiEvent);
	}
	
	public void setNoButtonAction(EventHandler<ActionEvent> e) {
		MultiEvent<ActionEvent> multiEvent = new MultiEvent<ActionEvent>();
		multiEvent.addEvent(NO_HANDLER);
		multiEvent.addEvent(e);
		noButton.setOnAction(multiEvent);
	}

	/**
	 * Show the dialog and pause execution of the calling thread until the user
	 * makes a confirmation selection.
	 */
	public void showAndWait() {
		if (ownerEffectProperty.get() != null) {
			ownerRoot.setEffect(ownerEffectProperty.get());
		}
		dialog.show();
	}

	/**
	 * Show the dialog and immediately continue execution of the calling thread.
	 */
	public void show() {
		if (ownerEffectProperty.get() != null) {
			ownerRoot.setEffect(ownerEffectProperty.get());
		}
		dialog.show();
	}
	
	private void makeDraggable() {
		content.getStyleClass().add("draggable");
		content.setOnMousePressed(ON_MOUSE_PRESSED_DRAG_HANDLER);
		content.setOnMouseDragged(ON_MOUSE_DRAGGED_DRAG_HANDLER);
	}

	private void makeUndraggable() {
		content.getStyleClass().remove("draggable");
		content.setOnMousePressed(null);
		content.setOnMouseDragged(null);
	}

	private Scene createScene() {
		Scene scene = new Scene(content, Color.TRANSPARENT);
		//scene.getStylesheets().add(css);
		return scene;
	}
	
	public void setCenter() {
		dialog.setX(owner.getX() + owner.getWidth() / 2 - dialog.getWidth() / 2);
		dialog.setY(owner.getY() + owner.getHeight() / 2 - dialog.getHeight() / 2);
	}

	private void relocateDialog(Side newValue) {
		if (newValue != null) {
			final Window owner = dialog.getOwner();
			switch (newValue) {
			case TOP:
				setDialogX(owner.getX() + owner.getWidth() / 2 - dialog.getWidth() / 2);
				setDialogY(owner.getY() - dialog.getHeight());
				break;

			case RIGHT:
				setDialogX(owner.getX() + owner.getWidth());
				setDialogY(owner.getY() + owner.getHeight() / 2 - dialog.getHeight() / 2);
				break;

			case BOTTOM:
				setDialogX(owner.getX() + owner.getWidth() / 2 - dialog.getWidth() / 2);
				setDialogY(owner.getY() + owner.getHeight());
				break;

			case LEFT:
				setDialogX(owner.getX() - dialog.getWidth());
				setDialogY(owner.getY() + owner.getHeight() / 2 - dialog.getHeight() / 2);
				break;
			}
		} else {
			dialog.centerOnScreen();
		}
	}
	
	// default action to take on yes chosen.
		final private EventHandler<ActionEvent> YES_HANDLER = (event) -> {
			if (onYesProperty.get() != null)
				onYesProperty.get().handle(event);
			
			if (!event.isConsumed()) {
				confirmedProperty.setValue(true);
				if (ownerEffectProperty.get() != null)
					ownerRoot.setEffect(null);
				
				dialog.close();
			}
		};

		// default action to take on no chosen.
		final private EventHandler<ActionEvent> NO_HANDLER = (event) -> {
			if (onNoProperty.get() != null)
				onNoProperty.get().handle(event);
			
			if (!event.isConsumed()) {
				confirmedProperty.setValue(false);
				if (ownerEffectProperty.get() != null)
					ownerRoot.setEffect(null);
				
				dialog.close();
			}
		};

	// perform the drag operation.
	private EventHandler<MouseEvent> ON_MOUSE_DRAGGED_DRAG_HANDLER = (mouseEvent) -> {
		if (canDrag) {
			dialog.setX(mouseEvent.getScreenX() + dragDelta.x);
			dialog.setY(mouseEvent.getScreenY() + dragDelta.y);
		}
	};

	// record a delta distance for the drag and drop operation.
	private final EventHandler<MouseEvent> ON_MOUSE_PRESSED_DRAG_HANDLER = 	(mouseEvent) -> {
		if (mouseEvent.getSceneY() <= 47) {
			dragDelta.x = dialog.getX() - mouseEvent.getScreenX();
			dragDelta.y = dialog.getY() - mouseEvent.getScreenY();
			canDrag = true;
		} else
			canDrag = false;
	};
	
	
	
	
	
	// switch the drag option on and off as appropriate.
	private final ChangeListener<Boolean> DRAG_CHANGE_LISTENER = (obs, oldValue, newValue) -> {
		if (newValue == null || !newValue)
			makeUndraggable();
		else
			makeDraggable(); 
		};

	// display the dialog at the appropriate side of the owner.
	private final ChangeListener<Side> SIDE_CHANGE_LISTENER = (obs, oldValue, newValue) -> {
		if (dialog.isShowing())
			relocateDialog(newValue);
		else
			dialog.setOnShown((event) -> relocateDialog(newValue));
	};

	final private static double EDGE_INSET = 100;

	private void setDialogX(double x) {
		final double min = EDGE_INSET;
		final double max = getMaxX() - dialog.getWidth() - EDGE_INSET;
		x = Math.min(max, x);
		x = Math.max(min, x);

		dialog.setX(x);
	}

	private void setDialogY(double y) {
		final double min = EDGE_INSET;
		final double max = getMaxY() - dialog.getHeight() - EDGE_INSET;
		y = Math.min(max, y);
		y = Math.max(min, y);

		dialog.setY(y);
	}

	private double getMaxX() {
		double maxX = 0;
		for (final Screen s : Screen.getScreens()) {
			maxX = Math.max(maxX, s.getVisualBounds().getMaxX());
		}
		return maxX;
	}

	private double getMaxY() {
		double maxY = 0;
		for (final Screen s : Screen.getScreens()) {
			maxY = Math.max(maxY, s.getVisualBounds().getMaxY());
		}
		return maxY;
	}

	/**
	 * @return the confirmation selection the user has made.
	 */
	public ReadOnlyBooleanProperty confirmedProperty() {
		return confirmedProperty.getReadOnlyProperty();
	}

	/**
	 * @return true if the user answered yes to the question, false if the user
	 *         answered no to the question, null if the user has not yet made a
	 *         selection.
	 */
	public Boolean isConfirmed() {
		return confirmedProperty.getValue();
	}

	/** @return custom action which can be taken on yes. */
	public ObjectProperty<EventHandler<ActionEvent>> onYesProperty() {
		return onYesProperty;
	}

	/**
	 * @param onYes
	 *            custom action which can be taken on yes.
	 */
	public void setOnYes(EventHandler<ActionEvent> onYes) {
		onYesProperty.set(onYes);
	}

	/** @return custom action which can be taken on yes. */
	public EventHandler<ActionEvent> getOnYes() {
		return onYesProperty.get();
	}

	/** @return custom action which can be taken on no. */
	public ObjectProperty<EventHandler<ActionEvent>> onNoProperty() {
		return onNoProperty;
	}

	/**
	 * @param onNo
	 *            custom action which can be taken on no.
	 */
	public void setOnNo(EventHandler<ActionEvent> onNo) {
		onNoProperty.set(onNo);
	}

	/** @return custom action which can be taken on no. */
	public EventHandler<ActionEvent> getOnNo() {
		return onNoProperty.get();
	}

	/**
	 * @return effect to be applied to the owner stage when the dialog is shown.
	 */
	public ObjectProperty<Effect> ownerEffectProperty() {
		return ownerEffectProperty;
	}

	/**
	 * @param ownerEffect
	 *            effect to be applied to the owner stage when the dialog is
	 *            shown.
	 */
	public void setOwnerEffect(Effect ownerEffect) {
		ownerEffectProperty.set(ownerEffect);
	}

	/**
	 * @param effect
	 *            to be applied to the owner stage when the dialog is shown.
	 */
	public Effect getOwnerEffect() {
		return ownerEffectProperty.get();
	}

	/** @return whether or not the dialog can be dragged around. */
	public BooleanProperty isDraggableProperty() {
		return isDraggableProperty;
	}

	/**
	 * @param isDraggable
	 *            whether or not the dialog can be dragged around.
	 */
	public void setDraggable(boolean isDraggable) {
		isDraggableProperty.set(isDraggable);
	}

	/** @return whether or not the dialog can be dragged around. */
	public boolean isDraggable() {
		return isDraggableProperty.getValue();
	}

	/**
	 * @param side
	 *            the side of the owner at which the dialog is to be displayed.
	 */
	public void setSide(Side side) {
		sideProperty.set(side);
	}

	/** @return the side of the owner at which the dialog is to be displayed. */
	public void getSide(Side side) {
		sideProperty.get();
	}

	// records relative x and y co-ordinates.
	private class Delta {
		double x, y;
	}
}