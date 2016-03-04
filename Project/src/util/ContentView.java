package util;

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
	}
	
	public void setPrevious(ContentView view) {
		this.previous = view;
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
	
	public boolean isDisabled() {
		return !activeProperty.get();
	}
	
	public BindingGroup getBindingGroup() {
		return this.requirements;
	}
}
