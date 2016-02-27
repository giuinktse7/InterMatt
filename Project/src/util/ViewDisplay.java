package util;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

/** Holds and manages ContentViews. */ 
public class ViewDisplay {

	private Pane area;
	
	private ObservableContentView currentView = new ObservableContentView(null);
	
	public ViewDisplay(Pane area) {
		this.area = area;
	}
	
	/** Attempts to show a view. Succeeds if the view validates successfully. If view is null, does nothing.  */
	public void show(ContentView view) {
		if (view == null) return;
		
		if (view.validate()) {
			area.getChildren().setAll(view.getContent());
			currentView.setValue(view);
		}
	}
	
	public void next() {
		show(currentView.getValue().next());
	}
	
	public void previous() {
		show(currentView.getValue().previous());
	}
	
	public ObservableValue<ContentView> getCurrentView() {
		return this.currentView;
	}
}
