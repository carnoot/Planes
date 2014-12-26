import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;


public class GameFlowControlElements{

	JButton place_planes_button;
	JButton undo_planes_button;
	JButton turn_ready;
	JButton player_ready;
	JRadioButton set_plane_direction_up;
	JRadioButton set_plane_direction_right;
	JRadioButton set_plane_direction_down;
	JRadioButton set_plane_direction_left;
	ButtonGroup plane_direction_group;
	JLabel instruction_label;
	
	volatile boolean to_repaint_flow_control;
	volatile boolean plane_group_direction_changed;
	volatile boolean plane_button_pressed;
	volatile boolean plane_undo_button_pressed;
	volatile boolean turn_ready_button_pressed;
	volatile boolean player_ready_button_pressed;
	public final int max_number_of_planes = 3;
	public int number_of_planes = 0;
	public int plane_group_direction;
	
	public GameFlowControlElements() {	
	}
	
}
