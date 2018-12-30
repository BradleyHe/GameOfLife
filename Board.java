import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Board
{
	private Cell[][] array;
	private int gens, cutoutSize;
	private Shape shape;

	public Board(boolean[][] a, Shape s)
	{
		array = new Cell[a.length][a[0].length];
		gens = 0;
		shape = s;
		cutoutSize = 5;

		for(int x = 0; x < a.length; x++)
			for(int y = 0; y < a[0].length; y++)
				array[x][y] = new Cell(a[x][y], shape);
	}

	/* This program handles the problem of having a finite board size by only showing a smaller portion of a larger array which has boundaries.
		This way, it will seem as if the board is infinite, but only a certain portion is visible to the user.
	*/
	public void generateNextGen()
	{
		Cell[][] newGen = new Cell[array.length][array[0].length];

		for(int x = 0; x < array.length; x++)
		{
			for(int y = 0; y < array.length; y++)
			{
				int neighbors = getNeighbors(x, y);
				Cell currentCell = getCell(x, y);

				// different conditions separated for clarity, could be optimized
				if(currentCell.isAlive() && neighbors >= 2 && neighbors <= 3)
					newGen[x][y] = new Cell(true, shape);
				else if(!currentCell.isAlive() && neighbors == 3)
					newGen[x][y] = new Cell(true, shape);
				else
					newGen[x][y] = new Cell(false, shape);
			}
		}

		array = newGen;
		gens++;
	}

	// Returns the number of live neighbors 
	public int getNeighbors(int x, int y)
	{
		int numAlive = 0;
		Cell center = getCell(x, y);

		// Loop through all cells around the given location
		for(int[] offset : center.getOffsets())
		{
			try
			{
				numAlive += getCell(x + offset[0], y + offset[1]).isAlive() ? 1 : 0;
			}

			catch(Exception e) {}
		}

		return numAlive;
	}

	public Cell getCell(int x, int y)
	{
		return array[x][y];
	}

	public int getHeight()
	{
		return array.length;
	}

	public int getWidth()
	{
		return array[0].length;
	}

	// create a cutout of the original array
	public Cell[][] getBoard()
	{
		Cell[][] newArray = new Cell[array.length - cutoutSize * 2][array[0].length - cutoutSize * 2];

		for(int x = 0; x < newArray.length; x++)
			for(int y = 0; y < newArray[0].length; y++)
				newArray[x][y] = array[x + cutoutSize][y + cutoutSize];	

		return newArray;
	}
}