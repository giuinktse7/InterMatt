package util;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

public class ContentView {
	private Node content;
	
	private BooleanProperty activeProperty = new SimpleBooleanProperty();
	
	private ContentView previous;
	private ContentView next;
	
	private List<Condition> conditions;
	
	public ContentView(Node content, ContentView previous, ContentView next) {
		this.content = content;
		this.previous = previous;
		this.next = next;
		
		conditions = new ArrayList<Condition>();
	}
	
	public ContentView(Node content) {
		this(content, null, null);
	}
	
	public void setNext(ContentView view) {
		this.next = view;
	}
	
	public void setPrevious(ContentView view) {
		this.previous = view;
	}
	
	public void require(Condition condition) {
		conditions.add(condition);
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
	
	public boolean validate() {
		boolean pass = true;
		for (Condition condition : conditions)
			if(!condition.isSatisfied())
				pass = false;
		
		return pass;
	}
	
	public BooleanProperty activeProperty() {
		return this.activeProperty;
	}
	
	public boolean isDisabled() {
		return !activeProperty.get();
	}
}
