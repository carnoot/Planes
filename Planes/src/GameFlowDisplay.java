//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
//import java.awt.GridBagLayout;
//import java.awt.GridLayout;
//import java.awt.LayoutManager;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameFlowDisplay extends JPanel implements MouseListener,
		ComponentListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int[][] MatrixTable;
	public int[][] MatrixTable2;
	public int[][] MatrixTable_undo_plane_backup;
	private int MatrixTableSize;
	private int MatrixOffset;
	private int xPosition;
	private int RectangleSize;
	private int[] coords;
	private int offset;
	private final int cell_clear_value = 0;
	private final int cell_plane_set_value = 3;
	private boolean pozitie_gresita;
	public volatile boolean to_repaint;
	public volatile boolean plane_button_pressed;
	public volatile boolean plane_undo_button_pressed;
	public int plane_direction;
	public volatile boolean plane_positioning_phase = true;

	public GameFlowDisplay(int offset) {
		this.offset = offset;

		this.initializeVariables();
		this.initializeMatrixTables();

		this.addMouseListener(this);
		this.addComponentListener(this);

		new Thread(this).start();

		System.out.println("FlowDisplay Created!");
		this.repaint();
	}

	public void initializeMatrixTables() {
		this.MatrixTable = new int[this.MatrixTableSize][this.MatrixTableSize];
		this.MatrixTable2 = new int[this.MatrixTableSize][this.MatrixTableSize];
		this.MatrixTable_undo_plane_backup = new int[this.MatrixTableSize][this.MatrixTableSize];
		for (int i = 0; i < this.MatrixTableSize; i++) {
			for (int j = 0; j < this.MatrixTableSize; j++) {
				this.MatrixTable[i][j] = 0;
				this.MatrixTable2[i][j] = 0;
				this.MatrixTable_undo_plane_backup[i][j] = 0;
			}
		}
	}

	public void initializeVariables() {
		this.coords = new int[3];
		this.MatrixOffset = this.offset;
		this.MatrixTableSize = 20;
		this.RectangleSize = 25;
		this.xPosition = 590;
		this.coords[2] = 1;
		this.pozitie_gresita = false;
		this.to_repaint = false;
		this.plane_button_pressed = false;
		this.plane_positioning_phase = true; // DE SCHIMBAT LA COMUNICAREA CU
												// SERVERUL
		this.plane_direction = 0;

	}

	public int[][] getMatrix() {
		return this.MatrixTable;
	}

	public void setPlane(int Xcenter, int Ycenter, int direction) {
		System.out.println("Plane SET!" + "x_center: " + Xcenter + "y_center: "
				+ Ycenter + "direction: " + direction);
		pozitie_gresita = false;
		if ((Xcenter < 2 || Xcenter > 17) || (Ycenter < 2 || Ycenter > 17)) {
			System.out.println("Nu poti sa pui acolo!");
			this.MatrixTable[Xcenter][Ycenter] = 0;
			this.MatrixTable_undo_plane_backup[Xcenter][Ycenter] = 0;
			pozitie_gresita = true;
		} else {
			switch (direction) {
			case 2:
				for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
					if (this.MatrixTable[i][Ycenter - 2] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // prima linie cu capul in jos

				for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
					if (this.MatrixTable[i][Ycenter + 1] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // doua linie cu capul in jos
				for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
					if (i != Ycenter)
					if (this.MatrixTable[Xcenter][i] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // linia perpendiculara pe celelalte cu capul in jos
				break;
			case 3:
				for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
					if (this.MatrixTable[Xcenter - 1][i] == this.cell_plane_set_value) {
						this.pozitie_gresita = true;
					}
				} // prima linie cu capul in stanga

				for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
					if (this.MatrixTable[Xcenter + 2][i] == this.cell_plane_set_value) {
						this.pozitie_gresita = true;
					}
				} // doua linie cu capul in stanga

				for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
					if (i != Xcenter)
					if (this.MatrixTable[i][Ycenter] == this.cell_plane_set_value) {
						this.pozitie_gresita = true;
					}
				} // linia perpendiculara pe celelalte cu capul in stanga

				break;
			case 0:
				for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
					if (this.MatrixTable[i][Ycenter - 1] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // prima linie cu capul in sus

				for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
					if (this.MatrixTable[i][Ycenter + 2] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // doua linie cu capul in sus
				for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
					if (i != Ycenter)
					if (this.MatrixTable[Xcenter][i] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // linia perpendiculara pe celelalte cu capul in sus
				break;
			case 1:
				for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
					if (this.MatrixTable[Xcenter + 1][i] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // prima linie cu capul in dreapta

				for (int i = Ycenter - 3; i < Ycenter + 3; i++) {
					if (this.MatrixTable[Xcenter - 2][i] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // doua linie cu capul in dreapta
				for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
					if (i != Xcenter)
					if (this.MatrixTable[i][Ycenter] == this.cell_plane_set_value)
						this.pozitie_gresita = true;
				} // linia perpendiculara pe celelalte cu capul in dreapta
				break;
			}
			if (this.pozitie_gresita == false) {
//				System.out.println("pozitie_gresita: " + this.pozitie_gresita);
				switch (direction) {
				case 2:
					for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
						this.MatrixTable[i][Ycenter - 2] = this.cell_plane_set_value;
					} // prima linie cu capul in jos

					for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
						this.MatrixTable[i][Ycenter + 1] = this.cell_plane_set_value;
					} // doua linie cu capul in jos
					for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
						this.MatrixTable[Xcenter][i] = this.cell_plane_set_value;
					} // linia perpendiculara pe celelalte cu capul in jos
					break;
				case 3:
					for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
						this.MatrixTable[Xcenter - 1][i] = this.cell_plane_set_value;
					} // prima linie cu capul in stanga

					for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
						this.MatrixTable[Xcenter + 2][i] = this.cell_plane_set_value;
					} // doua linie cu capul in stanga
					for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
						this.MatrixTable[i][Ycenter] = this.cell_plane_set_value;
					} // linia perpendiculara pe celelalte cu capul in stanga
					break;
				case 0:
					for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
						this.MatrixTable[i][Ycenter - 1] = this.cell_plane_set_value;
					} // prima linie cu capul in sus

					for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
						this.MatrixTable[i][Ycenter + 2] = this.cell_plane_set_value;
					} // doua linie cu capul in sus
					for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
						this.MatrixTable[Xcenter][i] = this.cell_plane_set_value;
					} // linia perpendiculara pe celelalte cu capul in sus
					break;
				case 1:
					for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
						this.MatrixTable[Xcenter + 1][i] = this.cell_plane_set_value;
					} // prima linie cu capul in dreapt

					for (int i = Ycenter - 2; i < Ycenter + 3; i++) {
						this.MatrixTable[Xcenter - 2][i] = this.cell_plane_set_value;
					} // doua linie cu capul in stanga
					for (int i = Xcenter - 2; i < Xcenter + 3; i++) {
						this.MatrixTable[i][Ycenter] = this.cell_plane_set_value;
					} // linia perpendiculara pe celelalte cu capul in stanga
						// this.repaint();
					break;
				}
			} else {
				this.MatrixTable[Xcenter][Ycenter] = 0;
			}
		}

	}

	public void setHit(int x, int y, int offset) {
		if (offset < this.xPosition) {
			System.out.println("x: " + x + " y: " + y);
			if (this.plane_positioning_phase == true) {
				this.MatrixTable[x][y] = 3;
				this.MatrixTable_undo_plane_backup[x][y] = 3;
			} else {
				this.MatrixTable[x][y] = 1;
				this.MatrixTable_undo_plane_backup[x][y] = 1;
			}
		}
		if (offset > this.xPosition) {
			if (this.MatrixTable2[x][y] == 2) {
				// change color
				this.MatrixTable2[x][y] = 1;
			} else {
				this.MatrixTable2[x][y] = 1;
			}
		}
		// this.MatrixTable_undo_plane_backup = this.MatrixTable;
//		this.MatrixTable2 = this.MatrixTable_undo_plane_backup;
	}

	public void receiveHit(int x, int y, int offset) {

	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		BasicStroke stroke = new BasicStroke(0.7f);
		// if (coords[2] == 1) {
		// coords[2] = 0;
//		System.out.println("paint");
		this.paintFlowDisplay(g2d, this.MatrixTable, this.MatrixTableSize,
				this.MatrixOffset, this.MatrixOffset, this.RectangleSize,
				stroke);
		this.paintFlowDisplay(g2d, this.MatrixTable2, this.MatrixTableSize,
				this.xPosition, this.MatrixOffset, this.RectangleSize, stroke);
		// }
	}

	public void paintFlowDisplay(Graphics2D g2d, int[][] matrix_to_draw,
			int matrix_table_size, int position_to_draw, int matrix_offset,
			int rectangle_size, BasicStroke stroke) {
		for (int i = 0; i < matrix_table_size; i++) {
			for (int j = 0; j < matrix_table_size; j++) {
				if (matrix_to_draw[i][j] == 0) {
					this.paintDisplayGrid(g2d, matrix_to_draw,
							position_to_draw, matrix_offset, i, j,
							rectangle_size, stroke);
				} else {
					this.paintDisplayFillRect(g2d, matrix_to_draw,
							position_to_draw, matrix_offset, i, j,
							rectangle_size, stroke);
					this.paintDisplayGrid(g2d, matrix_to_draw,
							position_to_draw, matrix_offset, i, j,
							rectangle_size, stroke);
				}
			}
		}
	}

	public void paintDisplayGrid(Graphics2D g2d, int[][] matrix_to_draw,
			int position_to_draw, int matrix_offset, int i, int j,
			int rectangle_size, BasicStroke stroke) {
		g2d.setColor(Color.BLACK);
		g2d.setStroke(stroke);
		g2d.drawRect(position_to_draw + i * rectangle_size, matrix_offset + j
				* rectangle_size, rectangle_size, rectangle_size);
	}

	public void paintDisplayFillRect(Graphics2D g2d, int[][] matrix_to_draw,
			int position_to_draw, int matrix_offset, int i, int j,
			int rectangle_size, BasicStroke stroke) {
		if (matrix_to_draw[i][j] == 1)
			g2d.setColor(Color.RED);
		else
		g2d.setColor(Color.BLUE);
		g2d.setStroke(stroke);
		g2d.fillRect(position_to_draw + 5 + i * rectangle_size, matrix_offset
				+ 5 + j * rectangle_size, 13, 13);
	}

	public void clearMatrixTableField(int x, int y, int[][] matrix_table) {
		matrix_table[x][y] = 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		if ((e.getX() > 20) && (e.getX() < 520) && (e.getY() > 20)
				&& (e.getY() < 569)) {
			coords[0] = (e.getX() - MatrixOffset) / RectangleSize;
			coords[1] = (e.getY() - MatrixOffset) / RectangleSize;
			if (e.getButton() == MouseEvent.BUTTON1) {
//				System.out.println(e.getX() + " " + e.getY());
				this.setHit(coords[0], coords[1], e.getX());
				coords[2] = 1;
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				this.clearMatrixTableField(coords[0], coords[1],
						this.MatrixTable);
				this.clearMatrixTableField(coords[0], coords[1],
						this.MatrixTable_undo_plane_backup);
			}
			this.to_repaint = true;
		}
		if ((e.getX() > 591) && (e.getX() < 1091) && (e.getY() > 20)
				&& (e.getY() < 569)) {
			coords[0] = (e.getX() - xPosition) / RectangleSize;
			coords[1] = (e.getY() - MatrixOffset) / RectangleSize;
			if (e.getButton() == MouseEvent.BUTTON1) {
//				System.out.println(e.getX() + " " + e.getY());
				this.setHit(coords[0], coords[1], e.getX());
				coords[2] = 1;
			}
			if (e.getButton() == MouseEvent.BUTTON3) {
				this.clearMatrixTableField(coords[0], coords[1],
						this.MatrixTable2);
			}
			this.to_repaint = true;
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void componentHidden(ComponentEvent e) {
	}

	public void componentMoved(ComponentEvent e) {
	}

	public void componentResized(ComponentEvent e) {
	}

	public void componentShown(ComponentEvent e) {

	}
	
	public void setMatrixTable2(int[][] matrix)
	{
		this.MatrixTable2 = matrix;
	}

	@Override
	public void run() {
		while (true) {
			if (this.plane_button_pressed == true) {
				System.out.println("Plane Button \"FLAG\" from FlowDisplay" + this.plane_direction);
				for (int i = 0; i < this.MatrixTableSize; i++)
					for (int j = 0; j < this.MatrixTableSize; j++)
				this.MatrixTable_undo_plane_backup[i][j] = this.MatrixTable[i][j];
				this.setPlane(this.coords[0], this.coords[1],
						this.plane_direction);
				this.plane_button_pressed = false;
				this.to_repaint = true;
			}
			if (this.plane_undo_button_pressed == true) {
				this.MatrixTable[coords[0]][coords[1]] = 0;
				this.MatrixTable_undo_plane_backup[coords[0]][coords[1]] = 0;
				for (int i = 0; i < this.MatrixTableSize; i++)
					for (int j = 0; j < this.MatrixTableSize; j++)
				this.MatrixTable[i][j] = this.MatrixTable_undo_plane_backup[i][j];
//				this.MatrixTable = this.MatrixTable_undo_plane_backup;
				this.to_repaint = true;
				this.plane_undo_button_pressed = false;
			}
		}

	}

}
