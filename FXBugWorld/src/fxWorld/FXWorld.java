package fxWorld;


import java.util.*;

import javafx.animation.*;
import javafx.application.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.*;
import javafx.util.*;

/**
 * 
 * @author aidanbrock
 *
 */
public class FXWorld extends Application{
	private int width = 600;
	private int height = 450;
	private boolean active = false;
	private ArrayList<Bug> bugs = new ArrayList<Bug>();
	private boolean isOrange = true;
	
	public void start(Stage primaryStage) throws Exception {
		Stage control = new Stage();

		primaryStage.setResizable(false);
		control.setResizable(false);
		
		Button go = new Button("Go"); //Button used to switch the active boolean for starting and stopping movement of the bugs.
		Button add = new Button("Add Bug"); //Button for adding a new bug.
		Button reset = new Button("Reset");
		
		go.setMinWidth(75); //Set width of buttons. Otherwise, they will auto-adjust and look uneven.
		add.setMinWidth(75);
		reset.setMinWidth(75);
		
		Group root = new Group(); //Used for the circles.
		Scene scene = new Scene(root, width, height);
		scene.setFill(Color.DARKORANGE);
		
		//go.setFont();
		
		/*
		 * Event handler for start/stop button. Changes active boolean between true and false.
		 */
		go.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				if(go.getText().equals("Go")) {
					go.setText("Stop");
					active = true;
				}
				else if(go.getText().equals("Stop")) {
					go.setText("Go");
					active = false;
				}
			}
		});
		
		
		/*
		 * Code to add a new bug to the screen. Adds the bug objects to the ArrayList and
		 * circles to the group.
		 */
		add.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				int xLoc = (int)Math.floor(Math.random()*(width-60)+30);
				int yLoc = (int)Math.floor(Math.random()*(height-60)+30);
				for(int i = 0; i < bugs.size(); i++) {
					int a = xLoc-bugs.get(i).getX();
					int b = yLoc-bugs.get(i).getY();
					while(Math.sqrt(a*a+b*b)<bugs.get(i).getRadius()+30) {
						xLoc = (int)Math.floor(Math.random()*(width-2*bugs.get(i).getRadius())+bugs.get(i).getRadius());
						yLoc = (int)Math.floor(Math.random()*(height-2*bugs.get(i).getRadius())+bugs.get(i).getRadius());
						a = xLoc-bugs.get(i).getX();
						b = yLoc-bugs.get(i).getY();
					}
				}
				Bug bug = new Bug();
				bug.setStart(xLoc, yLoc);
				Circle circ = new Circle(xLoc, yLoc, bug.getRadius());
				circ.setStroke(Color.DARKMAGENTA);
				circ.setFill(Color.GREENYELLOW);
				circ.setStrokeWidth(bug.getRadius()/6);
				bug.setCircle(circ);
				bugs.add(bug);
				root.getChildren().add(circ);
			}
		});
		
		reset.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				for(int i = bugs.size()-1; i >= 0; i--) {
					root.getChildren().remove(i);
					bugs.remove(i);
				}
				active = false;
				go.setText("Go");
			}
		});
		
		
		KeyFrame frame = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				for(int i = 0; i < bugs.size(); i++) {
					root.getChildren().get(i);
					
					if(bugs.get(i).checkBoundsX(root, scene, i)) {
						bugs.get(i).setdX(-bugs.get(i).getdX());
					}
					if(bugs.get(i).checkBoundsY(root, scene, i)) {
						bugs.get(i).setdY(-bugs.get(i).getdY());
					}
					
					for(int j = 0; j < bugs.size(); j++) {
						checkCollide(root, i, j, scene);
					}
					move(root, i);
					
					if(bugs.get(i).getRadius()<=0) {
						bugs.remove(i);
						root.getChildren().remove(i);
						System.out.println("Bug removed.");
					}
					
				}
			}
		});
		
		TimelineBuilder.create().cycleCount(javafx.animation.Animation.INDEFINITE).keyFrames(frame).build().play();
		
		primaryStage.setTitle("Animated Bug World");
		primaryStage.setScene(scene);
		
		HBox pane = new HBox(10);
		pane.getChildren().add(go);
		pane.getChildren().add(add);
		pane.getChildren().add(reset);
		pane.setAlignment(Pos.CENTER);
		pane.setPadding(new Insets(5,5,5,5));
		control.setHeight(75);
		control.setWidth(255);
		control.setX(540);
		control.setY(600);
		control.setTitle("Pest Control");
		Scene panel = new Scene(pane);
		control.setScene(panel);
		
		primaryStage.show();
		control.show();
	}
	
	public void checkCollide(Group root, int i, int j, Scene scene) {
		/*
		 * Eat variable determines which bug eats which on collision.
		 * The bigger the bug in relation to the other, the better chance it has of winning.
		 */
		int eat = (int)Math.floor(Math.random()*bugs.get(i).getRadius()-bugs.get(j).getRadius()/2);
		if(bugs.get(i).checkCollide(root, i, j, bugs)) { //Code inside these brackets runs if the checkCollide method returns true.
			
			/*
			 * Following code has option for changing the background between white and black every time there is a collision.
			 * This was added for comedic effect and is commented out by default. For a less jarring, and more disco-like effect,
			 * use HOTPINK and DARKORANGE instead.
			 */
//			if(isOrange) {
//				scene.setFill(Color.BLACK);
//				isOrange = false;
//			}
//			else if(!isOrange) {
//				scene.setFill(Color.WHITE);
//				isOrange = true;
//			}
			
			
			dirChange(i, j);
			
			/*
			 * If eat is greater than 0, the first bug wins. If it is less than 0, the second bug wins.
			 * If a bug is eaten, it changes to be blue. If it has been eaten but then eats another bug,
			 * it turns red. The green ones are the ones that have never been eaten.
			 */
			if(eat>0) {
				bugs.get(j).setRadius(-6);
				if(bugs.get(i).getRadius()<30) {
					bugs.get(i).setRadius(6);
					Circle g = bugs.get(i).replace(root, i, bugs);
					g.setStrokeWidth(g.getRadius()/6);
					g.setStroke(Color.DARKMAGENTA);
					g.setFill(Color.RED);
					root.getChildren().set(i, g);
				}
				
				Circle h = bugs.get(j).replace(root, j, bugs);
				h.setStrokeWidth(h.getRadius()/6);
				h.setStroke(Color.DARKMAGENTA);
				h.setFill(Color.AQUA);
				root.getChildren().set(j, h);
				if(bugs.get(i).getRadius()<=0) {
					bugs.remove(i);
					root.getChildren().remove(i);
					System.out.println("Bug removed.");
				}
			}
			else if(eat<0) {
				bugs.get(i).setRadius(-6);
				if(bugs.get(j).getRadius()<30) {
					bugs.get(j).setRadius(6);
					Circle h = bugs.get(j).replace(root, j, bugs);
					h.setStrokeWidth(h.getRadius()/6);
					h.setStroke(Color.DARKMAGENTA);
					h.setFill(Color.RED);
					root.getChildren().set(j, h);
				}
				Circle g = bugs.get(i).replace(root, i, bugs);
				g.setStrokeWidth(g.getRadius()/6);
				g.setStroke(Color.DARKMAGENTA);
				g.setFill(Color.AQUA);
				root.getChildren().set(i, g);
			}
		}
	}
	
	/**
	 * Calls the bug methods for changing their direction.
	 * @param i
	 * @param j
	 */
	public void dirChange(int i, int j) {
		int idX = bugs.get(i).getdX();
		int idY = bugs.get(i).getdY();
		int jdX = bugs.get(j).getdX();
		int jdY = bugs.get(j).getdY();
		bugs.get(i).changeDir(idX, idY, jdX, jdY);
		bugs.get(j).changeDir(jdX, jdY, idX, idY);
	}
	
	/**
	 * Moves the circle in the index i by its value of dx and dy. If the active boolean is false, 
	 * does not move the circles.
	 * @param root
	 * @param i
	 */
	
	public void move(Group root, int i) {
		if(active) {
			root.getChildren().get(i).setTranslateX(root.getChildren().get(i).getTranslateX()+bugs.get(i).getdX());
			root.getChildren().get(i).setTranslateY(root.getChildren().get(i).getTranslateY()+bugs.get(i).getdY());
		}
		if(!active) {
			root.getChildren().get(i).setTranslateX(root.getChildren().get(i).getTranslateX()+0);
			root.getChildren().get(i).setTranslateY(root.getChildren().get(i).getTranslateY()+0);
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	

}
