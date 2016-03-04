package util;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class ObservableContentView implements ObservableValue<ContentView> {

	private ContentView contentView;
	private List<ChangeListener<? super ContentView>> listeners = new ArrayList<ChangeListener<? super ContentView>>();
	
	public ObservableContentView(ContentView contentView) {
		this.contentView = contentView;
	}
	
	public void setValue(ContentView contentView) {
		ContentView oldValue = this.contentView;
		ContentView newValue = contentView;
		this.contentView = contentView;
		listeners.forEach(consumer -> consumer.changed(this, oldValue, newValue));
	}
	
	/** Do not use, no functionality */
	@Override
	public void addListener(InvalidationListener listener) {
	}
	
	/** Do not use, no functionality */
	@Override
	public void removeListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addListener(ChangeListener<? super ContentView> listener) {
		listeners.add(listener);
	}

	@Override
	public ContentView getValue() {
		return this.contentView;
	}
	
	public ContentView getView() {
		return this.contentView;
	}

	@Override
	public void removeListener(ChangeListener<? super ContentView> listener) {
		listeners.remove(listener);
	}
	
	/** Returns the "css-ID" of the wrapped ContentView */
	public String getID() {
		return contentView.getID();
	}

}
