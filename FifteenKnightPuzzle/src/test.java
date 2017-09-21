import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class test {
	public static void main(String[] args)
	{
		testPuzzleBoard();
		testSolver();
	}
	
	public static void testSimleBoard()
	{
//		test puzzle board 
		SimpleBoard test_board = new SimpleBoard();
		test_board.init(2, 2);
		test_board.visit(new Point(1, 1));
		System.out.println(test_board.getUnvisited());
		SimpleBoard other_board = test_board.clone();
		other_board.visit(new Point(1, 0));
		System.out.println(test_board.getUnvisited());
		System.out.println(other_board.getUnvisited());
//		test distance record map 
		DistanceMap test_map = new DistanceMap(4, 4, 5);
		test_map.constructMap();
		System.out.println(test_map.toString());
		System.out.println(3%2);
	}
	
	public static void testPuzzleBoard()
	{
		int board_size = 16;
		ArrayList<Integer> in_num = new ArrayList<>();
		for(int idx = 0; idx < board_size - 1; idx +=1)
		{
			in_num.add(idx+1);
		}
		in_num.add(0);
		DistanceMap dist_map = new DistanceMap(4, 4, 5);
		HashMap<HashSet<Point>, Integer> in_dist_map = dist_map.constructMap();
		PuzzleBoard board = new PuzzleBoard(4, 4, in_num, in_dist_map);
		System.out.println(board);
		board.setMoveRule(2, 1);
		System.out.println(board.findPossibleMoves());
		PuzzleBoard other_board = board.cloneBoard(board);
		board.moveTile(7);
		System.out.println(board.getMoves());
		System.out.println(board.getSteps());
	}
	
	public static void testSolver()
	{
		int board_width = 4;
		int board_height = 4;
		int move1 = 1;
		int move2 = 2;
		int sq_dist = (int) (Math.pow(move1, 2) + (int)Math.pow(move2, 2));	
		DistanceMap dist_map = new DistanceMap(board_height, board_width, sq_dist);
		HashMap<HashSet<Point>, Integer> in_dist_map = dist_map.constructMap();
		int[] temp_in_order = {10,11,3,13
				,5,4,1,2
				,9,8,6,12
				,0,14,15,7};
		ArrayList<Integer> in_order = new ArrayList<>();
		for(int idx = 0; idx < temp_in_order.length; idx += 1) in_order.add(temp_in_order[idx]);
		PuzzleSolver solver = new PuzzleSolver();
		solver.readInPuzzle(board_height, board_width, in_order, in_dist_map);
		solver.setRules(move1, move2);
		System.out.println(solver.solvePuzzle().getMoves());
	}
	
	
	
}
