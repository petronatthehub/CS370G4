package com.racing.game3;

/*
Written by: Garrett Sullivan
Updated by Stephanie Petronella to integrate the mud feature
Sources/Credit: Connor Anderson's Youtube series "libGDX-Box2D"
https://www.youtube.com/watch?v=_y1RvNWoRFU&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI
OniBojan's Youtube video "LibGDX & Box2D Programming Tutorial "Top down car with drifting"-Part#4-Moving our car"
https://www.youtube.com/watch?v=MEn_I6eaMn0&t=898s
*/

import static com.racing.game3.extras.Constants.PPM;
import static com.racing.game3.extras.Constants.DEG;
import static com.racing.game3.extras.Constants.CARw;
import static com.racing.game3.extras.Constants.CARh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.racing.game3.extras.GameObjects;
import java.util.ArrayList;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;


public class Race implements Screen{

    private int P1y = 810;		//Player one start position y
    private int P1x = 1500;	//x
    public RacingGamev3 g;
    private boolean isInMud;
    private ArrayList<Boolean> mudTouching;
    private int currentLap;

    public class ObjectContactListener implements ContactListener {
        @Override
        public void beginContact(Contact con) {
            //this method is called automatically when a collision is sensed
            //the contact object holds information about the bodies involved
            Object o1 = con.getFixtureA().getBody().getUserData();
            Object o2 = con.getFixtureB().getBody().getUserData();

            if(o1 == "Car" || o2 == "Car") {

                if(o1 == "Mud" || o2 == "Mud") {
                    mudTouching.add(true);
                    System.out.println("They touch");
                }
                else if (o1 == "StartLine" || o2 == "StartLine") {
                    currentLap = currentLap + 1;
                    System.out.println(currentLap);
                    System.out.println("Crossed");
                }
            }
        }
        @Override
        public void endContact(Contact con) {
            Object o1 = con.getFixtureA().getBody().getUserData();
            Object o2 = con.getFixtureB().getBody().getUserData();

            if(o1 == "Car" || o2 == "Car") {

                if(o1 == "Mud" || o2 == "Mud") {
                    mudTouching.remove(mudTouching.size()-1);
                    System.out.println("They stop touching");
                }
                else if (o1 == "StartLine" || o2 == "StartLine") {
                    System.out.println("Off the line");
                }
            }
        }
        @Override
        public void preSolve(Contact con, Manifold oldManifold) { }

        @Override
        public void postSolve(Contact con, ContactImpulse impulse) { }

    }


    public Race(RacingGamev3 game) {
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

        //so that the game can trigger events when there is a collision between objects
        g.world.setContactListener(new ObjectContactListener());

        //For sensing mud/grass collisions
        isInMud = false;
        currentLap = -1;
        mudTouching = new ArrayList<Boolean>(6);

        //used to render the texture within the window
        g.batch = new SpriteBatch();
        //used to store a textures
        g.background = new Texture("Background.png");

        if(g.csel == 1)
            g.racecar = new Texture("cars/RedCar.png");
        if(g.csel == 2)
            g.racecar = new Texture("cars/YellowCar.png");
        if(g.csel == 3)
            g.racecar = new Texture("cars/GreenCar.png");
        if(g.csel == 4)
            g.racecar = new Texture("cars/BlueCar.png");
        if(g.csel == 5)
            g.racecar = new Texture("cars/PurpleCar.png");
        if(g.csel == 6)
            g.racecar = new Texture("cars/WhiteCar.png");

        //used to store the Tiled map and render it
        if(g.tsel == 1) {
            g.track = new TmxMapLoader().load("maps/TiledTrackwithMud3.tmx");
            P1x = 1500;
            P1y = 810;
        }
        if(g.tsel == 2) {
            g.track = new TmxMapLoader().load("maps/8trackV2.tmx");
            P1x = 1430;
            P1y = 1250;
        }
        if(g.tsel == 3) {
            g.track = new TmxMapLoader().load("maps/lavaTrackV2.tmx");
            P1x = 110;
            P1y = 810;
        }
        if(g.tsel == 4) {
            g.track = new TmxMapLoader().load("maps/snowTrackV2.tmx");
            P1x = 1240;
            P1y = 1050;
        }
        if(g.tsel == 5) {
            g.track = new TmxMapLoader().load("maps/beachTrackV2Mud2.tmx");
            P1x = 1210;
            P1y = 890;
        }

        //initializes driver by creating a car object and setting its start position
        g.driver = buildCar(P1x,P1y,CARw*2,CARh*2,false);
        //if(GameObjects.getCarDirection().equals("left"))
         //   g.driver.setTransform(g.driver.getPosition(), (float)1.571);

        g.rend1 = new OrthogonalTiledMapRenderer(g.track);
        //calls on the GameObjects class to construct the objects related to the Tiled map
        //GameObjects.ObjectLayer(g.world, g.track.getLayers().get("Walls").getObjects());
        GameObjects.ObjectLayer(g.world, g.track.getLayers());
        //GameObjects.ObjectLayer(g.world, g.track.getLayers().get("Position").getObjects());
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        //calls update and using
        update(Gdx.graphics.getDeltaTime());

        //sets background color of app window
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //renders and scales box2D world and camera
        g.B2Drender.render(g.world, g.cam.combined.scl(PPM));
        //renders the Tiled map
        g.rend1.render();
        //renders and place the texture on the body of the player
        g.batch.begin();
        g.batch.draw(g.racecar, g.driver.getPosition().x*PPM-(CARw/2), g.driver.getPosition().y*PPM-(CARh/2), CARw/2f, CARh/2f, CARw*1f, CARh*1f, 2f, 2f, g.driver.getAngle()*DEG, 0, 0, CARw, CARh, false, false);
        g.batch.end();
        //resets camera position after world and map is rendered
        resetCam();
        UserInput();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}


