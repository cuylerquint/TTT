/**
 * Cuyler Quint
 * CPS240
 * 4/28/15
 * HmWk7
 * 			Purpose make a pvp RPS game
 * 
 * 
 * 			Client class makes a gui for both player one and two,
 * 			based off of char info from sever the GUI is updated accordingly 
*/
import java.awt.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RPS_Client extends Application implements RPS_Constants {

	private TextArea textArea = new TextArea();
	private Label textAlerts = new Label();
	private Button playAgain = new Button("Play Again");
	private Button bRockP1, bPaperP1, bScissorsP1, bRockP2, bPaperP2,
			bScissorsP2;
	private Label player1 = new Label("PLAYER 1");
	private Label player2 = new Label("PLAYER 2");


	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private boolean continueToPlay = true;

	private String host = "localhost";
	private String mySide;
	private String otherSide;
	private char mChoice;
	private char otherChoice;

	FlowPane p1Flow;
	FlowPane p2Flow;

	@Override
	public void start(Stage primaryStage) throws Exception {
		BorderPane mainPane = new BorderPane();

		BorderPane playerPane = new BorderPane();
		BorderPane playerLabels = new BorderPane();

		p1Flow = addP1();
		p2Flow = addP2();

		playerPane.setLeft(p1Flow);
		playerPane.setRight(p2Flow);

		playerLabels.setLeft(player1);
		playerLabels.setRight(player2);
		playerLabels.setCenter(playAgain);
		mainPane.setTop(playerLabels);
		mainPane.setCenter(playerPane);
		mainPane.setBottom(textAlerts);

		Scene scene = new Scene(mainPane);
		primaryStage.setTitle("Rock Paper Scissors Cilent"); // Set the stage
																// title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage
		playAgain.setOnAction((e) -> {
			// connectToServer();
				System.out.println("new game");
				p1Flow.setVisible(false);
				p2Flow.setVisible(false);
				continueToPlay = true;
				if (mySide == "left") {
					p1Flow.setVisible(true);
					bRockP1.setVisible(true);
					bPaperP1.setVisible(true);
					bScissorsP1.setVisible(true);

				} else if (mySide == "right") {

					p2Flow.setVisible(true);
					bRockP2.setVisible(true);
					bPaperP2.setVisible(true);
					bScissorsP2.setVisible(true);

				}

				textAlerts.setText("new game select move");

			});
		connectToServer();
	}

	private void connectToServer() {

		try {
			// Create a socket to connect to the server
			Socket socket = new Socket(host, 8000);

			fromServer = new DataInputStream(socket.getInputStream());

			toServer = new DataOutputStream(socket.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		new Thread(
				() -> {
					try {
						int player = fromServer.readInt();
						if (player == PLAYER1) {
							mySide = "left";
							p2Flow.setVisible(false);
							Platform.runLater(() -> {
								textAlerts
										.setText("Connected to sever as Player 1, waiting for P2 to join");
							});
							fromServer.readInt();
							Platform.runLater(() -> textAlerts
									.setText("Player 2 has joined, Make a choice"));

						} else if (player == PLAYER2) {
							mySide = "right";
							p1Flow.setVisible(false);
							Platform.runLater(() -> {
								textAlerts
										.setText("Connected to sever as Player 2, Make a choice");
							});
						}
						System.out.println("before countie to play" + continueToPlay);
						while (continueToPlay) {
							
							if (player == PLAYER1) {
								receiveInfoFromServer();
							} else if (player == PLAYER2) {
								receiveInfoFromServer();
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}).start();
	}

	private void receiveInfoFromServer() throws IOException {
		int status = fromServer.readInt();
		System.out.println("status" + status);
		otherChoice = fromServer.readChar();
		if (status == PLAYER1_WON) {
			continueToPlay = false;
			if (mySide == "left") {
				if (otherChoice == 'R') {
					p2Flow.setVisible(true);
					bPaperP2.setVisible(false);
					bScissorsP2.setVisible(false);
					bRockP2.setVisible(true);
				} else if (otherChoice == 'P') {
					p2Flow.setVisible(true);
					bRockP2.setVisible(false);
					bScissorsP2.setVisible(false);
					bPaperP2.setVisible(true);
				} else if (otherChoice == 'S') {
					p2Flow.setVisible(true);
					bPaperP2.setVisible(false);
					bRockP2.setVisible(false);
					bScissorsP2.setVisible(true);
				}
				Platform.runLater(() -> textAlerts.setText("I won!"));
			} else if (mySide == "right") {
				if (otherChoice == 'R') {
					p1Flow.setVisible(true);
					bPaperP1.setVisible(false);
					bScissorsP1.setVisible(false);
					bRockP1.setVisible(true);
				} else if (otherChoice == 'P') {
					p1Flow.setVisible(true);
					bRockP1.setVisible(false);
					bScissorsP1.setVisible(false);
					bPaperP1.setVisible(true);
				} else if (otherChoice == 'S') {
					p1Flow.setVisible(true);
					bPaperP1.setVisible(false);
					bRockP1.setVisible(false);
					bScissorsP1.setVisible(true);
				}
				Platform.runLater(() -> textAlerts
						.setText("Player 1  has won!"));

			}
		} else if (status == PLAYER2_WON) {
			continueToPlay = false;
			if (mySide == "right") {
				if (otherChoice == 'R') {
					p1Flow.setVisible(true);
					bPaperP1.setVisible(false);
					bScissorsP1.setVisible(false);
					bRockP1.setVisible(true);
				} else if (otherChoice == 'P') {
					p1Flow.setVisible(true);
					bRockP1.setVisible(false);
					bScissorsP1.setVisible(false);
					bPaperP1.setVisible(true);
				} else if (otherChoice == 'S') {
					p1Flow.setVisible(true);
					bPaperP1.setVisible(false);
					bRockP1.setVisible(false);
					bScissorsP1.setVisible(true);
				}
				Platform.runLater(() -> textAlerts.setText("I won! "));
			} else if (mySide == "left") {
				if (otherChoice == 'R') {
					p2Flow.setVisible(true);
					bPaperP2.setVisible(false);
					bScissorsP2.setVisible(false);
					bRockP2.setVisible(true);
				} else if (otherChoice == 'P') {
					p2Flow.setVisible(true);
					bRockP2.setVisible(false);
					bScissorsP2.setVisible(false);
					bPaperP2.setVisible(true);
				} else if (otherChoice == 'S') {
					p2Flow.setVisible(true);
					bPaperP2.setVisible(false);
					bRockP2.setVisible(false);
					bScissorsP2.setVisible(true);
				}
				Platform.runLater(() -> textAlerts
						.setText("Player 2  has won!"));

			}
		} else if (status == DRAW) {
			continueToPlay = false;
			if (mySide == "right") {
				if (otherChoice == 'R') {
					p1Flow.setVisible(true);
					bPaperP1.setVisible(false);
					bScissorsP1.setVisible(false);
					bRockP1.setVisible(true);
				} else if (otherChoice == 'P') {
					p1Flow.setVisible(true);
					bRockP1.setVisible(false);
					bScissorsP1.setVisible(false);
					bPaperP1.setVisible(true);
				} else if (otherChoice == 'S') {
					p1Flow.setVisible(true);
					bPaperP1.setVisible(false);
					bRockP1.setVisible(false);
					bScissorsP1.setVisible(true);
				}

			} else if (mySide == "left") {
				if (otherChoice == 'R') {
					p2Flow.setVisible(true);
					bPaperP2.setVisible(false);
					bScissorsP2.setVisible(false);
					bRockP2.setVisible(true);
				} else if (otherChoice == 'P') {
					p2Flow.setVisible(true);
					bRockP2.setVisible(false);
					bScissorsP2.setVisible(false);
					bPaperP2.setVisible(true);
				} else if (otherChoice == 'S') {
					p2Flow.setVisible(true);
					bPaperP2.setVisible(false);
					bRockP2.setVisible(false);
					bScissorsP2.setVisible(true);
				}
				Platform.runLater(() -> textAlerts
						.setText("Game is over, no winner!"));

			}
			Platform.runLater(() -> textAlerts.setText("Draw game!!"));

		}
	}

	

	private void sendMove(char c) throws IOException {
		new Thread(() -> {
			try {
				toServer.writeChar(c);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}).start();

	}

	private FlowPane addP1() {
		FlowPane flow = new FlowPane();
		flow.setPadding(new Insets(0, 5, 0, 5));
		flow.setVgap(4);
		flow.setHgap(4);
		flow.setPrefWrapLength(200);

		ImageView imagePaper = new ImageView(new Image(("paper.jpg")));
		bPaperP1 = new Button();
		bPaperP1.setGraphic(imagePaper);

		ImageView imageRock = new ImageView(new Image(("rock.jpg")));
		bRockP1 = new Button();
		bRockP1.setGraphic(imageRock);

		ImageView imageScissors = new ImageView(new Image(("scissors.jpg")));
		bScissorsP1 = new Button();
		bScissorsP1.setGraphic(imageScissors);

		bPaperP1.setOnAction((e) -> {
			bRockP1.setVisible(false);
			bScissorsP1.setVisible(false);
			try {
				onClick('P');
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			textAlerts.setText("Paper selected");
		});

		bScissorsP1.setOnAction((e) -> {
			bRockP1.setVisible(false);
			bPaperP1.setVisible(false);
			try {
				onClick('S');
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			textAlerts.setText("Scissors selected");
		});
		bRockP1.setOnAction((e) -> {
			bPaperP1.setVisible(false);
			bScissorsP1.setVisible(false);
			try {
				onClick('R');
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			textAlerts.setText("Rock selected");
		});

		flow.getChildren().add(bPaperP1);
		flow.getChildren().add(bRockP1);
		flow.getChildren().add(bScissorsP1);
		return flow;

	}

	private FlowPane addP2() {
		FlowPane flow = new FlowPane();
		flow.setPadding(new Insets(0, 5, 0, 5));
		flow.setVgap(4);
		flow.setHgap(4);
		flow.setPrefWrapLength(200);

		ImageView imagePaper = new ImageView(new Image(("paper.jpg")));
		bPaperP2 = new Button();
		bPaperP2.setGraphic(imagePaper);

		ImageView imageRock = new ImageView(new Image(("rock.jpg")));
		bRockP2 = new Button();
		bRockP2.setGraphic(imageRock);

		ImageView imageScissors = new ImageView(new Image(("scissors.jpg")));
		bScissorsP2 = new Button();
		bScissorsP2.setGraphic(imageScissors);

		bPaperP2.setOnAction((e) -> {
			bRockP2.setVisible(false);
			bScissorsP2.setVisible(false);
			try {
				onClick('P');
			} catch (Exception e1) {

				e1.printStackTrace();
			}

			textAlerts.setText("Paper selected");
		});

		bScissorsP2.setOnAction((e) -> {
			bRockP2.setVisible(false);
			bPaperP2.setVisible(false);
			try {
				onClick('S');
			} catch (Exception e1) {

				e1.printStackTrace();
			}

			textAlerts.setText("Scissors selected");
		});
		bRockP2.setOnAction((e) -> {
			bPaperP2.setVisible(false);
			bScissorsP2.setVisible(false);
			try {
				onClick('R');
			} catch (Exception e1) {

				e1.printStackTrace();
			}

			textAlerts.setText("Rock selected");
		});

		flow.getChildren().add(bPaperP2);
		flow.getChildren().add(bRockP2);
		flow.getChildren().add(bScissorsP2);
		return flow;

	}

	private void onClick(char c) throws IOException {
		if ((c == 'R') || (c == 'P') || (c == 'S')) {
			
			System.out.println("onClick" + c);
			mChoice = c;
			sendMove(c);
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
