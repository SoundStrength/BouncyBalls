package fxWorld;

import java.util.ArrayList;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.*;

public class Bug {
	private int dx = ((int)Math.floor(Math.random()*2))*2-1;
	private int dy = ((int)Math.floor(Math.random()*2))*2-1;;
	private int x = 0;
	private int y = 0;
	private int radius = 30;
	private Circle c = new Circle();
	
	
	
	
	public void setStart(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Circle getCircle() {
		return c;
	}
	
	public void setCircle(Circle c) {
		this.c = c;
	}
	
	public int getdX() {
		return dx;
	}
	
	public void setdX(int i) {
		dx = i;
	}
	
	public int getdY() {
		return dy;
	}
	
	public void setdY(int i) {
		dy = i;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int i) {
		x = i;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int i) {
		y = i;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int eaten) {
		radius = radius + eaten;
	}
	
	/** 
	 * Returns a boolean after checking whether or not the bug is touching the left or right of the window.
	 * Requires the root  Group, the scene Scene and int i from the FXWorld class which calls it to be able to check everything.
	 * @param root The root group that stores the displayed circles.
	 * @param scene The scene of the main window. Is needed for checking the circle's position in relation to the boundary.
	 * @param i The number of the loop the for loop is on when it calls this method.
	 * */
	
	public boolean checkBoundsX(Group root, Scene scene, int i) {
		boolean edge = false;
		if(((Circle) root.getChildren().get(i)).getCenterX()+root.getChildren().get(i).getTranslateX()<((Circle) root.getChildren().get(i)).getRadius()||((Circle) root.getChildren().get(i)).getCenterX()+root.getChildren().get(i).getTranslateX()+((Circle) root.getChildren().get(i)).getRadius()>scene.getWidth()) {
			edge = true;
		}
		return edge;
	}
	
	/** 
	 * Returns a boolean after checking whether or not the bug is touching the top or bottom of the window.
	 * Requires the root  Group, the scene Scene and int i from the FXWorld class which calls it to be able to check everything.
	 * @param root The root group that stores the displayed circles.
	 * @param scene The scene of the main window. Is needed for checking the circle's position in relation to the boundary.
	 * @param i The number of the loop the for loop is on when it calls this method.
	 * */
	
	public boolean checkBoundsY(Group root, Scene scene, int i) {
		boolean edge = false;
		Circle iTemp = ((Circle) root.getChildren().get(i));
		if(iTemp.getCenterY()+iTemp.getTranslateY()<iTemp.getRadius()||iTemp.getCenterY()+iTemp.getTranslateY()+iTemp.getRadius()>scene.getHeight()) {
			edge = true;
		}
		return edge;
	}
	
	
	/**
	 * Returns boolean after checking to see whether the bug has collided with another bug.
	 * Checks distance between the centrepoints of the two bugs and if it is less than or equal to the
	 * total of the two radiuses, returns true.
	 * @param root The root group that stores the displayed circles.
	 * @param i The number of the loop the outer nested for loop is on when it calls this method.
	 * @param j The number of the loop the inner nested for loop is on when it calls this method.
	 * @param bugs The ArrayList that stores the bug objects.
	 */
	
	public boolean checkCollide(Group root, int i , int j, ArrayList<Bug> bugs) {
		boolean collision = false;
		
		Circle aa = (Circle) root.getChildren().get(i);
		Circle bb = (Circle) root.getChildren().get(j);
		int xAbs = (int) (aa.getCenterX()+aa.getTranslateX()-bb.getCenterX()-bb.getTranslateX());
		int yAbs = (int) (aa.getCenterY()+aa.getTranslateY()-bb.getCenterY()-bb.getTranslateY());
		int dist = (int)Math.sqrt(xAbs*xAbs+yAbs*yAbs);
		
		if(dist < bugs.get(i).getRadius()+bugs.get(j).getRadius()&&i!=j) {
			collision = true;
		}
		
		return collision;
	}
	
	/**
	 * Generates and returns a new circle when method is called. This is used when the circle's size changes.
	 * Also checks to see whether the expanded circle is outside the edges of the window. If it is, moves circle
	 * inside so it doesn't get stuck on the edge.
	 * @param root The root group that stores the displayed circles.
	 * @param i The number of the loop the for loop is on when it calls this method.
	 * @param bugs The ArrayList that stores the bug objects.
	 */
	
	public Circle replace(Group root, int i, ArrayList<Bug> bugs) {
		Circle temp = ((Circle) root.getChildren().get(i));
		double xTemp = temp.getCenterX()+temp.getTranslateX();
		double yTemp = temp.getCenterY()+temp.getTranslateY();
		int rTemp = bugs.get(i).getRadius();
		if(xTemp<rTemp) {
			xTemp = rTemp;
			dx = 1;
		}
		else if(xTemp + rTemp > 600) {
			xTemp = 600-rTemp;
			dx = -1;
		}
		
		if(yTemp < rTemp) {
			yTemp = rTemp;
			dy = 1;
		}
		else if(yTemp + rTemp > 450) {
			yTemp = 450 - rTemp;
			dy = -1;
		}
		Circle g = new Circle(xTemp, yTemp, bugs.get(i).getRadius());
		return g;
	}
	
	public void changeDir(int dX1, int dY1, int dX2, int dY2) {
		if(dX1 != dX2) {
			setdX(getdX()*-1);
		}
		if(dY1 != dY2) {
			setdY(getdY()*-1);
		}
	}
}
