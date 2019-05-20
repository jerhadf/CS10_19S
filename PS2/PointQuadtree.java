import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, 
 * with children at the subdivided quadrants
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2015
 * @author CBK, Spring 2016, explicit rectangle
 * @author CBK, Fall 2016, generic with Point2D interface
 * 
 */
/**
 * PointQuadtree.java @param <E>
 * 
 * @author Jeremy Hadfield
 * @date Apr 23, 2019
 * @category Dartmouth CS10 19S, Assignment Name
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node (the point at the middle)
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether or not there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) 
				|| (quadrant==2 && c2!=null) 
				|| (quadrant==3 && c3!=null) 
				|| (quadrant==4 && c4!=null);
	}


	/**
	 * Helper function to check which quadrant a given point is in
	 * 
	 * @param pt — a Point object
	 * @return — a number between 1 and 4, reflecting the quadrant the point is in 
	 */
	public int checkQuadrant(E pt) {
		if (pt.getX() > point.getX() && pt.getY() < point.getY()) { 
			return 1; // the point is in quadrant 1
		}
		else if (pt.getX() < point.getX() && pt.getY() < point.getY()) {
			return 2; // the point is in quadrant 2
		}
		else if (pt.getX() < point.getX() && pt.getY() > point.getY()) {
			return 3; // the point is in quadrant 3
		} else {
			return 4; // the point is in quadrant 4
		}
	}
	
	/**
	 * Inserts the point into the tree
	 * 
	 * @param p2 — the point to be inserted
	 */
	public void insert(E p2) {
		if(checkQuadrant(p2) == 1) { // check if point p2 is in quadrant 1 
			if(this.hasChild(1)) { 
				c1.insert(p2); // recurse 
			} else { 
				c1 = new PointQuadtree<E>(p2, (int) point.getX(), y1, x2, (int) point.getY()); 
			}
		} 
		if(checkQuadrant(p2) == 2) { // check if point p2 is in quadrant 2
			if(this.hasChild(2)) { 
				c2.insert(p2); // recurse 
			} else { 
				c2 = new PointQuadtree<E>(p2, x1, y1, (int) point.getX(), (int) point.getY()); 
			}
		} 
		
		if (checkQuadrant(p2) == 3) { // check if point p2 is in quadrant 3 
			if(this.hasChild(3)) { 
				c3.insert(p2); // recurse 
			} else {  
				c3 = new PointQuadtree<E>(p2, x1, (int) point.getY(), (int) point.getX(), y2); 
			}
		}
		
		if(checkQuadrant(p2) == 4) { // check if point p2 is in quadrant 4 
			if(this.hasChild(4)) { 
				c4.insert(p2); // recurse 
			} else { 
				c4 = new PointQuadtree<E>(p2, (int) point.getX(), (int) point.getY(), x2, y2); 
			}
		} 
	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// create a list of all points in the quadtree, and then return the list's size 
		return allPoints().size(); 
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 * 
	 * @param result : the in-progress list of points in the quadtree
	 */
	public List<E> allPoints() {
		List<E> result = new ArrayList<E>(); 
		
		allPoints(result); 
		
		return result;
	}	

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		List<E> result = new ArrayList<E>(); // the list of points within the circle 
		
		findInCircle(cx, cy, cr, result); 
		
		return result; 
	}
	

	// TODO: YOUR CODE HERE for any helper methods
	
	/** 
	 * Helper method for allPoints(), recurses through the entire Quadtree and adds each point to a list of points
	 * @param result : the list of all points in the Quadtree
	 */
	public void allPoints(List<E> result) { 
		// BASE CASE: add the current point 
		result.add(point); 
		
		// RECURSIVE CASE: checks if the quadtree has a given child; if yes, add that child tree to the list of all points
		if(this.hasChild(1)) { 
			this.getChild(1).allPoints(result); // recurse
		} 
		if(this.hasChild(2)) { 
			this.getChild(2).allPoints(result); // recurse
		} 
		if(this.hasChild(3)) { 
			this.getChild(3).allPoints(result); // recurse
		} 
		if(this.hasChild(4)) { 
			this.getChild(4).allPoints(result); // recurse
		} 
	}
	
	/**
	 * helper method for findInCircle, recurses through the Quadtree to find points within the given circle
	 * 
	 * @param cx : circle center x
	 * @param cy : circle center y 
	 * @param cr : circle radius
	 * @param points_within : the list of all points within the circle (to be returned)
	 */
	public void findInCircle(double cx, double cy, double cr, List<E> points_within) {
	
		// check if the circle is within the current rectangle 
		if(Geometry.circleIntersectsRectangle(cx, cy, cr, this.getX1(), this.getY1(), this.getX2(), this.getY2())) { 
			// check if the current point is within the circle 
			if(Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) {
				points_within.add(point); 
			}
		}
		
		// recurse through each of the current node's children 
		if(this.hasChild(1)) { 
			this.getChild(1).findInCircle(cx, cy, cr, points_within); 
		}
		if(this.hasChild(2)) { 
			this.getChild(2).findInCircle(cx, cy, cr, points_within); 
		}
		if(this.hasChild(3)) { 
			this.getChild(3).findInCircle(cx, cy, cr, points_within); 
		}
		if(this.hasChild(4)) { 
			this.getChild(4).findInCircle(cx, cy, cr, points_within); 
		}
	}
	 
}
