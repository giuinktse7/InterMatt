package util;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/** Holds and manages ContentViews. */
public class ViewDisplay {

	private Pane area;
	
	private Map<Node, ContentView> views = new HashMap<Node, ContentView>();
	private ObservableContentView currentView = new ObservableContentView(null);

	public ViewDisplay(Pane area) {
		this.area = area;
	}

	/**
	 * Attempts to show a view. Succeeds if the view validates successfully. If
	 * view is null or is locked, does nothing.
	 */
	public void show(ContentView view) {
		if (view == null || view.isLocked())
			return;

		area.getChildren().setAll(view.getContent());
		currentView.setValue(view);
	}
	
	public void show(Node node) {
		show(views.get(node));
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
	
	public void addPane(Pane pane) {
		views.put(pane, new ContentView(pane));
	}
	
	public void addView(ContentView view) {
		views.put(view.getContent(), view);
	}
	
	/** Gets a view. NOTE: Does not return the current view. Use getCurrentView().getValue() for that.  */
	public ContentView getView(ContentView view) {
		return views.get(view.getContent());
	}
	
	/** Gets a view. NOTE: Does not return the current view. Use getCurrentView().getValue() for that.  */
	public ContentView getView(Pane pane) {
		return views.get(pane);
	}
}
