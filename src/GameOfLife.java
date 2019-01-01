import javax.swing.*;

public class GameOfLife
{
	public static void main(String[] args)
	{
		// Largest amount of tiles that the game panel can hold on a 1920x1080 screen without having some tiles offscreen is 70 by 135. 

		JFrame frame = new JFrame();
		frame.add(new GamePanel(70, 135));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}