import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class GameFrame extends JFrame implements Observer {

	public GameTable game_table;
	
	public GameFrame(GameTable game_table){
		
		this.game_table = game_table;
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width, height);
		frame.add(this.game_table);
		frame.setVisible(true);
		
		System.out.println("GameFrame Created!");
	}

	@Override
	public void update(Observable o, Object arg1) {
		
		if (o.equals(this.game_table.game_frame_repaint_from_game_table)){
			System.out.println("GameFrame Repaint!");
			Graphics g = this.game_table.getGraphics();
			this.paintAll(g);
		}
		
	}

}
