package me.seanmaltby.lonearcher.core.utils;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.brashmonkey.spriter.Player;
import com.brashmonkey.spriter.Point;
import com.brashmonkey.spriter.Timeline;
import me.seanmaltby.lonearcher.core.Global;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Miscellaneous utility methods.
 */
public class Utils
{
	/**
	 * Performs the Graham's Scan algorithm on all of the points in the spriter bone,
	 * relative to the object's spriter player's center.
	 * @param player 	the player
	 * @param bone		the bone
	 * @param center 	the center of the object
	 * @return			vertices of convex polygon in box2d coordinates
	 */
	public static float[] grahamScanSpriterObject(Player player, Timeline.Key.Bone bone, Vector2 center)
	{
		//Create and populate an array of all of the points in the spriter object
		final Point[] points = new Point[4];

		com.brashmonkey.spriter.Rectangle rectangle = player.getBoudingRectangle(bone);
		points[0] = new Point(rectangle.left, rectangle.bottom);
		points[1] = new Point(rectangle.left, rectangle.top);
		points[2] = new Point(rectangle.right, rectangle.bottom);
		points[3] = new Point(rectangle.right, rectangle.top);

		//Swap points[0] with the point with the lowest y coordinate
		int lowestIndex = -1;
		float minY = Float.MAX_VALUE;
		float minX = Float.MAX_VALUE; //x coordinate is used if two or more points have the lowest y coordinate
		for(int i = 0; i < points.length; i++)
		{
			Point point = points[i];
			if(point.y < minY || (point.y == minY && point.x < minX))
			{
				minY = point.y;
				minX = point.x;
				lowestIndex = i;
			}
		}

		//Swap
		swap(points, 0, lowestIndex);

		//Sort the points in ascending order by their angle with points[0]
		Arrays.sort(points, new Comparator<Point>()
		{
			@Override
			public int compare(Point o1, Point o2)
			{
				if(o1 == points[0])
					return -1;
				if(o2 == points[0])
					return 1;
				Float angle1 = MathUtils.atan2(o1.y - points[0].y, o1.x - points[0].x);
				Float angle2 = MathUtils.atan2(o2.y - points[0].y, o2.x - points[0].x);
				return angle1.compareTo(angle2);
			}
		});

		int m = 1;	//Number of points on convex hull so far
		for(int i = 1; i < points.length; i++)
		{
			//While the next point isn't counterclockwise
			while(ccw(points[m-1], points[m], points[i]) <= 0)
			{
				if(m > 1)					//Drop the last added point to the convex hull
					m -= 1;
				else if(i == points.length)	//All points are collinear
					break;
				else						//Try the next point
					i += 1;
			}

			//Found the next point to add, add it and increment m
			m += 1;
			swap(points, i, m);
		}

		//Now, create the polygon
		float[] vertices = new float[points.length * 2];
		for(int i = 0; i < points.length; i++)
		{
			vertices[i * 2] = (points[i].x - center.x) * Global.WORLD_TO_BOX;
			vertices[i * 2 + 1] = (points[i].y - center.y) * Global.WORLD_TO_BOX;
		}
		return vertices;
	}

	/**
	 * Swaps the objects at index1 and index2 in the given array.
	 */
	private static void swap(Object[] array, int index1, int index2)
	{
		Object prev = array[index1];
		array[index1] = array[index2];
		array[index2] = prev;
	}

	/**
	 * Determines whether or not three points are in counter clockwise order, which all points should be in a convex hull.
	 * @param p1	p1
	 * @param p2	p2
	 * @param p3	p3
	 * @return		a float that indicates whether or not they are counter clockwise. If > 0, counter clockwise,
	 * 				else if < 0, clockwise, else if equal to 0, collinear
	 */
	private static float ccw(Point p1, Point p2, Point p3)
	{
		return (p2.x - p1.x) * (p3.y - p1.y) - (p2.y - p1.y) * (p3.x - p1.x);
	}

	/**
	 * Creates and returns a polygon with the same dimensions as the specified rectangle.
	 * @param rectangle		rectangle
	 * @return				polygon with dimensions of rectangle
	 */
	public static Polygon polygonFromRectangle(Rectangle rectangle)
	{
		float[] vertices = new float[8];
		vertices[0] = rectangle.x;
		vertices[1] = rectangle.y;
		vertices[2] = rectangle.x + rectangle.width;
		vertices[3] = rectangle.y;
		vertices[4] = rectangle.x + rectangle.width;
		vertices[5] = rectangle.y + rectangle.height;
		vertices[6] = rectangle.x;
		vertices[7] = rectangle.y + rectangle.height;
		return new Polygon(vertices);
	}

	public static void setParticleEffectScale(ParticleEffect effect, float scale)
	{
		for(ParticleEmitter emitter : effect.getEmitters())
		{
			float scaling = emitter.getScale().getHighMax();
			emitter.getScale().setHigh(scaling * scale);

			scaling = emitter.getScale().getLowMax();
			emitter.getScale().setLow(scaling * scale);

			scaling = emitter.getVelocity().getHighMax();
			emitter.getVelocity().setHigh(scaling * scale);

			scaling = emitter.getVelocity().getLowMax();
			emitter.getVelocity().setLow(scaling * scale);
		}
	}

	public static Vector2 toBox2dCoords(Vector2 screenCoordinates)
	{
		return new Vector2(screenCoordinates).scl(Global.WORLD_TO_BOX);
	}

	public static Vector2 toWorldCoordinates(Vector2 box2dCoordinates)
	{
		return new Vector2(box2dCoordinates).scl(Global.BOX_TO_WORLD);
	}
}
