/*
Written by: Garrett Sullivan 
Source/Credit: Hollowbit's Youtube video "LibGDX 2D Tutorial #4: Main Menu and Mouse Input"
 https://www.youtube.com/watch?v=67ZCQt8QpNA
*/

package com.mytile.game;

import static com.mytile.game.extras.Constants.PPM;
import static com.mytile.game.extras.Constants.Window_W;
import static com.mytile.game.extras.Constants.Window_H;
import static com.mytile.game.extras.Constants.BoxH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class Menu implements Screen{
	
	private static final int BoxW = 350;	//button width
	private static final int Play = 510; 	//Play button y-coordinate
	private static final int Track = 400;	//Tracks button y-coordinate
	private static final int Garage = 290;	//Garage button y-coordinate
	private static final int Options = 180;	//Options button y-coordinate
	private static final int Quit = 70;	//Quit button y-coordinate
	private static final int ONX = 630;	//On textures' x-coordinate
	
	public RacingGame g;
	
	public Menu(RacingGame game) {
		this.g = game;
		//stores the width and height of the app window 
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		//creates an OrthographicCamera and sets the camera's position inside the window
		g.cam = new OrthographicCamera();
		g.cam.setToOrtho(false, w/2, h/2);
		//creates an instance of World, which sets the gravity as a 2Dvector
		g.world = new World(new Vector2(0, 0), false);
		//used to render the Box2D World and camera
		g.B2Drender= new Box2DDebugRenderer();
		//used render Tiled map 
		g.menu = new TmxMapLoader().load("map/TiledMenu2.tmx");
		g.rendMenu = new OrthogonalTiledMapRenderer(g.menu);
		//used to handle textures
		g.batch = new SpriteBatch();
		//used to store a textures
		g.background = new Texture("Background.png");
		g.playOn = new Texture("buttons/Play.png");
		g.trackOn = new Texture("buttons/Track.png");
		g.garageOn = new Texture("buttons/Garage.png");
		g.optionsOn = new Texture("buttons/Options.png");
		g.quitOn = new Texture("buttons/Quit.png");
	}

	@Override
	public void show() {}

	@Override
	public void render(float delta) {
		//calls update and using  
		update(Gdx.graphics.getDeltaTime());
		//renders and place the texture on the body of the player
		g.batch.begin();
		g.batch.draw(g.background,0,0);
		g.batch.end();
		//renders and scales box2D world and camera
		g.B2Drender.render(g.world, g.cam.combined.scl(PPM));
		//renders the Tiled map
		g.rendMenu.render();
		
		MenuInput(delta); //captures all user input
	}

	@Override
	public void resize(int width, int height) {
		g.cam.setToOrtho(false, width*1.6f, height*1.6f);	//zooms out camera view after map is rendered
	}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {	//disposes all things on screen
		g.B2Drender.dispose();
		g.rendMenu.dispose();
		g.menu.dispose();
		g.batch.dispose();
		g.world.dispose();
		g.playOn.dispose();
		g.trackOn.dispose();
		g.garageOn.dispose();
		g.optionsOn.dispose();
		g.quitOn.dispose();
		g.dispose();
	}
	
	public void update(float delta) {
		//6 and 2 are to keep the refresh rate smooth
		g.world.step(1/60f,  6,  2);
		updateCam(); //updates camera's position
		g.rendMenu.setView(g.cam);	//resets camera view boundaries
	}
	
	public void updateCam() {
		//creates a 3D vector (position) and initializes it as the camera's current position
		Vector3 position = g.cam.position;
		//acquires the player's position 
		position.x = 800; 
		position.y = 800; 
		//sets the camera's position
		g.cam.position.set(position);
		//updates the camera with its new position
		g.cam.update();
	}
	
	public void MenuInput(float delta) {
		
		//checks if the mouse is hovering over a button
		//then draws outline texture around the correct button and sends to the correct screen when button clicked
		int boxX = Window_W/2 - BoxW/2;
		if(Gdx.input.getX() > boxX && Gdx.input.getX() < boxX + BoxW && Window_H - Gdx.input.getY() < Play + BoxH && Window_H - Gdx.input.getY() > Play) {
			
			g.batch.begin();
			g.batch.draw(g.playOn,ONX,500);
			g.batch.end();
			
			if(Gdx.input.isTouched()) {
				g.setScreen(new Race(g));
			}
		}
		
		if(Gdx.input.getX() > boxX && Gdx.input.getX() < boxX + BoxW && Window_H - Gdx.input.getY() < Track + BoxH && Window_H - Gdx.input.getY() > Track) {
			
			g.batch.begin();
			g.batch.draw(g.trackOn,ONX,390);
			g.batch.end();
			
			if(Gdx.input.isTouched()) {
				g.setScreen(new Tracks(g));
			}
		}
		if(Gdx.input.getX() > boxX && Gdx.input.getX() < boxX + BoxW && Window_H - Gdx.input.getY() < Garage + BoxH && Window_H - Gdx.input.getY() > Garage) {
			
			g.batch.begin();
			g.batch.draw(g.garageOn,ONX,280);
			g.batch.end();
			
			if(Gdx.input.isTouched()) {
				g.setScreen(new Garage(g));
			}
		}
		if(Gdx.input.getX() > boxX && Gdx.input.getX() < boxX + BoxW && Window_H - Gdx.input.getY() < Options + BoxH && Window_H - Gdx.input.getY() > Options) {
			
			g.batch.begin();
			g.batch.draw(g.optionsOn,ONX,170);
			g.batch.end();
			
			if(Gdx.input.isTouched()) {
				g.setScreen(new Options(g));
			}
		}
		if(Gdx.input.getX() > boxX && Gdx.input.getX() < boxX + BoxW && Window_H - Gdx.input.getY() < Quit + BoxH && Window_H - Gdx.input.getY() > Quit) {
			
			g.batch.begin();
			g.batch.draw(g.quitOn,ONX,60);
			g.batch.end();
			
			if(Gdx.input.isTouched()) {	//disposes all assets from screen then close the game window
				Gdx.app.exit();
			}
		}
	}
}
