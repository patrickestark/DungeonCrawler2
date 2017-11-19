package actors;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ecu.se.map.Map;
import ecu.se.ObjectManager;
import ecu.se.map.Direction;

public class RangedBadGuy extends Actor {

	private Player player;
	
	private int maxDist = 115, minDist = 100;
	
	public RangedBadGuy(float x, float y, float z, Map map, String spriteSheet, Player player) {
		super(x, y, z, map, spriteSheet);
		this.player = player;
		 currentSpeed = new Vector2(0, 0);
		 setDefaults();
	     updateStats();
	     team = Team.MOB;
	}
	
	public void act(float deltaTime) {
		
		float deltaX = player.getPosition().x - x;
		float deltaY = player.getPosition().y - y;
		
		LinkedList<Vector2> path = Map.getPath(new Vector2(player.getPosition().x, player.getPosition().y), new Vector2(deltaX, deltaY));
		Vector2 lookAt = path.getFirst();
		
		float dist = Math.abs(lookAt.x-x) + Math.abs(lookAt.y-y);
		
		if(dist > maxDist)
		{
			move(deltaTime, Direction.directionTo(x, y, lookAt.x, lookAt.y), true);
		} 
		else if(dist < minDist)
		{
			move(deltaTime, Direction.directionTo(lookAt.x, lookAt.y, x, y), true);
		}
		
		lookAt = player.getPositionV2();

    }
}
