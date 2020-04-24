/*
Adapted by Garrett Sullivan
Updated by Stephanie Petronella to handle rectangle objects and different layer types

Sources/Credit: Connor Anderson's Youtube series "libGDX-Box2D"
https://www.youtube.com/watch?v=_y1RvNWoRFU&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI
*/

package com.mytile.game.extras;


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
	
	//changed from passing in all the objects of one layer to instead passing in all layers
	public static void ObjectLayer(World game, MapLayers layers) {
		//looping through all the layers
		for(MapLayer layer : layers) {
			
			//getting the list of objects in the layer
			MapObjects objects = layer.getObjects();
			
			//looping through all the objects in the layer
			for (MapObject object : objects) {
		
				Shape shape;
				
				//if object is a rectangle in Tiled
				if (object instanceof RectangleMapObject) {
                    			shape = getShapeFromRectangle((RectangleMapObject) object);
               			}
				
				//if object is a polygon in Tiled
				if(object instanceof PolygonMapObject) {
					shape = createPolygon((PolygonMapObject) object);
				}
				//if not, continue
				else {
					continue;
			}
			
				
			Body body;
			BodyDef bdef = new BodyDef();
				
			//sets body def to static because the body will not move
			bdef.type = BodyDef.BodyType.StaticBody;	
			
			//positions the body where it should be on the track; necessary just for rectangle objects 
                        if (object instanceof RectangleMapObject)
                        	bDef.position.set(getCenterForRectangle((RectangleMapObject) object));
				
			//creates a body based on the body def
			body = game.createBody(bdef);

			//the body is given shape and properties using fixtures
                	FixtureDef fDef = new FixtureDef();
                	fDef.shape = shape;
                	fDef.density = 1f;
			
		        //identifies if the layer is the collision layer; if so, it adds the fixture as is to each object
			if (layer.getName().contentEquals("Walls") || layer.getName().contentEquals("Wall"))
				body.createFixture(fDef);
				
			//if the layer is the mud layer, adds to each object the Sensor property so that a car can drive through it 
			//and it will send a signal to slow the car down
			if (layer.getName().contentEquals("Mud")) {
				fDef.isSensor = true;
				body.createFixture(fDef);
			}
			
			shape.dispose();
	
		}
	}
	
	//used to get the shape from a rectangle object in Tiled
	private static Shape getShapeFromRectangle(RectangleMapObject rectangle) {
		Rectangle rectangleBox = rectangle.getRectangle();
		PolygonShape polyShape = new PolygonShape();
		polyShape.setAsBox((float)(rectangleBox.getWidth()*0.5 / Constants.PPM),
			(float)(rectangleBox.getHeight()*0.5 / Constants.PPM));
		return polyShape;
   	}
	
	//used to get the shape from a polygon object in Tiled
	//chain shape is used because the walls are a series of connected lines that form a shape
	private static ChainShape createPolygon(PolygonMapObject polygon) {
		
		float[] vertices = polygon.getPolygon().getTransformedVertices();
		Vector2[] gameVertices = new Vector2[vertices.length / 2];
		
		for(int i=0;i<gameVertices.length;i++) {
			gameVertices[i] = new Vector2(vertices[i*2] / Constants.PPM, vertices[i*2+1] / Constants.PPM);
		}
		ChainShape cs = new ChainShape();
		cs.createChain(gameVertices);
	
		return cs;
	}
		
        //used for placing a rectangle shape over its corresponding body
	private static Vector2 getCenterForRectangle(RectangleMapObject rectangle) {
		Rectangle rectangleBox = rectangle.getRectangle();
		Vector2 center = new Vector2();
		rectangleBox.getCenter(center);
		return center.scl(1 / Constants.PPM);
    	}
}
