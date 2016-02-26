package util;

import javafx.scene.layout.Pane;

/** Holds and manages ContentViews. */ 
public class ViewDisplay {

	private Pane area;
	
	private ContentView currentView;
	
	public ViewDisplay(Pane area) {
		this.area = area;
	}
	
	/** Attempts to show a view. Succeeds if the view validates successfully. If view is null, does nothing.  */
	public void show(ContentView view) {
		if (view == null) return;
		
		if (view.validate()) {
			area.getChildren().setAll(view.getContent());
			currentView = view;
		}
	}
	
	public void next() {
		show(currentView.next());
	}
	
	public void previous() {
		show(currentView.previous());
	}
}
