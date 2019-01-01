import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel
{
	private Board board;
	private JButton[][] grid;
	private JButton forward, clear, startAuto;
	private JLabel genLabel;
	private JPanel topPanel, bottomPanel;
	private Timer auto;

	/* This program handles the problem of having a finite board size by only showing a smaller portion of a larger array which has boundaries.
		This way, it will seem as if the board is infinite, but only a certain portion is visible to the user.
	*/
	public GamePanel(int height, int width)
	{
		int smaller = height > width ? width : height;
		int sizeOfButton = (int)(63 - 1.4 * smaller + 0.01 * Math.pow(smaller, 2));

		auto = new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				moveForward();
			}
		});

		grid = new JButton[height][width];
		board = new Board(height + 10, width + 10, CellShape.SQUARE);

		setLayout(new BorderLayout());

		// set up panel that will house the grid
		bottomPanel = new JPanel(new GridLayout(height, width));

		for(int x = 0; x < height; x++)
		{
			for(int y = 0; y < width; y++)
			{
				grid[x][y] = new JButton();
				grid[x][y].addActionListener(new ButtonListener());
				grid[x][y].setPreferredSize(new Dimension(sizeOfButton, sizeOfButton));
				grid[x][y].setBackground(Color.WHITE);
				bottomPanel.add(grid[x][y]);
			}
		}

		genLabel = new JLabel("Gen 0");
		forward = new JButton("Next Generation");
		forward.addActionListener(new ButtonListener());
		clear = new JButton("Clear");
		clear.addActionListener(new ButtonListener());
		startAuto = new JButton("Auto");
		startAuto.addActionListener(new ButtonListener());

		topPanel = new JPanel();
		topPanel.add(genLabel);	
		topPanel.add(forward);
		topPanel.add(clear);
		topPanel.add(startAuto);
		
		add(topPanel, BorderLayout.NORTH);
		add(bottomPanel, BorderLayout.CENTER);
	}

	public void update()
  {
  	for(int x = 0; x < grid.length; x++)
		{
			for(int y = 0; y < grid[0].length; y++)
			{
				grid[x][y].setBackground(board.getCell(x + board.getCutoutSize(), y + board.getCutoutSize()).isAlive() ? Color.BLACK : Color.WHITE);
			}
		}
	}

	public void clear()
	{
		board.reset();
		auto.stop();
		update();
		genLabel.setText("Gen 0");
	}

	public void moveForward()
	{
		board.generateNextGen();
		genLabel.setText("Gen " + board.getGenerations());
		update();
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == forward)
				moveForward();
			else if(e.getSource() == clear)
				clear();
			else if(e.getSource() == startAuto)
			{
				if(auto.isRunning())
					auto.stop();
				else
					auto.start();
			}

			else
			{
				int[] location = getLocation((JButton)(e.getSource()));
				Cell selected = board.getCell(location[0] + board.getCutoutSize(), location[1] + board.getCutoutSize());
				selected.setState(selected.isAlive() ? false : true);
				update();
			}	
		}	
	}

	public int[] getLocation(JButton c)
	{
		for(int x = 0; x < grid.length; x++)
		{
			for(int y = 0; y < grid[0].length; y++)
			{
				if(grid[x][y] == c)
					return new int[] {x, y};
			}
		}

		return null;
	}
}