package com.racing.game3;

/*
Written by: Garrett Sullivan
Source/Credit: Hollowbit's Youtube video "LibGDX 2D Tutorial #4: Main Menu and Mouse Input"
 https://www.youtube.com/watch?v=67ZCQt8QpNA
*/

import static com.racing.game3.extras.Constants.BoxH;
import static com.racing.game3.extras.Constants.Window_H;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Garage implements Screen {

    public RacingGamev3 g;
    private int backW = 340;	//back button width
    private int BackY = 910;	//back button y-coordinate on screen
    private int BackX = 0;	//back button x-coordinate on screen
    private int On = 10;	//offset variable for the "On" texture

    public Garage(RacingGamev3 game) {
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
        g.Red = new Texture("cars/garage/RedCarB.png");
        g.Yellow = new Texture("cars/garage/YellowCarB.png");
        g.Green = new Texture("cars/garage/GreenCarB.png");
        g.Blue = new Texture("cars/garage/BlueCarB.png");
        g.Purple = new Texture("cars/garage/PurpleCarB.png");
        g.White = new Texture("cars/garage/WhiteCarB.png");
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        //fills the screen with all the necessary textures
        g.batch.begin();
        g.batch.draw(g.background, 0, 0);
        g.batch.draw(g.back,BackX,BackY);
        g.batch.draw(g.Red,300,550);
        g.batch.draw(g.Yellow,650,550);
        g.batch.draw(g.Green,1000,550);
        g.batch.draw(g.Blue,300,150);
        g.batch.draw(g.Purple,650,150);
        g.batch.draw(g.White,1000,140);
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
    public void dispose() { //dispose of all things on screen
        g.dispose();
        g.B2Drender.dispose();
        g.batch.dispose();
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
                g.setScreen(new Menu(g));	//sets current screen to the menu screen
            }
        }

        if(Window_H - Gdx.input.getY() < 550 + 300 && Window_H - Gdx.input.getY() > 550) {

            if(Gdx.input.getX() > 250 && Gdx.input.getX() < 250 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,240,540);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.csel = 1;
                }
            }
            if(Gdx.input.getX() > 600 && Gdx.input.getX() < 600 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,590,540);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.csel = 2;
                }
            }
            if(Gdx.input.getX() > 950 && Gdx.input.getX() < 950 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,940,540);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.csel = 3;
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
                    g.csel = 4;
                }
            }
            if(Gdx.input.getX() > 600 && Gdx.input.getX() < 600 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,590,140);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.csel = 5;
                }
            }
            if(Gdx.input.getX() > 950 && Gdx.input.getX() < 950 + 300) {

                g.batch.begin();
                g.batch.draw(g.outline,940,140);
                g.batch.end();

                if(Gdx.input.isTouched()) {
                    g.csel = 6;
                }
            }
        }
    }
}
