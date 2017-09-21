import java.awt.Point;
import java.util.HashSet;
import java.util.PriorityQueue;

public class SuperQueensSolver {
	private int board_size;
	public SuperQueensSolver(int in_board_size)
	{
		board_size = in_board_size;
	}
	
	public SuperQueensBoard solver()
	{
		SuperQueensBoard ini_board = new SuperQueensBoard(board_size);
		PriorityQueue<SuperQueensBoard> amazon_que = new PriorityQueue<>();
		amazon_que.add(ini_board);
		while(!amazon_que.peek().isFilled())
		{
			SuperQueensBoard cur_board = amazon_que.poll();
			HashSet<Point> nextMoves = cur_board.nextAvailables();
			for(Point each_point: nextMoves)
			{
				SuperQueensBoard next_board = cur_board.clone();
				next_board.fillNext(each_point);
				amazon_que.add(next_board);
			}
		}
		return amazon_que.poll();
	}
	
}
