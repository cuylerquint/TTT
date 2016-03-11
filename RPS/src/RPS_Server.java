/**
 * Cuyler Quint
 * CPS240
 * 4/28/15
 * HmWk7
 * 			Purpose make a pvp RPS game
 * 
 * 			Sever class sets up muti threading for players to join and play
 * 			sessions, this class also sends and revives player choices,
 * 			with addition the sever sends the result of each game
 * 		
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class RPS_Server extends Application implements RPS_Constants {
	private int sessionNo = 1;

	@Override
	public void start(Stage primaryStage) throws Exception {
		TextArea taLog = new TextArea();
		Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
		primaryStage.setTitle("RPS Server");
		primaryStage.setScene(scene);
		primaryStage.show();

		new Thread(
				() -> {
					try {

						ServerSocket serverSocket = new ServerSocket(8000);
						Platform.runLater(() -> taLog.appendText(new Date()
								+ ": Server started at socket 8000\n"));
						while (true) {
							Platform.runLater(() -> taLog.appendText(new Date()
									+ ": Wait for players to join session "
									+ sessionNo + '\n'));

							Socket player1 = serverSocket.accept();

							Platform.runLater(() -> {
								taLog.appendText(new Date()
										+ ": Player 1 joined session "
										+ sessionNo + '\n');
								taLog.appendText("Player 1's IP address"
										+ player1.getInetAddress()
												.getHostAddress() + '\n');
							});

							new DataOutputStream(player1.getOutputStream())
									.writeInt(PLAYER1);
							Socket player2 = serverSocket.accept();
							Platform.runLater(() -> {
								taLog.appendText(new Date()
										+ ": Player 2 joined session "
										+ sessionNo + '\n');
								taLog.appendText("Player 2's IP address"
										+ player2.getInetAddress()
												.getHostAddress() + '\n');
							});

							new DataOutputStream(player2.getOutputStream())
									.writeInt(PLAYER2);

							Platform.runLater(() -> taLog.appendText(new Date()
									+ ": Start a thread for session "
									+ sessionNo++ + '\n'));

							new Thread(new HandleASession(player1, player2))
									.start();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}).start();
	}

	class HandleASession implements Runnable, RPS_Constants {
		private Socket player1;
		private Socket player2;
		private DataInputStream fromPlayer1;
		private DataOutputStream toPlayer1;
		private DataInputStream fromPlayer2;
		private DataOutputStream toPlayer2;
		private boolean continueToPlay = true;

		public HandleASession(Socket player1, Socket player2) {
			this.player1 = player1;
			this.player2 = player2;

		

		}

		@Override
		public void run() {
			System.out.println("run before try");
			try {
				DataInputStream fromPlayer1 = new DataInputStream(
						player1.getInputStream());
				DataOutputStream toPlayer1 = new DataOutputStream(
						player1.getOutputStream());
				DataInputStream fromPlayer2 = new DataInputStream(
						player2.getInputStream());
				DataOutputStream toPlayer2 = new DataOutputStream(
						player2.getOutputStream());

				toPlayer1.writeInt(1);
				while (true) {
					char charP1 = fromPlayer1.readChar();
					char charP2 = fromPlayer2.readChar();
					int result = getWinner(charP1, charP2);
					if (result == 1) {
						toPlayer1.writeInt(PLAYER1_WON);
						toPlayer2.writeInt(PLAYER1_WON);
						System.out.println("p1 win");
						sendMove(toPlayer1, charP2);
						sendMove(toPlayer2, charP1);
					} else if (result == 0) {
						toPlayer1.writeInt(DRAW);
						toPlayer2.writeInt(DRAW);
						System.out.println("draw");
						sendMove(toPlayer1, charP2);
						sendMove(toPlayer2, charP1);
					} else if (result == -1) {
						toPlayer1.writeInt(PLAYER2_WON);
						toPlayer2.writeInt(PLAYER2_WON);
						System.out.println("p2 win");
						sendMove(toPlayer1, charP2);
						sendMove(toPlayer2, charP1);
					}
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}

	}

	public int getWinner(char charP1, char charP2) {
		if (charP1 == (charP2)) {
			return 0;
		} else if ((charP1 == 'R') && (charP2 == 'S')) {
			return 1;
		} else if ((charP1 == 'S') && (charP2 == 'P')) {
			return 1;
		} else if ((charP1 == 'P') && (charP2 == 'R')) {
			return 1;
		} else {
			return -1;
		}

	}

	public void sendMove(DataOutputStream out, char choice) throws IOException {
		out.writeChar(choice);

	}

	public static void main(String[] args) {
		launch(args);
	}
}