    @Override
    public void resize(int width, int height) {
        g.cam.setToOrtho(false, width*1.6f, height*1.6f);	//zooms out camera to better fit the map
        //g.cam.setToOrtho(false, width*2.5f, height*2.5f);	//zooms out camera to better fit the map
    }

    @Override
    public void dispose () {	//disposes of all things on the screen
        g.dispose();
        g.B2Drender.dispose();
        g.rend1.dispose();
        g.track.dispose();
        g.batch.dispose();
    }

    public void update(float delta) {
        //6 and 2 are to keep the refresh rate smooth
        g.world.step(1/60f,  6,  2);

        if(mudTouching.isEmpty())
            isInMud = false;
        else
            isInMud = true;

        //UserInput(delta);	//captures and implements user input
        g.batch.setProjectionMatrix(g.cam.combined);	//
        g.rend1.setView(g.cam); //resets the camera view bounds

        //if(currentLap == 3) {
        //    System.out.println("You've Won");
        //    Gdx.app.exit();
        //}
    }
    //updates the car's velocity and direction based on input
    public void UserInput() {

        Vector2 velocity = new Vector2(0,0);
        //gets input from keyboard and then in/decrements var
        if(Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            g.setScreen(new Menu(g));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.R)) {
            g.setScreen(new Race(g));
        }

        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.set(0, -60.0f);
            if(isInMud) {
                g.driver.setLinearDamping(5);
            }
            else {
                g.driver.setLinearDamping(1);
            }

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.set(0, 120.0f);
            if(isInMud) {
                g.driver.setLinearDamping(5f);
            }
            else {
                g.driver.setLinearDamping(0.5f);
            }

        }
        else {
            velocity.set(0,0);
            if(isInMud) {
                g.driver.setLinearDamping(5);
            }
            else {
                g.driver.setLinearDamping(1);
            }

        }

        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            if(isInMud) {
                g.driver.setAngularVelocity(-0.5f);
                g.driver.setLinearDamping(5);
            }
            else {
                g.driver.setAngularVelocity(-1.25f);
                g.driver.setLinearDamping(1);
            }

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            if(isInMud) {
                g.driver.setAngularVelocity(0.5f);
                g.driver.setLinearDamping(5);
            }
            else {
                g.driver.setAngularVelocity(1.25f);
                g.driver.setLinearDamping(1);
            }

        }
        else {
            g.driver.setAngularVelocity(0.0f);
        }

        if(!velocity.isZero()) {
            g.driver.applyForceToCenter(g.driver.getWorldVector(velocity), true);
        }
        else {
            g.driver.applyForceToCenter(g.driver.getWorldVector(velocity), true);
        }
    }
    //updates the camera's position
    public void resetCam() {
        //creates a 3D vector (position) and initializes it as the camera's current position
        Vector3 position = g.cam.position;
        //sets a new position for camera
        position.x = 800;
        position.y = 800;
        //sets the camera to its new position
        g.cam.position.set(position);
        //updates the camera with its new position
        g.cam.update();
    }

    public Body buildCar(int x, int y, int width, int height, boolean isStatic) {
        Body car;
        BodyDef def = new BodyDef();
        //if bool var is true then makes body static,(false = dynamic)
        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x/PPM,y/PPM);
        def.fixedRotation = false; //does not have a fixed rotation
        car = g.world.createBody(def);

        PolygonShape shape = new PolygonShape();
        //divided by 2 because it scales 2x, so 32x32 is (64x64)
        shape.setAsBox(width/2/PPM, height/2/PPM);

        car.createFixture(shape, 1.0f);

        //so that we can sense the car in a collision
        car.setUserData("Car");

        shape.dispose();

        return car;
    }
}

