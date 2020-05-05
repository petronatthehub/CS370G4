package com.racing.game3.extras;
/*
Adapted by Garrett Sullivan
Updated by Stephanie Petronella to handle rectangle objects, regular polygon objects, and different layer types
Sources/Credit: Connor Anderson's Youtube series "libGDX-Box2D"
https://www.youtube.com/watch?v=_y1RvNWoRFU&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI
*/

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class GameObjects {

    private static Vector2 startingPoint, startingLinePoint;
    private static String carDirection;

    public static void ObjectLayer(World game, MapLayers layers) {

        for(MapLayer layer : layers) {

            MapObjects objects = layer.getObjects();
            //loop through all the objects in the layer
            for (MapObject object : objects) {

                Shape shape;

                //if object is a rectangle in Tiled
                if (object instanceof RectangleMapObject) {
                    shape = getShapeFromRectangle((RectangleMapObject) object);
                }
                //if object is a polygon in Tiled and is a mud object
                else if (object instanceof PolygonMapObject && layer.getName().equals("Mud")) {
                    shape = createComplexPolygon((PolygonMapObject) object);
                }
                //if object is a polygon in Tiled
                else if (object instanceof PolygonMapObject) {
                    shape = createPolygon((PolygonMapObject) object);
                }
                //if not, continue
                else {
                    continue;
                }

                Body body;
                BodyDef bDef = new BodyDef();
                //sets body def to static so that it doesn't move
                bDef.type = BodyDef.BodyType.StaticBody;
                //positions the body where it should be on the track
                if (object instanceof RectangleMapObject)
                    bDef.position.set(getCenterForRectangle((RectangleMapObject) object));
                //creates a body based on the body def
                body = game.createBody(bDef);

                //the body is given shape and properties using fixtures
                FixtureDef fDef = new FixtureDef();
                fDef.shape = shape;
                fDef.density = 1f;


                if (layer.getName().contentEquals("Walls") || layer.getName().contentEquals("Wall"))
                    body.createFixture(fDef);


                else if (layer.getName().contentEquals("Mud")) {
                    //Giving mud the sensor property means that the car can pass over it
                    //and that it can signal to somehow slow the car down
                    fDef.isSensor = true;
    
                    body.createFixture(fDef);
                    body.setUserData("Mud");
                } else if (layer.getName().contentEquals("Position")) {
                    if (object.getName().contentEquals("StartingLine")) {
                        fDef.isSensor = true;
                        body.createFixture(fDef);
                        body.setUserData("StartLine");
			    
			//experimenting with using the startling line to initially place a race car
                        startingLinePoint = new Vector2(getCenterForRectangle((RectangleMapObject) object));
                        startingLinePoint.x = startingLinePoint.x * 16;
                        startingLinePoint.y = startingLinePoint.y * 16;
			    
                    } else if (object.getName().contentEquals("StartingPoint")) {
			//experimenting with using the a starting point object to initially place a race car
                        startingPoint = new Vector2(getCenterForRectangle((RectangleMapObject) object));
                        startingPoint.x = startingPoint.x * 16;
                        startingPoint.y = startingPoint.y * 16;
                    }
                }

                shape.dispose();

            }
            if(layer.getName().contentEquals("Position"))
                setCarDirection();
        }
    }

    //private static Shape getShapeFromRectangle(Rectangle rectangle) {
    private static Shape getShapeFromRectangle(RectangleMapObject rectangle) {
        Rectangle rectangleBox = rectangle.getRectangle();
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox((float)(rectangleBox.getWidth()*0.5 / Constants.PPM),
                (float)(rectangleBox.getHeight()*0.5 / Constants.PPM));
        return polyShape;
    }

    //private static Vector2 getCenterForRectangle(Rectangle rectangle) {
    private static Vector2 getCenterForRectangle(RectangleMapObject rectangle) {
        Rectangle rectangleBox = rectangle.getRectangle();
        Vector2 center = new Vector2();
        rectangleBox.getCenter(center);
        return center.scl(1 / Constants.PPM);
    }

    //chain shape is used because the walls are a series of connected lines that form a shape
    private static ChainShape createPolygon(PolygonMapObject polygon) {

        float[] vertices = polygon.getPolygon().getTransformedVertices();
        Vector2[] gameVertices = new Vector2[vertices.length / 2];

        int i;
        for(i=0;i<gameVertices.length;i++) {
            gameVertices[i] = new Vector2(vertices[i*2] / Constants.PPM, vertices[i*2+1] / Constants.PPM);
        }
        ChainShape cs = new ChainShape();
        cs.createChain(gameVertices);

        return cs;
    }

    //for creating a closed polygon, unlike chain shape
    private static Shape createComplexPolygon(PolygonMapObject object) {
        PolygonShape ps = new PolygonShape();
        float[] vertices = object.getPolygon().getTransformedVertices();
        Vector2[] shapeVertices = new Vector2[vertices.length/2];

        int i;
        for(i=0;i<shapeVertices.length;i++) {
            shapeVertices[i] = new Vector2(vertices[i*2] / Constants.PPM, vertices[(i*2)+1] / Constants.PPM);
        }
        ps.set(shapeVertices);
        return ps;
    }

    //experimenting with using two position objects to face the race car towards the starting line
    private static void setCarDirection() {
        if((int)startingLinePoint.x > (int)startingPoint.x)
            carDirection = "right";
        else if ((int) startingLinePoint.x < (int) startingPoint.x)
            carDirection = "left";
        else if ((int) startingLinePoint.y > (int) startingPoint.y)
            carDirection = "up";
        else if ((int) startingLinePoint.y < (int) startingPoint.y)
            carDirection = "down";
        else carDirection = "error";
    }
	
    //these methods do actually work when used in Race.java to start the car in the correct spot,
    //though the starting points on the Tiled map itself can be adjusted, so it isn't actually used
    //by Race.java yet
    public static Vector2 getStartingPoint() {
        return startingPoint;
    }

    public static Vector2 getStartingLinePoint() {
        return startingLinePoint;
    }

    public static String getCarDirection() {
        return carDirection;
    }
}
