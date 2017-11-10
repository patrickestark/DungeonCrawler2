package ecu.se.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

import ecu.se.map.Map;

public class Button extends Game{

	/*private Stage stage;
	private TextureAtlas atlas;
	private Table table;
	private TextButton buttonPlay, buttonExit;
	private BitmapFont black, white;
	private Label heading;
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}*/


	Stage stage;
	TextButton textButton;
	TextButtonStyle textButtonStyle;
	BitmapFont font;
	Skin skin;
	TextureAtlas buttonAtlas;
	

	@Override
	public void create() {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();
		skin = new Skin((Gdx.files.internal("uiskin.json")));
		buttonAtlas = new TextureAtlas();
		skin.addRegions(buttonAtlas);
		textButtonStyle = new TextButtonStyle();
	    textButtonStyle.font = font;
	    textButtonStyle.up = skin.getDrawable("white");
	    textButtonStyle.down = skin.getDrawable("white");
	    textButtonStyle.checked = skin.getDrawable("white");
	    textButton = new TextButton("ClickHere", textButtonStyle);
	    stage.addActor(textButton);
	    
	        
		
	}
	
	public void render()
	{
		super.render();
		stage.draw();
	}
	

		
}

	



