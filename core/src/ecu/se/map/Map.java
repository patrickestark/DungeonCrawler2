package ecu.se.map;

import java.util.ArrayList;
import java.util.LinkedList;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ecu.se.Globals;

public class Map {

	public static final int DIAGONAL_COST = 14;
	public static final int V_H_COST = 10;
	public static LinkedList<Vector3> open = new LinkedList<Vector3>();
	public static LinkedList<Vector3> closed = new LinkedList<Vector3>();

	private static int tilesVertical = 5;
	private static int tilesHorizontal = 5;

	private static Tile[][] visibleTiles;
	private static ArrayList<Floor> floors;
	private static Floor currentFloor;
	private static TextureRegion background;
	private static int currentLevel = 0;

	public Map() {
		floors = new ArrayList<Floor>(200);
		setFloor(currentLevel);
	}

	public static void setFloor(int floor) {
		if (floor < 0)
			floor = 0;
		if (floors.size() <= floor) {
			for (int i = floors.size(); i <= floor; i++) {
				floors.add(null);
			}
		}
		if (floors.get(floor) == null) {
			floors.add(floor, new Floor());
		}
		currentFloor = floors.get(floor);
		if (!currentFloor.getGenerated()) {
			currentFloor.generate();
		}

	}

	public static void render(SpriteBatch batch, int cameraX, int cameraY) {
		if (Globals.RENDER_ALL_TILES) {
			currentFloor.renderAll(batch);
			return;
		}

		visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
		for (int i = 0; i < tilesHorizontal; i++) {
			for (int j = 0; j < tilesVertical; j++) {
				if (visibleTiles[i][j] != null) {
					visibleTiles[i][j].render(batch);
				}
			}
		}
	}

	private static ShapeRenderer debugRenderer = new ShapeRenderer();

	public static void debugRender(Matrix4 projection, int cameraX, int cameraY) {
		debugRenderer.begin(ShapeType.Line);
		debugRenderer.setProjectionMatrix(projection);
		debugRenderer.setColor(Color.RED);

		visibleTiles = currentFloor.getAdjacent(cameraX, cameraY, tilesHorizontal, tilesVertical);
		for (int i = 0; i < tilesHorizontal; i++) {
			for (int j = 0; j < tilesVertical; j++) {
				if (visibleTiles[i][j] != null) {
					debugRenderer.polygon(visibleTiles[i][j].getBounds().getTransformedVertices());
				}
			}
		}

		debugRenderer.end();
	}

	public static void dispose() {
		for (Floor f : floors) {
			if (f != null)
				f.dispose();
		}
	}

	public static Tile currentTile(int x, int y) {
		return currentFloor.getTile(x, y);
	}

	public static Vector2 getFloorIn(int x, int y) {
		return currentFloor.getFloorIn(x, y);
	}

	public static Vector2 getFloorOut(int x, int y) {
		return currentFloor.getFloorOut(x, y);
	}

	public static void setScreenResolution(int screenWidth, int screenHeight) {
		tilesHorizontal = (screenWidth / Globals.TILE_PIXEL_WIDTH) + 2;
		tilesVertical = (screenHeight / Globals.TILE_PIXEL_HEIGHT) + 2;
	}

	// TODO: Pathfinding!
	public static LinkedList<Vector2> getPath(Vector2 from, Vector2 to) {
		// Return a list of Vector2s. Should correspond to tile indices in tiles.
		LinkedList<Vector2> path = new LinkedList<Vector2>();
		

		boolean found = false;
		Vector2 startPos = from, endPos = to,current, endTest;
		Vector3 newCurrent;
		
        current = startPos;
		newCurrent = new Vector3(startPos.x,startPos.y,0);

		while ( found = false) {
//			new Vector2(startPos.x + Direction.NORTH.x, startPos.y + Direction.NORTH.y);
			
			findOpen( current, endPos);
			newCurrent=getLowestFCost(open);
			
			closed.add(newCurrent);
			open.remove(newCurrent);
			//System.out.println(size(open));
			
			endTest =new Vector2(newCurrent.x,newCurrent.y);
			
			if(endTest==endPos) {
				
			found = true;
			
			}
			
			
			}
		 
		   for(int count = 0; count < size(closed); count++) {
			
			if(closed.get(count).z == newCurrent.z) {
				
				path.add(new Vector2(closed.get(count).x,closed.get(count).y));
				
			}
		}
		
		
		return path;

	}
	public static float getFCost (Vector2 current, Vector2 endPos) {
		float  x1, x2, y1, y2, gCost,heCost, fCost;
		
		 x1=current.x;
		 y1=current.y;
		 
		 x2=endPos.x;
		 y2=endPos.y;
	   
		gCost = (DIAGONAL_COST * y2) + V_H_COST * (x2-y2);
		heCost= (DIAGONAL_COST * y1) + V_H_COST * (x1-y1);
		fCost= gCost + heCost;
			
		return  fCost;
	}
	public static void findOpen(Vector2 current, Vector2 endPos)
	{
		
		Direction[] dir = new Direction[8];
		dir = Direction.values();
		Vector2 test;
		
		for(int i = 0; i < 7; i++)
		{
			test = new Vector2(current.x + dir[i].x, current.y = dir[i].y);
			
			if(test != null) {
				
			open.add(new Vector3(current.x + dir[i].x, current.y = dir[i].y, getFCost(current, endPos)));
			}
		}
		
		
	}
	
	public static Vector3 getLowestFCost(LinkedList<Vector3> open)
	{
		int lowestIndex = 0;
		for(int i = 0; i < size(open); i++)
		{

			if(open.get(lowestIndex).z > open.get(i).z)

			{
				lowestIndex = i;
			}
		}

		return open.get(lowestIndex);
	}
	
	public static int size(LinkedList<Vector3> list) {
		
		int size=0;
		
		while (list.get(size) != null){
			//System.out.println("size in Map.java" + size);
			size++;
		}
		return size;

		
	}
}