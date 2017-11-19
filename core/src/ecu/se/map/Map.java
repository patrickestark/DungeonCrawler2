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
		LinkedList<Vector2> open = new LinkedList<Vector2>();
		LinkedList<Vector2> closed = new LinkedList<Vector2>();

		boolean walkable;
		Vector2 startPos = from, endPos = to, current;


		open.add(startPos);
		current = startPos;

		while ( walkable = true) {
			open.add(new Vector2(startPos.x + Direction.NORTH.x, startPos.y + Direction.NORTH.y));
			open.add(new Vector2(startPos.x + Direction.NORTHEAST.x, startPos.y + Direction.NORTHEAST.y));
			open.add(new Vector2(startPos.x + Direction.NORTHWEST.x, startPos.y + Direction.NORTHWEST.y));
			open.add(new Vector2(startPos.x + Direction.EAST.x, startPos.y + Direction.EAST.y));
			open.add(new Vector2(startPos.x + Direction.WEST.x, startPos.y + Direction.WEST.y));
			open.add(new Vector2(startPos.x + Direction.SOUTH.x, startPos.y + Direction.SOUTH.y));
			open.add(new Vector2(startPos.x + Direction.SOUTHEAST.x, startPos.y + Direction.SOUTHEAST.y));
			open.add(new Vector2(startPos.x + Direction.SOUTHWEST.x, startPos.y + Direction.SOUTHWEST.y));
//			new Vector2(startPos.x + Direction.NORTH.x, startPos.y + Direction.NORTH.y);
         break;
		}
		
		return null;

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
	public static LinkedList<Vector3> something(Vector2 current, Vector2 endPos)
	{
		LinkedList<Vector3> dirCost = new LinkedList<Vector3>();
		Direction[] dir = new Direction[8];
		dir = Direction.values();
		for(int i = 0; i < 7; i++)
		{
			dirCost.add(new Vector3(current.x + dir[i].x, current.y = dir[i].y, getFCost(current, endPos)));
		}
		
		if(current != null)
		{
			
		}
		return null;
	}
	
	public static Vector3 getLowestFCost(LinkedList<Vector3> dirCost)
	{
		int lowestIndex = 0;
		for(int i = 0; i < 7; i++)
		{
			if(dirCost.get(lowestIndex).z > dirCost.get(i).z)
			{
				lowestIndex = i;
			}
		}
		return dirCost.get(lowestIndex);
	}
}
