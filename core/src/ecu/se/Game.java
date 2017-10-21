package ecu.se;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import actors.Player;
import actors.TestActor;
import archive.Archiver;
import assetManager.AssetManager;
import ecu.se.gui.HUD;
import ecu.se.map.Map;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	
	private float deltaTime;
	private ObjectManager objectManager;
	private int screenHeight, screenWidth;
	private Map map;
	private OrthographicCamera camera;
	private Player player;
	private HUD hud;
	
	private int zoom = Globals.DEFAULT_CAMERA_ZOOM;
	
	// DEBUG THINGS - Needs to be deleted later
    private ShapeRenderer shaperRenderer;
	private ShaderProgram shader;
	private Renderable renderable;
	private Environment environment;
	private RenderContext renderContext;
	
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
		
		
		ShaderProgram.pedantic = false;
	    shader = new ShaderProgram(
	            Gdx.files.internal("shader/test.vertex.glsl").readString(),
	            Gdx.files.internal("shader/lightTest.frag").readString());
	    if(!shader.isCompiled()) {
	        Gdx.app.log("Problem loading shader:", shader.getLog());
	    }
	    batch.setShader(shader);

	}
	
	// Update all game objects
	public void update() {
	    deltaTime = Gdx.graphics.getDeltaTime();
        objectManager.update(deltaTime);
        player.update(deltaTime);
        camera.update();
        camera.zoom = zoom;
	}
	
	@Override
	public void render () {
	    input(); // JUST MOVED THIS FROM THE BOTTOM TO THE TOP
	    update();
		
	    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.setProjectionMatrix(camera.combined);
		
		batch.setShader(shader);
		map.render(batch, (int)player.x, (int)player.y);
		objectManager.render(deltaTime, batch);
		
		batch.setShader(null);
		hud.render(batch);
		
		
		batch.end();
		
		
		if(Globals.DEBUG) {
		    shaperRenderer.begin(ShapeType.Line);
		    int radius = 25;
		    shaperRenderer.ellipse((int)(camera.viewportWidth*0.5-radius*0.5), (int)(camera.viewportHeight*0.5-radius*0.5), 25, 25);
		    shaperRenderer.end();
		    
		}
		map.debugRender(camera.combined, (int)player.x, (int)player.y);
		objectManager.debugRender(camera.combined);
		
		if(Globals.DEBUG) {
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
            zoom += 1;
        } else if(Gdx.input.isKeyPressed(Input.Keys.Q)){
            zoom -= 1;
        }else if(Gdx.input.isKeyPressed(Input.Keys.R)){
            zoom = 1;
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
           
	}
	
	@Override
	public void dispose () {
		Archiver.dispose();
	    batch.dispose();
		objectManager.dispose();
		map.dispose();
		shader.dispose();
		AssetManager.dispose();
	}
	
}
