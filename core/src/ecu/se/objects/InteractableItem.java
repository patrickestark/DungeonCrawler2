package ecu.se.objects;

import com.badlogic.gdx.math.Vector3;

import ecu.se.Game;
import ecu.se.GameObject;
import ecu.se.gui.GUI;
import ecu.se.gui.Window_Inventory;


public class InteractableItem extends ItemObject {

	public InteractableItem(float x, float y, float width, float height, String name, String path) {
		super(x, y, width, height, name, path);
		
	}
	
	public InteractableItem(float x, float y, String name, String path) {
		super(x, y, name, path);
		
	}
	
	@Override
	public void onCollision(GameObject otherObject) {
	}
	
	public void onClick(GameObject otherObject) {
		if (otherObject == Game.player) {
			((Window_Inventory)GUI.getWindow(GUI.WINDOW_INVENTORY)).insertItem(this);
		}
	}

	public String getName() {
		return name;
	}
	
	public Vector3 getPosition() {
    	return new Vector3(x, y, 1);
    }
	
	public boolean itemUsed() {
		return true;
	}
	
	
}
