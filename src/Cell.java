public class Cell 
{
	private boolean alive;

	public Cell(boolean a)
	{
		alive = a;
	}

	public boolean isAlive()
	{
		return alive;
	}

	public void setState(boolean a)
	{
		alive = a;
	}
}