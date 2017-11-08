package ecu.se.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import actors.Player;


// default public class Button extends Window
public class Button {


	private TextureAtlas atlas;
	private Stage stage;
	private Skin skin;
	
	public void create()
	{
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		stage = new Stage(new ScreenViewport());
		
		
		final TextButton button = new TextButton("Click Here", skin, "default");
		button.setWidth(200);
		button.setHeight(100);
		final Dialog dialog = new Dialog("Click Message", skin);
		stage.addActor(button);
		Gdx.input.setInputProcessor(stage);
	}

	public void render ()
	{
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

}
