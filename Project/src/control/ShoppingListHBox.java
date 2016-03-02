package control;

import java.util.HashMap;


import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class ShoppingListHBox extends HBox {

	Map map;
	String name;
	
	public ShoppingListHBox(Label label) {
		super(label);
		name = label.getText();
		map = new TreeMap<String, Integer>();
	}
	
	public void addLine(String name, float quantity){
		map.put(name, quantity);
	}
	
	public Map getLines(){
		return map;
	}
	
	public String getName(){
		return name;
	}
}
