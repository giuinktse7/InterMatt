package util;

import java.util.HashMap;
import java.util.Map;

import se.chalmers.ait.dat215.project.IMatDataHandler;
import controllers.MainController;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/** Holds and manages ContentViews. */
public class ViewDisplay {

	private Pane area;
	
	private ContentView first;
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
		
		//Update arrows
		view.getBindingGroup().refreshAND();
		
		if (view.next() != null)
			view.next().getBindingGroup().refreshAND();
		
		if (view.previous() != null)
			view.previous().getBindingGroup().refreshAND();
		
		if (view.equals(first))
			MainController.leftButton.disable();
		
		if (view.getID().equals("recipePane"))
			reset();
	}
	
	public void reset(){
		//TERMINATE EVERYTHING ON MIKAELS DEMAND
		/*
		 * 	
		 * 		    \O/
		 *  	     |
		 *  __,-o_ 	/ \  _o-.__
		 * 	  
		 */
		System.out.println("sdasdsad");
		MainController.get().finishPurchase();
		/** Rensa alla vyer */
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
}
