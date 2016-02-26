package util;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.binding.BooleanBinding;
import javafx.scene.control.Button;

public class NavigationButton extends Button {
	ContentView view;
	List<BooleanBinding> bindings = new ArrayList<BooleanBinding>();
	
	public void setView(ContentView view) {
		this.view = view;
	}
	
	public void addBinding(BooleanBinding binding) {
		bindings.add(binding);
		binding.addListener((obs, oldValue, newValue) -> update());
	}
	
	public void update() {
		for (BooleanBinding binding : bindings)
			if (!binding.get()) {
				setDisable(true);
				return;
			}
		
		setDisable(false);
	}
}
