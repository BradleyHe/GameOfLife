import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Board
{
	private int[][] offsets;
	private Cell[][] array;
	private int gens, cutoutSize, height, width;
	private CellShape cellShape;

	public Board(int height, int width, CellShape s)
	{
		this.height = height;
		this.width = width;

		array = new Cell[height][width];
		gens = 0;
		cellShape = s;
		cutoutSize = 5;

		for(int x = 0; x < height; x++)
			for(int y = 0; y < width; y++)
				array[x][y] = new Cell(false);

		if(cellShape == CellShape.SQUARE)
		{
			// Create array of offsets that can be used to access all cells around given cell
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

	
	public void generateNextGen()
	{
		Cell[][] newGen = new Cell[array.length][array[0].length];

		for(int x = 0; x < array.length; x++)
		{
			for(int y = 0; y < array[0].length; y++)
			{
				int neighbors = getNeighbors(x, y);
				Cell currentCell = getCell(x, y);

				// different conditions separated for clarity, could be optimized
				if(currentCell.isAlive() && neighbors >= 2 && neighbors <= 3)
					newGen[x][y] = new Cell(true);

				// Normal game of life
				// else if(!currentCell.isAlive() && neighbors == 3)
				// 	newGen[x][y] = new Cell(true);

				// "HighLife" variation
				else if(!currentCell.isAlive() && (neighbors == 3 || neighbors == 6))
					newGen[x][y] = new Cell(true);

				else
					newGen[x][y] = new Cell(false);
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
		for(int[] offset : offsets)
		{
			try
			{
				numAlive += getCell(x + offset[0], y + offset[1]).isAlive() ? 1 : 0;
			}

			catch(Exception e) {}
		}

		return numAlive;
	}

	public void setCell(int x, int y, boolean state)
	{
		array[x][y].setState(state);
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

	public int getCutoutSize()
	{
		return cutoutSize;
	}

	public int getGenerations()
	{
		return gens;
	}

	public void reset()
	{
		for(int x = 0; x < height; x++)
			for(int y = 0; y < width; y++)
				array[x][y] = new Cell(false);
		gens = 0;
	}
}