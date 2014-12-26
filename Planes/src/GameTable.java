import javax.swing.*;

import java.awt.*;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class GameTable extends JPanel implements Runnable, Observer {

	public JSplitPane split_pane;
	public GameFlowDisplay game_flow_display;
	public GameFlowControl game_flow_control_panel;
	public GameTablePlayerReadyButtonObservable game_player_ready_button_observable;
	public GameFrameRepaintObservable game_frame_repaint_from_game_table;

	public GameTable(GameFlowDisplay game_display,
			GameFlowControl game_control) {

		this.setLayout(new BorderLayout());
		this.game_flow_display = game_display;
		this.game_flow_control_panel = game_control;
		
		this.game_flow_control_panel.place_plane_button_observable.addObserver(this);
		this.game_flow_control_panel.undo_plane_button_observable.addObserver(this);
		this.game_flow_control_panel.player_ready_button_observable.addObserver(this);
		this.game_flow_control_panel.turn_ready_button_observable.addObserver(this);
		this.game_flow_control_panel.plane_direction_up_observable.addObserver(this);
		this.game_flow_control_panel.plane_direction_right_observable.addObserver(this);
		this.game_flow_control_panel.plane_direction_down_observable.addObserver(this);
		this.game_flow_control_panel.plane_direction_left_observable.addObserver(this);
		
		this.game_player_ready_button_observable = new GameTablePlayerReadyButtonObservable();
		this.game_frame_repaint_from_game_table = new GameFrameRepaintObservable();
		
		this.addMouseListener(this.game_flow_display);

		JSplitPane split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		split_pane.setResizeWeight(0.8);
		split_pane.setDividerLocation(0.8);
		split_pane.setRightComponent(game_control);
		split_pane.setLeftComponent(game_display);
		this.add(split_pane, BorderLayout.CENTER);

		System.out.println("GameTable Created!");
	}

	public void run() {
		while (true) {
			// System.out.println(this.game_flow_display.to_repaint);
//			if (this.game_client.get_can_update_game_table() == true) {
//				System.out.println("GameMatrixReceived in Client!");
//				for (int i = 0; i < this.game_client.get_game_matrix_table().length; i++)
//					for (int j = 0; j < this.game_client
//							.get_game_matrix_table().length; j++)
//						if (this.game_client.get_game_matrix_table()[i][j] == 1)
//							this.game_flow_display.MatrixTable[i][j] = this.game_client
//									.get_game_matrix_table()[i][j];
//				// this.game_flow_display.setMatrixTable2(this.game_client.get_game_matrix_table());
//				this.game_client.set_can_update_game_table(false);
//				this.game_flow_display.to_repaint = true;
//			}
			
			
			if (this.game_flow_display.to_repaint == true) {
//				System.out.println("REPAINT FROM FRAME!");
				this.repaint();
				this.game_flow_display.to_repaint = false;
			}
			
			
			if (this.game_flow_control_panel.place_planes_button.isVisible() == false) {
				this.game_flow_display.plane_positioning_phase = false;
			}
			else
			{
				this.game_flow_display.plane_positioning_phase = true;
			}
			
			
//			if (this.game_flow_control_panel.to_repaint_flow_control == true) {
//				System.out.println("Repaint From Frame RadioButton!");
//				this.repaint();
//				this.game_flow_control_panel.to_repaint_flow_control = false;
//			}
			
			
//			if (this.game_flow_control_panel.plane_group_direction_changed == true) {
//				this.game_flow_display.plane_direction = this.game_flow_control_panel.plane_group_direction;
//				this.game_flow_control_panel.plane_group_direction_changed = false;
//			}
			
			
//			if (this.game_flow_control_panel.plane_button_pressed == true) {
//				this.game_flow_display.plane_button_pressed = true;
//				this.game_flow_control_panel.plane_button_pressed = false;
//			}
//			if (this.game_flow_control_panel.plane_undo_button_pressed == true) {
//				this.game_flow_display.plane_undo_button_pressed = true;
//				this.game_flow_control_panel.plane_undo_button_pressed = false;
//			}
			if (this.game_flow_control_panel.turn_ready_button_pressed == true) {
				int[][] table = new int[20][20];
				for (int i = 0; i < 20; i++) {
					for (int j = 0; j < 20; j++)
						table[i][j] = this.game_flow_display.MatrixTable2[i][j];
				}
				System.out.println("AFTER SETTING: " + table[0][0]);
				System.out.println("AFTER SETTING: " + table[0][1]);
				System.out.println("AFTER SETTING: " + table[1][0]);
				System.out.println("AFTER SETTING: " + table[1][1]);
				// this.game_client.send_game_turn_status(game_turn_status_new);
				this.game_flow_control_panel.turn_ready_button_pressed = false;
			}
			if (this.game_flow_control_panel.player_ready_button_pressed == true) {
				this.game_flow_control_panel.player_ready_button_pressed = false;
			}
		}
	}

	public void update(Observable o, Object arg) {
		System.out.println("Update from Observer");
		System.out.println(o.toString());
		
		if (o.equals(this.game_flow_control_panel.place_plane_button_observable))
		{
//			this.game_flow_display.to_repaint = false;
			this.game_flow_display.plane_button_pressed = true;
			System.out.println("PLACE####### PLANE BUTTONNN");
//			this.game_frame_repaint_from_game_table.RepaintObservable();
		}
		if (o.equals(this.game_flow_control_panel.plane_direction_down_observable))
		{
			this.game_flow_display.plane_direction = 2;
		}
		if (o.equals(this.game_flow_control_panel.plane_direction_up_observable))
		{
			this.game_flow_display.plane_direction = 0;
		}
		if (o.equals(this.game_flow_control_panel.plane_direction_left_observable))
		{
			this.game_flow_display.plane_direction = 3;
		}
		if (o.equals(this.game_flow_control_panel.plane_direction_right_observable))
		{
			this.game_flow_display.plane_direction = 1;
		}
		if (o.equals(this.game_flow_control_panel.undo_plane_button_observable))
		{
			this.game_flow_display.plane_undo_button_pressed = true;
		}
		if (o.equals(this.game_flow_control_panel.player_ready_button_observable))
		{
			this.game_player_ready_button_observable.buttonPress();
		}
		
	}

}

class GameTablePlayerReadyButtonObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class GameFrameRepaintObservable extends Observable {
	public void RepaintObservable() {
		this.setChanged();
		this.notifyObservers();
	}

}
