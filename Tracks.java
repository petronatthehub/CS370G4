package com.racing.game3;

/*
Written by: Garrett Sullivan
Source/Credit: Hollowbit's Youtube video "LibGDX 2D Tutorial #4: Main Menu and Mouse Input"
 https://www.youtube.com/watch?v=67ZCQt8QpNA

*/

import static com.racing.game3.extras.Constants.Window_H;
import static com.racing.game3.extras.Constants.BoxH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tracks implements Screen {

    public RacingGamev3 g;
    private int backW = 340;
    private int BackY = 910;
    private int BackX = 0;
    private int On = 10;

    public Tracks(RacingGamev3 game) {
        this.g = game;
        //stores the width and height of the app window
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        //creates an OrthographicCamera and sets the camera's position inside the window
        g.cam = new OrthographicCamera();
        g.cam.setToOrtho(false, w/2, h/2);

        g.batch = new SpriteBatch();
        //used to store a textures
        g.background = new Texture("Background.png");
        g.back = new Texture("buttons/Back.png");
        g.backOn = new Texture("buttons/BackOn.png");
        g.outline = new Texture("buttons/Outline.png");
        g.trackCustom = new Texture("tracks/Custom.png");
        g.track8 = new Texture("tracks/8Trackpic.png");
        g.trackLava = new Texture("tracks/lavaTrackpic.png");
        g.trackSnow = new Texture("tracks/snowTrackpic.png");
        g.trackBeach = new Texture("tracks/beachTrackpic.png");
        g.trackMud = new Texture("tracks/TiledTrackwithMud.png");
        g.CustomOn = new Texture("tracks/CustomOn.png");
    }

    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {

        //fills the screen with all the necessary textures
        g.batch.begin();
        g.batch.draw(g.background, 0, 0);
        g.batch.draw(g.back,BackX,BackY);
        g.batch.draw(g.trackMud,250,550);
        g.batch.draw(g.track8,600,550);
        g.batch.draw(g.trackLava,950,550);
        g.batch.draw(g.trackSnow,250,150);
        g.batch.draw(g.trackBeach,600,150);
        g.batch.draw(g.trackCustom,940,140);
        g.batch.end();

        //calls input method to sense all user mouse input
        userInput();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {	//dispose or wipes screen clean

        g.batch.dispose();
        g.background.dispose();
        g.back.dispose();
        g.backOn.dispose();
        g.outline.dispose();
        g.trackCustom.dispose();
        g.track8.dispose();
        g.trackLava.dispose();
        g.trackSnow.dispose();
        g.trackBeach.dispose();
        g.trackMud.dispose();
        g.CustomOn.dispose();
        g.dispose();
    }

    public void userInput() {

        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            g.setScreen(new Menu(g));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        //checks if the mouse is hovering over the back button
        //then draws texture on the correct back button
        if(Gdx.input.getX() > BackX+On && Gdx.input.getX() < BackX+backW && Window_H - Gdx.input.getY() < BackY+On+BoxH && Window_H - Gdx.input.getY() > BackY+On) {

            g.batch.begin();
            g.batch.draw(g.backOn,BackX+On,BackY);
            g.batch.end();

            if(Gdx.input.isTouched()) {
                g.setScreen(new Menu(g));	//sets current screen to menu
            }
        }
        //checks if the mouse is hovering over the top row of tracks
        //then draws outline texture around the correct track
        if(Window_H - Gdx.input.getY() < 550 + 300 && Window_H - Gdx.input.getY() > 550) {

            if(Gdx.input.getX() > 250 && Gdx.input.getX() < 250 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,240,540);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.tsel = 1;
                }
            }
            if(Gdx.input.getX() > 600 && Gdx.input.getX() < 600 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,590,540);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.tsel = 2;
                }
            }
            if(Gdx.input.getX() > 950 && Gdx.input.getX() < 950 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,940,540);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.tsel = 3;
                }
            }
        }
        //checks if the mouse is hovering over the bottom row of tracks
        //then draws outline texture around the correct track
        if(Window_H - Gdx.input.getY() < 150 + 300 && Window_H - Gdx.input.getY() > 150) {

            if(Gdx.input.getX() > 250 && Gdx.input.getX() < 250 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,240,140);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.tsel = 4;
                }
            }
            if(Gdx.input.getX() > 600 && Gdx.input.getX() < 600 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,590,140);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.tsel = 5;
                }
            }
            if(Gdx.input.getX() > 950 && Gdx.input.getX() < 950 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,940,140);
                g.batch.draw(g.CustomOn,940,140);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.tsel = 1;
                }
            }
        }
    }
}
