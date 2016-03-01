package util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import interfaces.Action;
import javafx.beans.binding.BooleanBinding;

public class BindingGroup {
	
	List<BooleanBinding> bindings = new ArrayList<BooleanBinding>();
	Action onTrueAction, onFalseAction;
	
	public BindingGroup() {
		onTrueAction = () -> {};
		onFalseAction = () -> {};
	}
	
	public void addBinding(BooleanBinding binding) {
		bindings.add(binding);
		binding.addListener((obs, oldValue, newValue) -> update());
	}
	
	public void addBindings(BooleanBinding... bindings) {
		for (BooleanBinding binding : bindings) {
			this.bindings.add(binding);
			binding.addListener((obs, oldValue, newValue) -> update());
		}
	}
	
	public void setOnTrueAction(Action action) {
		this.onTrueAction = action;
	}
	
	public void setOnFalseAction(Action action) {
		this.onFalseAction = action;
	}
	
	public void update() {
		for (BooleanBinding binding : bindings)
			if (!binding.get()) {
				onFalseAction.call();
				return;
			}
		
		onTrueAction.call();
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
		this.bindings = bindings;
	}
}
