package control;

import java.util.ArrayList;

import interfaces.Action;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.BindingGroup;
import util.ContentView;
import util.ViewDisplay;

public class NavigationButton extends Button {
	
	private ContentView view;
	
	private Label description;
	
	private static final int SELECTED = 0;
	private static final int UNSELECTED = 1;
	private static final int DISABLED = 2;
	
	private static final String IMAGE_PATHS[] = {"resources/thisPage%s.png", "resources/previous%s.png", "resources/disabled%s.png"};
	
	private ImageView[] images = null;
	
	//Used since we only have one group of navigation buttons. Would pose a problem otherwise.
	private static ViewDisplay viewDisplay;
	
	BindingGroup bindings = new BindingGroup();
	NavigationButton nextButton;

	public NavigationButton() {
		getStyleClass().add("navigation-button");
		bindings = new BindingGroup();
	}
	
	public BindingGroup getBindingGroup() {
		return this.bindings;
	}
	
	/**
	 * Associates a view with this <code>NavigationButton</code>, and assigns it an action.
	 */
	public void initialize(ContentView view, EventHandler<ActionEvent> e, NavigationButton nextButton, Label descriptionLabel) {
		
		//Since the last actual NavigationButton won't have a nextButton, create a dummy-button.
		if (nextButton == null)
			nextButton = new NavigationButton();
		
		this.nextButton = nextButton;
		this.view = view;
		this.setOnAction(e);
		
		
		disabledProperty().addListener((obs, oldValue, disabled) -> {
			//If we were just disabled, disable the following button, too.
			if (disabled)
				this.nextButton.disable();
			//If we were just enabled, update our binds
			else
				enable();
		});
		
		disabledProperty().addListener((obs, oldValue, newValue) -> {
		updateImage(this.view.equals(newValue));
		});
		viewDisplay.getCurrentView().addListener((obs, oldValue, newValue) ->  updateImage(this.view.equals(newValue)));
		
		updateImage(this.view.equals(viewDisplay.getCurrentView().getValue()));
		
		bindings.getState().addListener((obs, o, n) -> {setDisable(!n); System.out.println("well"); });
	}
	
	private void updateImage(boolean onSelectedPage) {
		if(images == null)
			loadImages(IMAGE_PATHS);
		
		ImageView graphic = isDisabled() ? images[DISABLED] : images[UNSELECTED];
		
		if (onSelectedPage)
			graphic = images[SELECTED];	
		
		setGraphic(graphic);
	}
	
	public static void setViewDisplay(ViewDisplay viewDisplay) {
		NavigationButton.viewDisplay = viewDisplay;
	}
	
	private void loadImages(String[] paths) {
		ImageView[] imageViews = new ImageView[paths.length];
		
		int id = getNumberBasedOnFxID();
		
		for (int i = 0; i < paths.length; ++i) {
			Image image = new Image(String.format(paths[i], id), 64, 64, true, true);
			imageViews[i] = new ImageView(image);
		}
		
		images = imageViews;
	}
	
	private int getNumberBasedOnFxID() {
		ArrayList<String> a = new ArrayList<String>();
		a.add("btnToStore");
		a.add("btnToCredentials");
		a.add("btnToPurchase");
		a.add("btnToReceipt");
		
		return a.indexOf(getId()) + 1;
	}
	
	public void enable() {
		this.setDisable(false);
	}
	
	public void disable() {
		this.setDisable(true);
	}
}
