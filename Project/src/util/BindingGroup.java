package util;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class BindingGroup implements ObservableValue<Boolean> {
	
	private List<BooleanBinding> bindings = new ArrayList<BooleanBinding>();
	private List<ChangeListener<? super Boolean>> listeners = new ArrayList<ChangeListener<? super Boolean>>();
	
	/** Combined AND between all BooleanBindings in the group. */
	private BooleanProperty refreshBinding = new SimpleBooleanProperty(true);
	private BooleanBinding state = Bindings.createBooleanBinding(() -> refreshBinding.get(), refreshBinding);
	
	public void addBinding(BooleanBinding binding) {
		bindings.add(binding);
		state = state.and(binding);
	}
	
	public void addBindings(BooleanBinding... bindings) {
		for (BooleanBinding binding : bindings)
			addBinding(binding);
	}
	
	public boolean allMet() {
		for (BooleanBinding binding : bindings)
			if (!binding.get())
				return false;
		
		return true;
	}
	
	/** Sends an update call to every listener */
	public void refresh() {
		refreshBinding.set(false);
		refreshBinding.set(true);
	}
	
	/** Returns the BooleanBindings of this group. */
	public List<BooleanBinding> getBinds() {
		return this.bindings;
	}
	
	/** Replaces the BooleanBindings of this group with those in <code>bindings</code>. */
	public void setAll(List<BooleanBinding> bindings) {
		for (BooleanBinding bind : bindings)
			addBinding(bind);
	}

	/** Do not use, no functionality */
	@Override
	public void addListener(InvalidationListener listener) {
	}

	/** Do not use, no functionality */
	@Override
	public void removeListener(InvalidationListener listener) {
	}

	@Override
	public void addListener(ChangeListener<? super Boolean> listener) {
		listeners.add(listener);
	}

	@Override
	public Boolean getValue() {
		return allMet();
	}

	@Override
	public void removeListener(ChangeListener<? super Boolean> listener) {
		listeners.remove(listener);
	}
	
	public void clearListeners() {
		listeners.clear();
	}
	
	/** Combined AND between all BooleanBindings in the group. */
	public BooleanBinding getState() {
		return state;
	}
}
