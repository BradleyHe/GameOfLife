import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel
{
	private GridPanel gridPanel;
	private Rectangle[][] cells;
	private Board board;
	private JComboBox backgroundColorBox, cellColorBox;
	private JButton forward, clear, startAuto, gridEnable, random;
	private JLabel genLabel;
	private JPanel topPanel, graphicsPanel, iterationPanel;
	private JSlider timerSlider;
	private Timer auto;
	private Color backgroundColor, cellColor;
	private boolean gridOn;

	/* This program handles the problem of having a finite board size by only showing a smaller portion of a larger array which has boundaries.
		This way, it will seem as if the board is infinite, but only a certain portion is visible to the user.
	*/
	public GamePanel(int row, int col, ArrayList<Integer> birth, ArrayList<Integer> survive)
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);

		gridOn = true;
		board = new Board(row + 10, col + 10, birth, survive, CellShape.SQUARE);
		genLabel = new JLabel("Gen 0    ");
		forward = new JButton("Next Generation");
		forward.addActionListener(new ButtonListener());
		clear = new JButton("Clear");
		clear.addActionListener(new ButtonListener());
		startAuto = new JButton("Auto");
		startAuto.addActionListener(new ButtonListener());
		gridEnable = new JButton("Grid On/Off");
		gridEnable.addActionListener(new ButtonListener());
		random = new JButton("Random");
		random.addActionListener(new ButtonListener());

		auto = new Timer(250, new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				nextIteration();
			}
		});

		timerSlider = new JSlider(JSlider.HORIZONTAL, 0, 500, 250);
		timerSlider.setMajorTickSpacing(100);
		timerSlider.setMinorTickSpacing(25);
		timerSlider.setPaintTicks(true);
		timerSlider.setPaintLabels(true);
		timerSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e)
			{
				auto.setDelay(timerSlider.getValue());
			}
		});

		gridPanel = new GridPanel(row, col);
		gridPanel.setBackground(Color.WHITE);

		backgroundColorBox = new JComboBox(new String[] {"White", "Black"});
		backgroundColorBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				updateColors();
				gridPanel.setBackground(backgroundColor);
				setBackground(backgroundColor);
			}
		});

		cellColorBox = new JComboBox(new String[] {"White", "Black", "Green"});
		cellColorBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				updateColors();
			}
		});

		graphicsPanel = new JPanel();
		graphicsPanel.setBackground(Color.WHITE);
		graphicsPanel.add(clear);
		graphicsPanel.add(gridEnable);
		graphicsPanel.add(new JLabel("Background Color: "));
		graphicsPanel.add(backgroundColorBox);
		graphicsPanel.add(new JLabel("Cell Color: "));
		graphicsPanel.add(cellColorBox);
		graphicsPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		iterationPanel = new JPanel();
		iterationPanel.setBackground(Color.WHITE);
		iterationPanel.add(forward);
		iterationPanel.add(startAuto);
		iterationPanel.add(timerSlider);
		iterationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		topPanel = new JPanel();
		topPanel.setBackground(Color.WHITE);
		topPanel.add(genLabel);	
		topPanel.add(graphicsPanel);
		topPanel.add(iterationPanel);
		topPanel.add(random);
		
		add(topPanel);
		add(gridPanel);

		gridPanel.setPreferredSize(gridPanel.getMaximumSize());
		setPreferredSize(getPreferredSize());
	}

	// GridPanel handles the task of drawing the board out.
	public class GridPanel extends JPanel
	{
		private int rowCount, columnCount, cellSize;

		public GridPanel(int rowCount, int columnCount)
		{
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

			// Generate cellSize based on how many cells high the board is
		 	cellSize = rowCount < (screenSize.getHeight() - screenSize.getHeight() / 10) / 20 ? 20 : (int)(screenSize.getHeight() - screenSize.getHeight() / 10) / rowCount;

			this.rowCount = rowCount;
			this.columnCount = columnCount;

			MouseListener clickHandler = new MouseListener() {
				public void mouseClicked(MouseEvent e) {}

				public void mouseReleased(MouseEvent e) {}

				public void mousePressed(MouseEvent e) 
				{
					int column = e.getX() / cellSize;
					int row = e.getY() / cellSize;
				
					board.flipCell(row + board.getCutoutSize(), column + board.getCutoutSize());

					repaint();
				}

				public void mouseExited(MouseEvent e) {}

				public void mouseEntered(MouseEvent e) {}
			};
			addMouseListener(clickHandler);
		}

		@Override
		public Dimension getMaximumSize()
		{
			// need to increment by 1 so the entire grid gets shown
			return new Dimension(columnCount * cellSize + 1, rowCount * cellSize + 1);
		}

		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D)g.create();

			int panelWidth = getWidth();
			int panelHeight = getHeight();
			int cellSize = panelHeight / rowCount;

			if(cells == null)
			{
				cells = new Rectangle[rowCount][columnCount];
				for(int row = 0; row < rowCount; row++)
					for(int col = 0; col < columnCount; col++)
						cells[row][col] = new Rectangle(col * cellSize, row * cellSize, cellSize, cellSize);
			}

			g2d.setColor(cellColor);

			// draw all live cells
			for(int row = 0; row < rowCount; row++)
				for(int col = 0; col < columnCount; col++)
					if(board.getCellState(row + board.getCutoutSize(), col + board.getCutoutSize()))
						g2d.fill(cells[row][col]);

			if(gridOn)
			{
				g2d.setColor(Color.GRAY);
				for(Rectangle[] arr : cells)
					for(Rectangle cell : arr)
						g2d.draw(cell);
			}
			g2d.dispose();
		}
	}

	public void nextIteration()
	{
		board.generateNextGen();
		genLabel.setText("Gen " + board.getGenerations());
		gridPanel.repaint();

		// needed to reduce lag on linux 
		if(System.getProperty("os.name").equals("Linux"))
			Toolkit.getDefaultToolkit().sync();
	}

	public void updateColors()
	{
		switch((String)backgroundColorBox.getSelectedItem())
		{
			case "Black":
				backgroundColor = Color.BLACK;
				break;

			default:
				backgroundColor = Color.WHITE;
				break;
		}

		switch((String)cellColorBox.getSelectedItem())
		{
			case "White":
				cellColor = Color.WHITE;
				break;

			case "Green":
				cellColor = Color.GREEN;
				break;

			default:
				cellColor = Color.BLACK;
				break;
		}
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == forward)
			{
				nextIteration();
			}

			else if(e.getSource() == clear)
			{
				board.reset();
				auto.stop();
				genLabel.setText("Gen 0    ");
				gridPanel.repaint();
			}

			else if(e.getSource() == startAuto)
			{
				if(auto.isRunning())
					auto.stop();
				else
					auto.start();
			}

			else if(e.getSource() == gridEnable)
			{
				gridOn = !gridOn;
				gridPanel.repaint();
			}

			else if(e.getSource() == random)
			{
				int height = board.getHeight();
				int width = board.getWidth();

				for(int row = -5; row <= 5; row++)
					for(int col = -5; col <= 5; col++)
						if(Math.random() > 0.5)
							board.flipCell(height / 2 + row, width / 2 + col);
				gridPanel.repaint();
			}
		}	
	}
}