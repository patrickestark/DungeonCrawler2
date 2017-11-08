package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;

import actors.Player;
import assetManager.TextureAsset;


public class Window extends WindowsManager {

	private Player player;
    private int halfWidth, halfHeight;
    private int x, y;
    private OrthographicCamera hudCamera;
    private Matrix4 oldProjectionMatrix;
    
    private final float defaultWidth = 1920.0f;
    private final float defaultHeight = 1080.0f;
    private float conversionX;
    private float conversionY; 
    //test
    private BitmapFont font;
    private TextureAsset textureAsset;
    private TextureRegion textureRegion;
    private Texture texture;
    //test



public void render(SpriteBatch batch) {
    oldProjectionMatrix = batch.getProjectionMatrix();
    batch.setProjectionMatrix(hudCamera.combined);
    // RENDER CODE HERE
    
    
    //batch.draw(texture, getProportionalX(1920-120), getProportionalY(1080-60), convertX(120), convertY(67));
    
    
    // END RENDER CODE
    batch.setProjectionMatrix(oldProjectionMatrix);
}

private int convertX (int x) {
    return (int)(x * conversionX);
}
private int convertY (int y) {
    return (int)(y * conversionY);
}

private int getProportionalX(int x) {
    return convertX(x) - halfWidth;
}

private int getProportionalY(int y) {
    return convertY(y) - halfHeight;
}

public OrthographicCamera getCamera() {
	return hudCamera;
}

}