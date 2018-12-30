import java.util.Scanner;

public class GameOfLife
{
	public static void print(Cell[][] b)
	{
		for(int x = 0; x < b.length; x++)
		{
			for(int y = 0; y < b[0].length; y++)
				System.out.print(b[x][y].isAlive() ? "*" : " ");
			System.out.println();
		}
	}

	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		boolean[][] arr = new boolean[50][50];

		for(int x = 0; x < 50; x++)
		{
			for(int y = 0; y < 50; y++)
			{
				arr[x][y] = false;
			}
		}

		// create glider
		arr[0][1] = true;
		arr[1][2] = true;
		arr[2][0] = true;
		arr[2][1] = true;
		arr[2][2] = true;


		Board b = new Board(arr, Shape.SQUARE);
		
		for(;;)
		{
			print(b.getBoard());
			System.out.println("--------------------------------------------------------------");
			input.nextLine();
			b.generateNextGen();
		}
	}
}