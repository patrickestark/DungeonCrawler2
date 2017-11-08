package ecu.se;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import actors.Player;
import actors.TestActor;
import archive.Archiver;
import assetManager.AssetManager;
import ecu.se.gui.Button;
import ecu.se.gui.HUD;
import ecu.se.map.Map;
import ecu.se.map.Tile;
import ecu.se.objects.Light;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	private OrthographicCamera camera;
	private Player player;
	private HUD hud;
	
	
	private FrameBuffer frameBuffer;
	private TextureRegion frameBufferRegion;
	
	private float zoom = Globals.DEFAULT_CAMERA_ZOOM;
	
	// DEBUG OBJECT(S)
	private ShapeRenderer shaperRenderer;
	private Light light;
	// END DEBUG OBJECT(S)
	
	@Override
	public void create () {
	    deltaTime = TimeUtils.millis();
	    screenHeight = Gdx.graphics.getHeight();
	    screenWidth = Gdx.graphics.getWidth();
	    objectManager = new ObjectManager();
	    map = new Map();
	    map.setScreenResolution(screenWidth, screenHeight);
	    player = new Player(map.floorHelper(0,0).x, map.floorHelper(0,0).y, 0, map, camera, "texture/spritesheet/adventuretime_sprites.png");
	    objectManager.setPlayer(player);
	    
	    Random random  = new Random();
	    for(int  i = 0; i < 50; i++) {
	        objectManager.add(new TestActor(random.nextInt(Globals.MAP_TILE_WIDTH * 128), random.nextInt(Globals.MAP_TILE_HEIGHT * 128), 0, map, "texture/spritesheet/adventuretime_sprites.png"));	        
	    }
	    hud = new HUD(player, screenWidth, screenHeight);
	    camera = new OrthographicCamera(screenWidth, screenHeight);
		batch = new SpriteBatch();
		shaperRenderer = new ShapeRenderer();
		
		
		// RECORDS
		Archiver.startArchiver();
	    Lighting.init(camera);
		Lighting.setShader(batch);
		
		light = new Light(player);
		light.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f));
		light.setIntensity(205);
		Lighting.addLight(light);
		
		light = new Light(player);
		light.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f));
		light.setIntensity(205);
		Lighting.addLight(light);
		
		light = new Light(player);
		light.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f));
		light.setIntensity(205);
		Lighting.addLight(light);
		for(int i = 0 ; i < 25; i++) {
			light = new Light(new Vector3(i*100,i*100,0));
			light.setColor(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1.0f));
			light.setIntensity(200);
			Lighting.addLight(light);
			
		}
		
		
		halfWidth = screenWidth * 0.5f;
		halfHeight = screenHeight * 0.5f;
	}
	
	// Update all game objects
	public void update() {
	    deltaTime = Gdx.graphics.getDeltaTime();
        objectManager.update(deltaTime);
        player.update(deltaTime);
        camera.update();
        Lighting.updateLights(deltaTime);
        camera.zoom = zoom;
	}
	
	private static Random rand = new Random();
	private float halfWidth;
	private float halfHeight;
	@Override
	public void render () {
	    input(); // JUST MOVED THIS FROM THE BOTTOM TO THE TOP
		
	    update();
	    
	    //TEST
	    frameBuffer = new FrameBuffer(Format.RGBA8888, screenWidth, screenHeight, false);
		frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture(), (int)(camera.viewportWidth), (int)(camera.viewportHeight));
		frameBufferRegion.flip(false, true);
	    
	    Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    frameBuffer.begin();
			batch.begin();
				batch.setProjectionMatrix(camera.combined);	
				map.render(batch, (int)player.x, (int)player.y);
				objectManager.render(deltaTime, batch);		
			batch.end();
		frameBuffer.end();
		
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
			Lighting.setShader(batch);
			batch.draw(frameBufferRegion, camera.position.x - halfWidth, camera.position.y - halfHeight, (int)(camera.viewportWidth), (int)(camera.viewportHeight));
			batch.setShader(null);
			hud.render(batch);
		batch.end();
		
		frameBuffer.dispose();

		
		
		if(Globals.DEBUG) {
		    shaperRenderer.begin(ShapeType.Line);
		    int radius = 25;
		    Vector3 temp = camera.project(light.getPos());
		    Vector3 temp2 = camera.project(player.getPosition());
		    shaperRenderer.end();
		    
			map.debugRender(camera.combined, (int)player.x, (int)player.y);
			objectManager.debugRender(camera.combined);
		
            Utils.DrawDebugLine(new Vector2(0,-50), new Vector2(0,50), camera.combined);
            Utils.DrawDebugLine(new Vector2(-50,0), new Vector2(50,0), camera.combined);
        }
		
		
	}
	int floor = 0;
	public void input() {
		
	    player.input(deltaTime);
        camera.position.set(player.x, player.y, 0);
        // Zoom camera
        if(Gdx.input.isKeyPressed(Input.Keys.E)){
            zoom += 0.01f;
            System.out.println("Zoom=" +zoom);
        } else if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            zoom -= 0.01f;
            System.out.println("Zoom=" +zoom);
        }else if(Gdx.input.isKeyPressed(Input.Keys.R)){
            zoom = Globals.DEFAULT_CAMERA_ZOOM;
        }
        
        // Generate new floor
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)){
            floor++;
            map.setFloor(floor);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)){
            floor--;
            if(floor < 0)
                floor = 0;
            map.setFloor(floor);
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)){
            Globals.RENDER_ALL_TILES ^= true; 
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            Lighting.printLog();
        }
        
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
        	
            dispose();
        }
           
	}
	
	@Override
	public void dispose () {
		Archiver.dispose();
	    batch.dispose();
		objectManager.dispose();
		map.dispose();
		Lighting.dispose();
		AssetManager.dispose();
	}
}
