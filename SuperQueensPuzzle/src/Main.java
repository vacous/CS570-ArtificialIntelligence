import java.awt.Point;
import java.util.HashSet;

public class Main{
	public static void main(String[] args)
	{
//		HERE!!!!!!!!!!!!!!!!!!!!!
		SolvePuzzle(7);
	}

	public static void SolvePuzzle(int board_dim)
	{
		long ini_time = System.nanoTime();
		SuperQueensSolver board_solver = new SuperQueensSolver(board_dim);
		SuperQueensBoard result_board = board_solver.solver();
		System.out.println(result_board);
		HashSet<HashSet<Point>> attack_pairs = result_board.attackPoints;
		int counter = 0;
		for(HashSet<Point> each_pair: attack_pairs)
		{	
			counter += 1;
			System.out.println("Pair " + counter + ":" + 
			"\n" + each_pair);
		}
		System.out.println("Elapsed Time: " + 
		(System.nanoTime() - ini_time) * Math.pow(10, -9) + " sec");
	}
}
