package Util;

import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventHandler;

public class MultiEvent<T extends Event> implements EventHandler<T> {

	List <EventHandler<T>> events = new ArrayList<EventHandler<T>>();
	
	public void addEvent(EventHandler<T> e) {
		this.events.add(e);
	}

	@Override
	public void handle(T event) {
		for (EventHandler<T> e : events) {
			e.handle(event);
		}
	}
}
