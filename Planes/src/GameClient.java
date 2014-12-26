import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class GameClient implements Runnable, Observer {

	private Socket clientSocket = null;

	public volatile ObjectInputStream object_input_stream = null;
	public volatile ObjectOutputStream object_output_stream = null;
	public GameFrame game_frame;

	private String host = "localhost"; // IP of Server Machine
	private int portNumber = 2222;

	private int[][] game_matrix_table;
	private volatile boolean matrix_received = false;

	private LinkedBlockingDeque<int[][]> messages;
	private volatile boolean can_update_game_table = false;

	public GameClient(GameFrame game_frame) {
		this.game_frame = game_frame;
		this.messages = new LinkedBlockingDeque<int[][]>();

		this.game_frame.game_table.game_player_ready_button_observable
				.addObserver(this);

		try {
			this.clientSocket = new Socket(this.host, this.portNumber);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + host);
		} catch (IOException e) {
			System.err
					.println("Couldn't get I/O for the connection to the host "
							+ host);
		}

		try {
			this.object_output_stream = new ObjectOutputStream(
					this.clientSocket.getOutputStream());
			this.object_input_stream = new ObjectInputStream(
					this.clientSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (this.object_input_stream != null && this.clientSocket != null) {
			new Thread(this).start();
		}
	}

	public void run() {

		while (true) {
			System.out.println("Reading From Server!");
			if (this.matrix_received == true) {
				this.game_matrix_table = this.messages.getFirst();
				this.can_update_game_table = true;
				this.matrix_received = false;
			} else {
				try {
					this.messages.addFirst((int[][]) this.object_input_stream
							.readObject());
					this.matrix_received = true;
				} catch (IOException e) {
					System.err.println("IOException:  " + e);
				} catch (ClassNotFoundException e) {
					System.err.println("ClassNotFoundException: " + e);
					e.printStackTrace();
				}
			}
		}
	}

	public void send_game_turn_status() {
		try {
			System.out.println("Game_Turn_Status Sent!");
			// this.object_output_stream.writeObject();
			this.object_output_stream.flush();
		} catch (IOException err) {
			System.out.println(err);
		}
	}

	// public GamePlayerReady get_game_player_ready_status() {
	// return this.game_player_status;
	// }

	public void send_game_player_ready() {
		boolean player_ready = true;
		try {
			this.object_output_stream.writeObject((boolean) player_ready);
			this.object_output_stream.flush();
		} catch (IOException error) {
			System.out.println(error);
		}
	}

	public void send_game_player_status() {
		try {
			// this.game_player_status.set_ready_state(true);
			// this.object_output_stream.writeObject(this
			// .get_game_player_ready_status());
			this.object_output_stream.flush();
		} catch (IOException err) {
			System.out.println(err);
		}
	}

	// public GameTurnStatus get_game_turn_status() {
	// return this.game_turn_status;
	// }

	public boolean get_matrix_received() {
		return this.matrix_received;
	}

	public boolean get_can_update_game_table() {
		return this.can_update_game_table;
	}

	public void set_can_update_game_table(boolean value) {
		this.can_update_game_table = value;
	}

	// public GameMatrix get_game_matrix_received() {
	// return this.game_matrix_received;
	// }

	public void set_game_matrix_received(boolean matrix_received_set) {
		this.matrix_received = matrix_received_set;
	}

	public int[][] get_game_matrix_table() {
		return this.game_matrix_table;
	}

	@Override
	public void update(Observable o, Object arg) {

		if (o.equals(this.game_frame.game_table.game_player_ready_button_observable)) {
			this.send_game_player_ready();
		}

	}

	public static void main(String args[]) {

		GameFlowDisplay flow_display = new GameFlowDisplay(20);
		GameFlowControl flow_control = new GameFlowControl();
		GameTable game_table = new GameTable(flow_display, flow_control);
//		new Thread(game_table).start();
		GameFrame game_frame = new GameFrame(game_table);
		GameClient client = new GameClient(game_frame);
		new Thread(client.game_frame.game_table).start();

//		while (true) {
//
//		}

	}
}
