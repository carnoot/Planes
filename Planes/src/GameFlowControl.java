import java.awt.*;
import java.awt.event.*;
import java.util.Observable;

import javax.swing.*;

public class GameFlowControl extends JPanel implements ActionListener {

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

	UndoPlaneButtonObservable undo_plane_button_observable;
	PlacePlaneButtonObservable place_plane_button_observable;
	PlayerReadyButtonObservable player_ready_button_observable;
	TurnReadyButtonObservable turn_ready_button_observable;
	PlaneDirectionUpObservable plane_direction_up_observable;
	PlaneDirectionRightObservable plane_direction_right_observable;
	PlaneDirectionDownObservable plane_direction_down_observable;
	PlaneDirectionLeftObservable plane_direction_left_observable;
	
	public GameFlowControl() {

		this.setLayout(new GridBagLayout());
		this.setPreferredSize(new Dimension(200, 200));

		this.undo_plane_button_observable = new UndoPlaneButtonObservable();
		this.place_plane_button_observable = new PlacePlaneButtonObservable();
		this.player_ready_button_observable = new PlayerReadyButtonObservable();
		this.turn_ready_button_observable = new TurnReadyButtonObservable();
		this.plane_direction_up_observable = new PlaneDirectionUpObservable();
		this.plane_direction_right_observable = new PlaneDirectionRightObservable();
		this.plane_direction_down_observable = new PlaneDirectionDownObservable();
		this.plane_direction_left_observable = new PlaneDirectionLeftObservable();
		
		this.to_repaint_flow_control = false;

		this.instruction_label = new JLabel();
		this.instruction_label
				.setText("Plane positioning is active! Right Click to deselect plane center!");
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 2;
		c.gridy = 3;

		
		this.place_planes_button = new JButton();
		this.place_planes_button.setText("Set Plane!");
		this.place_planes_button.addActionListener(this);
		this.place_planes_button.setVisible(true);

		this.undo_planes_button = new JButton();
		this.undo_planes_button.setText("Undo Plane Set!");
		this.undo_planes_button.addActionListener(this);
		this.undo_planes_button.setVisible(true);

		this.turn_ready = new JButton();
		this.turn_ready.setText("Turn Ready!");
		this.turn_ready.addActionListener(this);
		this.turn_ready.setVisible(false);

		this.player_ready = new JButton();
		this.player_ready.setText("Player Ready!");
		this.player_ready.addActionListener(this);
		this.player_ready.setVisible(false);

		this.set_plane_direction_up = new JRadioButton("Up");
		this.set_plane_direction_up.setActionCommand("up_select");
		this.set_plane_direction_up.setVisible(true);
		this.set_plane_direction_up.addActionListener(this);
		this.set_plane_direction_up.setSelected(true);

		this.set_plane_direction_right = new JRadioButton("Right");
		this.set_plane_direction_right.setActionCommand("right_select");
		this.set_plane_direction_right.addActionListener(this);
		this.set_plane_direction_right.setSelected(true);

		this.set_plane_direction_down = new JRadioButton("Down");
		this.set_plane_direction_down.setActionCommand("down_select");
		this.set_plane_direction_down.setSelected(true);
		this.set_plane_direction_down.addActionListener(this);

		this.set_plane_direction_left = new JRadioButton("Left");
		this.set_plane_direction_left.setActionCommand("left_select");
		this.set_plane_direction_left.setSelected(true);
		this.set_plane_direction_left.addActionListener(this);

		this.plane_direction_group = new ButtonGroup();
		this.plane_direction_group.add(set_plane_direction_up);
		this.plane_direction_group.add(set_plane_direction_right);
		this.plane_direction_group.add(set_plane_direction_down);
		this.plane_direction_group.add(set_plane_direction_left);

		this.add(this.place_planes_button);
		this.add(this.undo_planes_button);
		this.add(this.player_ready);
		this.add(this.turn_ready);
		this.add(this.set_plane_direction_up);
		this.add(this.set_plane_direction_right);
		this.add(this.set_plane_direction_down);
		this.add(this.set_plane_direction_left);
		this.add(this.instruction_label, c);

		this.plane_group_direction_changed = false;
		this.plane_button_pressed = false;
		this.plane_undo_button_pressed = false;

		System.out.println("FlowControl Created!");

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.place_planes_button){
		System.out.println("Planes Pressed!");
		this.number_of_planes++;
//		this.plane_button_pressed = true;
		this.to_repaint_flow_control = true;
		if (this.number_of_planes >= this.max_number_of_planes)
		{
			this.place_planes_button.setVisible(false);
			this.player_ready.setVisible(true);
		}
		this.place_plane_button_observable.buttonPress();
		}
		if (e.getSource() == this.undo_planes_button){
			System.out.println("Undo PRESSED!");
			this.number_of_planes--;
			if (this.number_of_planes < this.max_number_of_planes)
			{
				this.place_planes_button.setVisible(true);
				this.player_ready.setVisible(false);
			}
//			this.plane_undo_button_pressed = true;
			this.undo_plane_button_observable.buttonPress();
		}
		if (e.getSource() == this.player_ready){
			this.player_ready_button_observable.buttonPress();
//			this.player_ready_button_pressed = true;
			this.turn_ready.setVisible(true);
			this.player_ready.setVisible(false);
		}
		if (e.getSource() == this.turn_ready){
			this.turn_ready_button_observable.buttonPress();
//			this.turn_ready_button_pressed = true;
		}
//		if (e.getSource().equals(this.set_plane_direction_down) || e.getSource() == this.set_plane_direction_right || e.getSource() == this.set_plane_direction_left || e.getSource() == this.set_plane_direction_up)
//		{
//			this.to_repaint_flow_control = true;
//		}
		if (e.getSource().equals(this.set_plane_direction_up)){
			this.plane_group_direction = 0;
			this.plane_direction_up_observable.buttonPress();
//			this.plane_group_direction_changed = true;
		}
		if (e.getSource().equals(this.set_plane_direction_right)){
			this.plane_group_direction = 1;
			this.plane_direction_right_observable.buttonPress();
//			this.plane_group_direction_changed = true;
		}
		if (e.getSource().equals(this.set_plane_direction_down)){
			this.plane_group_direction = 2;
			this.plane_direction_down_observable.buttonPress();
//			this.plane_group_direction_changed = true;
		}
		if (e.getSource().equals(this.set_plane_direction_left)){
			this.plane_group_direction = 3;
			this.plane_direction_left_observable.buttonPress();
//			this.plane_group_direction_changed = true;
		}
	}
}

class UndoPlaneButtonObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class PlacePlaneButtonObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class PlayerReadyButtonObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class TurnReadyButtonObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class PlaneDirectionUpObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class PlaneDirectionRightObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class PlaneDirectionDownObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}

class PlaneDirectionLeftObservable extends Observable {
	public void buttonPress() {
		this.setChanged();
		this.notifyObservers();
	}

}
