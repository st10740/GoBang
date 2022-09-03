package gobang;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class gobang extends Application {

	// put your code here

	// Indicate which player has a turn, initially it is the W player
	private char whoseTurn = 'B';
	private int curi = 10, curj = 10;

	// Create and initialize cell
	private Cell[][] cell = new Cell[20][20];

	// Create and initialize a status label
	private Label lblStatus = new Label("'s turn to play");
	
	// Create a chess
	private Ellipse turn_circle = new Ellipse(6, 6, 6, 6);

	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {

		// put your code here
		// pane to hold cell
		GridPane gridpane = new GridPane();
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				gridpane.add(cell[i][j] = new Cell(i, j), j, i);
			}
		}
		
		//set bottom chess icon
		HBox hbox = new HBox(2);
		turn_circle.setStroke(Color.BLACK);
		turn_circle.setFill(Color.BLACK);
		hbox.getChildren().addAll(turn_circle, lblStatus);
		hbox.setAlignment(Pos.CENTER);
		
		
		
		//set border pane
		BorderPane borderPane = new BorderPane();
		BorderPane.setMargin(gridpane, new Insets(4, 4, 4, 4));
		BorderPane.setAlignment(hbox, Pos.CENTER);
		borderPane.setCenter(gridpane);
		borderPane.setBottom(hbox);
		
		

		// Create a scene and place it in the stage
		Scene scene = new Scene(borderPane, 500, 500);
		primaryStage.setTitle("GoBang"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		
		cellToRed(curi, curj); //the start cell is red
		
		scene.setOnKeyPressed(e -> { //keyboard event
			switch(e.getCode()) {
				case UP:
					if(curi-1>=0) {
						cellToBlack(curi, curj);
						curi--;
						cellToRed(curi, curj);
						//System.out.println("up");
					}
					break;
				case DOWN:
					if(curi+1<20) {
						cellToBlack(curi, curj);
						curi++;
						cellToRed(curi, curj);
						//System.out.println("down");
					}
					break;
				case LEFT:
					if(curj-1>=0) {
						cellToBlack(curi, curj);
						curj--;
						cellToRed(curi, curj);
						//System.out.println("left");
					}
					break;
				case RIGHT:
					if(curj+1<20) {
						cellToBlack(curi, curj);
						curj++;
						cellToRed(curi, curj);
						//System.out.println("right");
					}
					break;
				case SPACE: //put the chess
					cell[curi][curj].handleMouseClick();
					//System.out.println("space");
					break;
				case R: //restart the game
					cleanCell();
					whoseTurn='B';
					lblStatus.setText("'s Turn");
					turn_circle.setFill(Color.BLACK);
					break;
				case Q: //quit the game
					primaryStage.close();
					break;
				default:
					break;
			}
			
		});

	}

	public boolean isFull() {
		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 20; j++)
				if (cell[i][j].getToken() == ' ')
					return false;

		return true;

	}
	
	public void cleanCell() {
		for(int i=0;i<20;i++) {
			for(int j=0;j<20;j++) {
				cell[i][j].getChildren().clear();
				cell[i][j].token = ' ';
			}
		}
	}

	public boolean isWon(char token, int i, int j) {
		//row
		for(int a=j-4;a<=j;a++) {
			if(a<0) {
				continue;
			}
			boolean win_row = true;
			for(int b=0;b<5;b++) {
				
				if(a+b>=20 || cell[i][a+b].getToken() != token) {
					win_row = false;
					break;
				}
			}
			if(win_row==true) {
				return true;
			}
		}
		
		//column
		for(int c=i-4;c<=i;c++) {
			if(c<0) {
				continue;
			}
			boolean win_col = true;
			for(int d=0;d<5;d++) {
				if(c+d>=20 || cell[c+d][j].getToken() != token) {
					win_col = false;
					break;
				}
			}
			if(win_col==true) {
				return true;
			}
		}
		
		//left_top to right_bottom
		for(int e=i-4, f=j-4; e<=i && f<=j; e++, f++) {
			if(e<0 || f<0) {
				continue;
			}
			boolean win_lr = true;
			for(int g=0;g<5;g++) {
				if(e+g>=20 || f+g>=20 || cell[e+g][f+g].getToken() != token) {
					win_lr = false;
					break;
				}
			}
			if(win_lr==true) {
				return true;
			}
		}
		
		//right_top to left_bottom
		for(int h=i+4, k=j-4; h>=i && k<=j; h--, k++) {
			if(h>=20 || k<0) {
				continue;
			}
			boolean win_rl = true;
			for(int l=0;l<5;l++) {
				if(h-l<0 || k+l>=20 || cell[h-l][k+l].getToken() != token) {
					win_rl = false;
					break;
				}
			}
			if(win_rl==true) {
				return true;
			}
		}
		
		return false;
	}

	/** An inner class for a cell */
	public class Cell extends Pane {

		// put your code here
		// Token used for this cell
		private char token = ' ';
		private int i = 0, j = 0;

		public Cell(int i, int j) {
			this.i = i;
			this.j = j;
			setStyle("-fx-border-color: black");
			this.setPrefSize(2000, 2000);
			this.setOnMouseClicked(e -> {
				
				cellToBlack(curi, curj); //turn the color of previous chess cell black 
				handleMouseClick();
				cellToRed(curi, curj); //turn the color of current chess cell red
			});
			
			
		}

		/** Return token */
		public char getToken() {
			return token;
		}

		/** Set a new token */
		public void setToken(char c) {
			token = c;
			
			curi = i;
			curj = j;
			

			if (token == 'W') {
				Ellipse ellipse1 = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 4,
						this.getHeight() / 2 - 4);
				ellipse1.centerXProperty().bind(this.widthProperty().divide(2));
				ellipse1.centerYProperty().bind(this.heightProperty().divide(2));
				ellipse1.radiusXProperty().bind(this.widthProperty().divide(2).subtract(4));
				ellipse1.radiusYProperty().bind(this.heightProperty().divide(2).subtract(4));
				ellipse1.setStroke(Color.BLACK);
				ellipse1.setFill(Color.WHITE);

				// Add the lines to the pane
				this.getChildren().add(ellipse1);
			} else if (token == 'B') {
				Ellipse ellipse2 = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 4,
						this.getHeight() / 2 - 4);
				ellipse2.centerXProperty().bind(this.widthProperty().divide(2));
				ellipse2.centerYProperty().bind(this.heightProperty().divide(2));
				ellipse2.radiusXProperty().bind(this.widthProperty().divide(2).subtract(4));
				ellipse2.radiusYProperty().bind(this.heightProperty().divide(2).subtract(4));
				ellipse2.setStroke(Color.BLACK);
				ellipse2.setFill(Color.BLACK);

				getChildren().add(ellipse2); // Add the ellipse to the pane
			}
		}

		/* Handle a mouse click event */
		private void handleMouseClick() {
			// If cell is empty and game is not over
			if (token == ' ' && whoseTurn != ' ') {
				
				setToken(whoseTurn); // Set token in the cell

				// Check game status
				if (isWon(whoseTurn, i, j)) {
					//set chess color
					if(whoseTurn=='W') {
						turn_circle.setFill(Color.WHITE);
					}
					else if(whoseTurn=='B') {
						turn_circle.setFill(Color.BLACK);
					}
					
					lblStatus.setText("won! The game is over. "
							+ "Press 'r' to restart. Press 'q' to quit.");
					whoseTurn = ' '; // Game is over
					
					
				} else if (isFull()) {
					
					lblStatus.setText("Draw! The game is over"
							+ "Press 'r' to restart. Press 'q' to quit.");
					turn_circle.setStroke(Color.WHITE);
					turn_circle.setFill(Color.WHITE);
					whoseTurn = ' '; // Game is over
					
					
				} else {
					
					// Change the turn
					whoseTurn = (whoseTurn == 'W') ? 'B' : 'W';
					
					//set chess color
					if(whoseTurn=='W') {
						turn_circle.setFill(Color.WHITE);
					}
					else if(whoseTurn=='B') {
						turn_circle.setFill(Color.BLACK);
					}
					
					// Display whose turn
					lblStatus.setText("'s turn");
				}
			}
		}
		
		
		
	}
	
	private void cellToBlack(int i, int j) {
		cell[i][j].setStyle("-fx-border-color: black");
	}
	
	private void cellToRed(int i, int j) {
		cell[i][j].setStyle("-fx-border-color: red");
	}
	

	/**
	 * The main method is only needed for the IDE with limited JavaFX support. Not
	 * needed for running from the command line.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
