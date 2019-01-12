import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GameRunner extends JFrame
{
	private GamePanel gamePanel;
	private InputPanel inputPanel;
	private JButton enter;
	private JLabel info;

	public static void main(String[] args) 
 	{
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() 
      {
        GameRunner frame = new GameRunner();
        setLocationRelativeTo(null);
        frame.setVisible(true);
      }
    });
	}

	public GameRunner()
	{
		info = new JLabel("Select the desired birth and survival conditions.");
		info.setHorizontalAlignment(SwingConstants.CENTER);
		inputPanel = new InputPanel();
		enter = new JButton("Enter");
		enter.addActionListener(new ButtonListener());

		setTitle("Game of Life");
		setLayout(new BorderLayout());
		add(info, BorderLayout.NORTH);
		add(inputPanel, BorderLayout.CENTER);
		add(enter, BorderLayout.SOUTH);
		setResizable(false);
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(inputPanel.valid())
			{
				int[] heightAndWidth = inputPanel.getHeightAndWidth();
				ArrayList[] conditions = inputPanel.getConditions();

				gamePanel = new GamePanel(heightAndWidth[0], heightAndWidth[1], conditions[0], conditions[1]);
				setVisible(false);
				getContentPane().removeAll();
				add(gamePanel, BorderLayout.CENTER);
				pack();
				setLocationRelativeTo(null);
				getContentPane().validate();
				setVisible(true);
			}

			else
			{
				info.setText("Invalid width and/or length.");
			}
		}
	}
}