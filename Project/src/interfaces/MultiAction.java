package interfaces;

import java.util.ArrayList;
import java.util.List;

public class MultiAction implements Action {

	List<Action> actions = new ArrayList<Action>();
	public MultiAction(Action... actions) {
		for (Action action : actions)
			this.actions.add(action);
	}
	
	public void call() {
		actions.forEach(action -> action.call());
	}
}
