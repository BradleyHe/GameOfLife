public class Cell 
{
	private int[][] offsets;
	private boolean alive;
	private Shape shape;

	public Cell(boolean a, Shape s)
	{
		alive = a;
		shape = s;

		if(shape == Shape.SQUARE)
		{
			// Create array of offsets that can be used to access all cells around this one
			offsets = new int[][] {
				{1, -1},
				{1, 0},
				{1, 1},
				{0, -1},
				{0, 1},
				{-1, -1},
				{-1, 0},
				{-1, 1}
			};
		}
	}

	public boolean isAlive()
	{
		return alive;
	}

	public void setState(boolean a)
	{
		alive = a;
	}

	public int[][] getOffsets()
	{
		return offsets;
	}
}