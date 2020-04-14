/*
Adapted by: Garrett Sullivan

Sources/Credit: Connor Anderson's Youtube series "libGDX-Box2D"
https://www.youtube.com/watch?v=_y1RvNWoRFU&list=PLD_bW3UTVsElsuvyKcYXHLnWb8bD0EQNI
*/

package com.mytile.game.extras;



import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;


public class GameObjects {
	
	public static void ObjectLayer(World game, MapObjects objects) {
		//loop through all the objects in the layer
		for(MapObject object : objects) {
		
			Shape shape;
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
			//sets body def to static 
			bdef.type = BodyDef.BodyType.StaticBody;	
			//creates a body based on the body def
			body = game.createBody(bdef);
			//the body is formed into the current shape and sets the density to 1 (same as car)
			body.createFixture(shape, 1.0f);
			shape.dispose();
	
		}
	}
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
}
