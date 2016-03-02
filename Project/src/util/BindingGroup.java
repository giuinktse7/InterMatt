package util;

import java.util.ArrayList;
import java.util.List;

import interfaces.Action;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class BindingGroup implements ObservableValue<Boolean> {
	
	/** Used to refresh the AND binding, has to be true except when refreshing */
	private BooleanProperty propertyForUpdating = new SimpleBooleanProperty(true);
	
	List<BooleanBinding> bindings = new ArrayList<BooleanBinding>();
	Action onTrueAction, onFalseAction;
	private List<ChangeListener<? super Boolean>> listeners = new ArrayList<ChangeListener<? super Boolean>>();
	
	/** Combined AND between all BooleanBindings in the group. */
	private BooleanBinding state = Bindings.createBooleanBinding(() -> propertyForUpdating.get(), propertyForUpdating);
	
	//Temporary TODO remove
	public String name;
	
	public BindingGroup() {
		onTrueAction = () -> {};
		onFalseAction = () -> {};
		addBinding(Bindings.createBooleanBinding(() -> propertyForUpdating.get(), propertyForUpdating));
		this.name = "N/A";
	}
	
	public void addBinding(BooleanBinding binding) {
		bindings.add(binding);
		binding.addListener((obs, oldValue, newValue) -> update());
		
			state = state.and(binding);
		
		update();
	}
	
	//Temporary TODO remove
	public void setName(String name) {
		this.name = name;
	}
	
	public void addBindings(BooleanBinding... bindings) {
		for (BooleanBinding binding : bindings)
			addBinding(binding);
	}
	
	public void setOnTrueAction(Action action) {
		this.onTrueAction = action;
	}
	
	public void setOnFalseAction(Action action) {
		this.onFalseAction = action;
	}
	
	
	public void update() {
		if (!state.get()) {
			onFalseAction.call();
			return;
		}
		
		onTrueAction.call();
	}
	
	public void executeTrueAction() {
		onTrueAction.call();
	}
	
	public void executeFalseAction() {
		onFalseAction.call();
	}
	
	public void refreshAND() {
		propertyForUpdating.set(false);
		propertyForUpdating.set(true);
	}
	
	public boolean allMet() {
		for (BooleanBinding binding : bindings)
			if (!binding.get())
				return false;
		
		return true;
	}
	
	/** Returns the BooleanBindings of this group. */
	public List<BooleanBinding> getBinds() {
		return this.bindings;
	}
	
	/** Replaces the BooleanBindings of this group with those in <code>bindings</code>. */
	public void setAll(List<BooleanBinding> bindings) {
		this.bindings.removeIf((e) -> !e.getDependencies().contains(propertyForUpdating));
		for (BooleanBinding bind : bindings)
			addBinding(bind);
	}

	/** Do not use, no functionality */
	@Override
	public void addListener(InvalidationListener listener) {
		// TODO Auto-generated method stub
		
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
