package util;

import com.sun.javafx.scene.traversal.Direction;

import control.ArrowButton;
import controllers.MainController;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

public class ContentView {
	private Node content;

	private BooleanProperty activeProperty = new SimpleBooleanProperty();
	BindingGroup requirements = new BindingGroup();

	private ContentView previous;
	private ContentView next;

	public ContentView(Node content, ContentView previous, ContentView next, BooleanBinding requirement) {
		this.content = content;
		this.previous = previous;
		this.next = next;
		if (requirement != null)
			requirements.addBinding(requirement);
	}

	public ContentView(Node content, ContentView previous, ContentView next) {
		this(content, previous, next, null);
	}

	public ContentView(Node content) {
		this(content, null, null, null);
	}

	public ContentView(Node content, BooleanBinding requirement) {
		this(content, null, null, requirement);
	}

	public void addRequirement(BooleanBinding requirement) {
		requirements.addBinding(requirement);
	}

	public void addRequirements(BooleanBinding... bindings) {
		for (BooleanBinding binding : bindings)
			requirements.addBinding(binding);
	}

	public void setNext(ContentView view) {
		this.next = view;

		// Handles enabling/disabling of the 'next' arrow-button
		next.getBindingGroup().getState().addListener((obs, o, n) -> {
			if (activeProperty.get()) {
				MainController.get().getArrowButton(Direction.RIGHT).setDisable(!n.booleanValue());
			}
		});
	}

	/**
	 * Sets the previous view. Also takes care of the 'previous arrow'. If
	 * <code>view</code> is set to <code>null</code>, the previous button will
	 * always be disabled for this view.
	 */
	public void setPrevious(ContentView view) {
		this.previous = view;
		ArrowButton leftArrow = MainController.get().getArrowButton(Direction.LEFT);
		//If previous view is null, always have leftArrow disabled for this view.
		if (view == null)
			activeProperty.addListener((obs, oldValue, newValue) -> {
				if (newValue)
					leftArrow.setDisable(true);
			});
		else {
			// Handles enabling/disabling of the 'previous' arrow-button
			previous.getBindingGroup().getState().addListener((obs, o, n) -> {
				if (activeProperty.get()) {
					leftArrow.setDisable(!n.booleanValue());
				}
			});
		}
	}

	public String getID() {
		return content.getId();
	}

	public Node getContent() {
		return this.content;
	}

	public ContentView next() {
		return this.next;
	}

	public ContentView previous() {
		return this.previous;
	}

	public boolean isLocked() {
		return !requirements.allMet();
	}

	public BooleanProperty activeProperty() {
		return this.activeProperty;
	}

	public void setActive(boolean value) {
		activeProperty.set(value);
	}

	public boolean isDisabled() {
		return !activeProperty.get();
	}

	public BindingGroup getBindingGroup() {
		return this.requirements;
	}
}
