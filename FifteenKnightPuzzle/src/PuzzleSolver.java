import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class PuzzleSolver {
	private HashMap<HashSet<Point>, Integer> dist_map;
	public PuzzleBoard board;
	
	public void readInPuzzle(int board_h, int board_w, 
			ArrayList<Integer> in_board, HashMap<HashSet<Point>, Integer> in_dist_map)
	{
		dist_map = in_dist_map;
		board = new PuzzleBoard(board_h, board_w, in_board, dist_map);
	}
	
	public void setRules(int move1, int move2)
	{
		board.setMoveRule(move1, move2);
	}
	
	public PuzzleBoard solvePuzzle()
	{
		PriorityQueue<PuzzleBoard> board_que = new PriorityQueue<>();
		board_que.add(board);
		while(!board_que.peek().ifGoal())
		{
			PuzzleBoard board_to_expand = board_que.poll();
			HashSet<Integer> possible_moves = board_to_expand.findPossibleMoves();
			for(Integer each_move: possible_moves)
			{
				PuzzleBoard cur_board = board_to_expand.cloneBoard(board_to_expand);
				cur_board.moveTile(each_move);
				board_que.add(cur_board);
			}
		}
		return board_que.poll();
	}
}
