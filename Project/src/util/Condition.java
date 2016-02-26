package util;

public class Condition {

	private Requirement requirement;
	private Performable action;
	
	public Condition(Requirement requirement, Performable action) {
		this.requirement = requirement;
		this.action = action;
	}

	public boolean isSatisfied() {
		boolean pass = requirement.require();
		if (!pass)
			action.perform();
		
		return pass;
	}
}
