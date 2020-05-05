/*
Written by: Garrett Sullivan

Sources/Credit: Connor Anderson's Youtube series "libGDX-Box2D"
https://www.youtube.com/watch?v=_y1RvNWoRFU&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI
*/

package com.mytile.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

public class RacingGame extends Game {
	
	public SpriteBatch batch;
	public Texture playOn, trackOn, garageOn, optionsOn, quitOn, back, backOn, outline, CustomOn;
	public Texture background, track8, trackLava, trackBeach, trackSnow, trackMud, trackCustom, selcar, seltrack;
	public Texture racecar, Red, Yellow, Green, Blue, Purple, White;
	public OrthographicCamera cam;
	public Box2DDebugRenderer B2Drender;
	public World world;
	public Body driver;
	public OrthogonalTiledMapRenderer rend1;
	public OrthogonalTiledMapRenderer rendMenu;
	public TiledMap track;
	public TiledMap menu;
	public int tsel = 1, csel = 1;
	
	@Override
	public void create () {
		this.setScreen(new Menu(this)); //sets screen to menu screen when game starts
	}

	@Override
	public void render () {
		super.render();
	}
}
