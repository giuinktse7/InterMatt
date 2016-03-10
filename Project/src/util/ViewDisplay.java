package util;

import java.util.HashMap;
import java.util.Map;
import controllers.MainController;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/** Holds and manages ContentViews. */
public class ViewDisplay {

	private Pane area;
	
	private ContentView first;
	private Map<Node, ContentView> views = new HashMap<Node, ContentView>();
	private ObservableContentView currentView = new ObservableContentView(new ContentView(null));

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
		
		//Mark the old view as inactive
		currentView.get().setActive(false);
		
		//Add new view and mark it as active
		currentView.setValue(view);
		view.setActive(true);

		if (view.getID().equals("credentialsPane")){
			MainController.get().restoreUserData();
		}

		if (view.getID().equals("purchasePane")){
			MainController.get().saveUserData();
		}
	}

	public void next() {
		show(currentView.getValue().next());
	}

	public void previous() {
		show(currentView.getValue().previous());
	}

	public ObservableContentView getCurrentView() {
		return this.currentView;
	}
	
	public void addPane(Pane pane) {
		addView(new ContentView(pane));
	}
	
	public void addView(ContentView view) {
		views.put(view.getContent(), view);
		if (first == null)
			first = view;
	}
	
	/** Gets a view. NOTE: Does not return the current view. Use getCurrentView().getValue() for that.  */
	public ContentView getView(ContentView view) {
		return views.get(view.getContent());
	}
	
	/** Gets a view. NOTE: Does not return the current view. Use getCurrentView().getValue() for that.  */
	public ContentView getView(Pane pane) {
		return views.get(pane);
	}
	
	public static void show(ViewDisplay viewDisplay, ContentView view) {
		viewDisplay.show(view);
	}
	
	public void showFirst() {
		show(first);
	}
	
	
}
