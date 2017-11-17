package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import actors.Player;
import assetManager.AssetManager;
import assetManager.TextureAsset;
import ecu.se.Game;

public class GUI {

	private static int halfWidth, halfHeight;
	private int x, y;
	private OrthographicCamera hudCamera;
	private Matrix4 oldProjectionMatrix;

	public static final float defaultWidth = 1920.0f;
	public static final float defaultHeight = 1080.0f;
	public static float conversionX;
	public static float conversionY;

	// public static final int MOUSE_PRESSED = 0;
	// public static final int MOUSE_RELEASED = 0;
	// public static final int MOUSE_DOWN = 0;

	public static final int WINDOW_PAUSED		= 0;
	public static final int WINDOW_HUD			= 1;
	public static final int WINDOW_MAIN_MENU 	= 2;
	public static final int WINDOW_SETTINGS	 	= 3;
	public static final int WINDOW_INVENTORY 	= 4;
	public static int currentWindow;

	public Player player;
	private Window[] windows;

	public GUI(Player player, int screenWidth, int screenHeight) {
		this.player = player;
		halfWidth = (int) (screenWidth * 0.5);
		conversionX = screenWidth / defaultWidth;
		halfHeight = (int) (screenHeight * 0.5);
		conversionY = screenHeight / defaultHeight;
		hudCamera = new OrthographicCamera(screenWidth, screenHeight);
		windows = new Window[] { 
				new Window_PauseScreen(), 
				new Window_HUD(),
				new Window_MainMenu(),
				new Window_Settings()
		};
		currentWindow = WINDOW_HUD;
	}

	private int mouseX;
	private int mouseY;
	boolean inputUsed;

	public void update(float deltaTime) {
		mouseX = Gdx.input.getX();
		mouseY = Gdx.input.getY();
		Vector3 mouse = hudCamera.unproject(new Vector3(mouseX, mouseY, 0));
		inputUsed = false;
		if (windows[currentWindow] != null) {
			inputUsed = windows[currentWindow].update(deltaTime, (int) mouse.x, (int) mouse.y);
		}
	}

	public void render(SpriteBatch batch) {
        oldProjectionMatrix = batch.getProjectionMatrix();
        batch.setProjectionMatrix(hudCamera.combined);
        
        windows[WINDOW_HUD].render(batch);
        
        if (Game.currentState == Game.GAME_STATE_PAUSED)
        	windows[WINDOW_PAUSED].render(batch);
        
        if (currentWindow != WINDOW_HUD)        
        	windows[currentWindow].render(batch);
        
        
        // END RENDER CODE
        batch.setProjectionMatrix(oldProjectionMatrix);
        batch.setColor(Color.WHITE);
    }

	public void debugRender(ShapeRenderer renderer) {

		// Matrix4 projection = hudCamera.projection;
		// projection.scale(conversionX, conversionY, 1);
		renderer.setColor(Color.GOLDENROD);
		renderer.setProjectionMatrix(hudCamera.projection);
		if (windows[currentWindow] != null) {
			windows[currentWindow].debugRender(renderer);
		}
	}

	public static int convertX(int x) {
		return (int) (x * conversionX);
	}

	public static int convertY(int y) {
		return (int) (y * conversionY);
	}

	public static float convertX(float x) {
		return (int) (x * conversionX);
	}

	public static float convertY(float y) {
		return (int) (y * conversionY);
	}

	public static int getProportionalX(int x) {
		return convertX(x) - halfWidth;
	}

	public static int getProportionalY(int y) {
		return convertY(y) - halfHeight;
	}

	public static float getProportionalX(float x) {
		return convertX(x) - halfWidth;
	}

	public static float getProportionalY(float y) {
		return convertY(y) - halfHeight;
	}

	public OrthographicCamera getCamera() {
		return hudCamera;
	}

	public boolean mouseUsed() {
		return inputUsed;
	}

}