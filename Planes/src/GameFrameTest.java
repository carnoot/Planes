
public class GameFrameTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		GameFlowDisplay flow_display = new GameFlowDisplay(20);
		GameFlowControlElements flow_control_elements = new GameFlowControlElements();
		GameFlowControl flow_control = new GameFlowControl();
		GameTable game_table = new GameTable(flow_display, flow_control);
		new Thread(game_table).start();
		GameFrame game_frame = new GameFrame(game_table);
		GameClient game_client = new GameClient(game_frame);
		
		while(true){
			
		}
	}

}
