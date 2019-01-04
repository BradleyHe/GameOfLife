import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class InputPanel extends JPanel
{
	private JCheckBox[] birthBoxes, surviveBoxes;
	private JPanel centerPanel, lCenterPanel, rCenterPanel, bottomPanel;
	private JTextField height, width;
	private JLabel lCenter, rCenter;

	public InputPanel()
	{
		setLayout(new BorderLayout());

		lCenter = new JLabel("Birth Condition");
		lCenter.setHorizontalAlignment(SwingConstants.CENTER);
		rCenter = new JLabel("Survival Condition");
		rCenter.setHorizontalAlignment(SwingConstants.CENTER);
		birthBoxes = new JCheckBox[9];
		surviveBoxes = new JCheckBox[9];
		height = new JTextField(5);
		width = new JTextField(5);

		lCenterPanel = new JPanel(new GridLayout(10, 1));
		rCenterPanel = new JPanel(new GridLayout(10, 1));
		lCenterPanel.setPreferredSize(new Dimension(150, 300));
		rCenterPanel.setPreferredSize(new Dimension(150, 300));
		lCenterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		rCenterPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lCenterPanel.add(lCenter);
		rCenterPanel.add(rCenter);

		for(int x = 0; x < birthBoxes.length; x++)
		{
			birthBoxes[x] = new JCheckBox(x + "");
			lCenterPanel.add(birthBoxes[x]);
			surviveBoxes[x] = new JCheckBox(x + "");
			rCenterPanel.add(surviveBoxes[x]);
		}

		centerPanel = new JPanel();
		centerPanel.add(lCenterPanel);
		centerPanel.add(rCenterPanel);

		bottomPanel = new JPanel();
		bottomPanel.add(new JLabel("Height:"));
		bottomPanel.add(height);
		bottomPanel.add(new JLabel("Width:"));
		bottomPanel.add(width);

		add(centerPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);
	}

	public boolean valid()
	{
		try
		{
			int a = Integer.parseInt(height.getText());
			int b = Integer.parseInt(width.getText());
			return !(a < 10 || b < 10);
		}

		catch(Exception e)
		{
			return false;
		}
	}

	public int[] getHeightAndWidth()
	{
		return new int[] { Integer.parseInt(height.getText()), Integer.parseInt(width.getText()) };
	}

	public ArrayList[] getConditions()
	{
		ArrayList<Integer> birthConditions = new ArrayList<Integer>();
		ArrayList<Integer> surviveConditions = new ArrayList<Integer>();

		for(int x = 0; x < birthBoxes.length; x++)
		{
			if(birthBoxes[x].isSelected())
				birthConditions.add(x);
			if(surviveBoxes[x].isSelected())
				surviveConditions.add(x);
		}

		return new ArrayList[] {birthConditions, surviveConditions};
	}
}