import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class PuzzleBoard implements Comparable<PuzzleBoard> {
	private static int EMPTY = 0;
	private int height, width, move_1, move_2;
	private int steps;
	public HashMap<Integer, Point> goal, loc_map;
	public HashMap<Point, Integer> val_map;
	public HashMap<HashSet<Point>, Integer> dist_map;
	private String move_record;
	
	public PuzzleBoard() {}
	
	public PuzzleBoard(int in_h, int in_w, ArrayList<Integer> in_order, HashMap<HashSet<Point>, Integer> in_dist_map)
	{
//		set board dimensions 
		height = in_h;
		width = in_w;
		steps = 0;
		move_record = "";
//		set the map(position:number) and map(number:position)
		dist_map = in_dist_map;
		loc_map = new HashMap<>();
		val_map = new HashMap<>();
		for(int idx = 0; idx < in_order.size(); idx +=1)
		{
			int cur_j = idx%height;
			int cur_i = (idx - cur_j)/height;
			int cur_num = in_order.get(idx);
			loc_map.put(cur_num, new Point(cur_i, cur_j));
			val_map.put(new Point(cur_i, cur_j), cur_num);
		}
// 		set goal state 
		goal = new HashMap<>();
		for(int idx = 0; idx < height; idx +=1)
			for(int jdx = 0; jdx < width; jdx +=1)
			{
				int cur_num = idx * width + jdx;
				goal.put(cur_num + 1, new Point(idx, jdx));
			}
		goal.remove(height * width);
		goal.put(EMPTY, new Point(height - 1, width - 1));
	}
	
	public void setMoveRule(int in_x_move, int in_y_move)
	{
		move_1 = in_x_move;
		move_2 = in_y_move;
	}
	
	
	public HashSet<Integer> findPossibleMoves()
	{
		HashSet<Integer> out = new HashSet<>();
		Point empty_loc = loc_map.get(EMPTY);
		HashSet<Point> possible_locs = new HashSet<>();
		possible_locs.add(new Point(empty_loc.x + move_1, empty_loc.y + move_2));
		possible_locs.add(new Point(empty_loc.x + move_1, empty_loc.y - move_2));
		possible_locs.add(new Point(empty_loc.x - move_1, empty_loc.y + move_2));
		possible_locs.add(new Point(empty_loc.x - move_1, empty_loc.y - move_2));
		possible_locs.add(new Point(empty_loc.x + move_2, empty_loc.y + move_1));
		possible_locs.add(new Point(empty_loc.x + move_2, empty_loc.y - move_1));
		possible_locs.add(new Point(empty_loc.x - move_2, empty_loc.y + move_1));
		possible_locs.add(new Point(empty_loc.x - move_2, empty_loc.y - move_1));
		possible_locs.retainAll(val_map.keySet());
		for(Point each: possible_locs) out.add(val_map.get(each));
		return out;
	}
	
	
	public void moveTile(int num_1)
	{
		Point empty_loc = loc_map.get(EMPTY);
		Point cur_loc = loc_map.get(num_1);
		if(empty_loc.distanceSq(cur_loc) != Math.pow(move_1, 2) + Math.pow(move_2, 2))
			System.err.println("Illegal Move" + empty_loc + " " + cur_loc);
		else
		{
//			switch position
			loc_map.put(EMPTY, cur_loc);
			loc_map.put(num_1, empty_loc);
			val_map.put(cur_loc, EMPTY);
			val_map.put(empty_loc, num_1);
			steps += 1;
			move_record += this.toString() + "\n" 
			+ "step: " + steps + "\n";
		}
	}
	
	public String getMoves()
	{
		return move_record;
	}
	
	public int calTotalDist()
	{	
		int total_dist = 0;
		for(Integer each_num: loc_map.keySet())
		{
			HashSet<Point> cur_pair = new HashSet<>();
			cur_pair.add(loc_map.get(each_num));
			cur_pair.add(goal.get(each_num));
			total_dist += dist_map.get(cur_pair);
		}
		return total_dist;
	}
	
	public int getSteps()
	{
		return steps;
	}
	
	public boolean ifGoal()
	{
		for(int each_num: goal.keySet())
		{
			if(!goal.get(each_num).equals(loc_map.get(each_num)))
			{
				return false;
			}
		}
		return true;
	}
	
	public int compareTo(PuzzleBoard other_board)
	{
		int cur_total = this.steps+ this.calTotalDist();
		int other_total = other_board.getSteps() + other_board.calTotalDist();
		return cur_total - other_total;
	}
	
	public PuzzleBoard cloneBoard(PuzzleBoard in_board)
	{
		PuzzleBoard rep_board = new PuzzleBoard();
		rep_board.height = in_board.height;
		rep_board.width = in_board.width;
		rep_board.move_1 = in_board.move_1;
		rep_board.move_2 = in_board.move_2;
		rep_board.steps = in_board.steps;
		rep_board.goal = in_board.goal;
		rep_board.move_record = in_board.move_record;
		rep_board.loc_map = new HashMap<>();
		rep_board.val_map = new HashMap<>();
		rep_board.dist_map = in_board.dist_map;
		for(int each_val: in_board.loc_map.keySet())
		{
			Point ori_loc = in_board.loc_map.get(each_val);
			rep_board.loc_map.put(each_val,
					(Point)ori_loc.clone());
		}
		for(Point each_loc: in_board.val_map.keySet())
		{
			rep_board.val_map.put((Point)each_loc.clone(), in_board.val_map.get(each_loc));
		}
		rep_board.steps = in_board.steps;
		return rep_board;
	}	
	
	public String toString()
	{
		String out = "";
		String bar = "";
		for(int num = 0; num < width; num += 1) bar += "---" ;
		out += bar;
		int[][] grid = new int[height][width];
		for(int idx = 0; idx < height; idx +=1)
			for(int jdx = 0; jdx < width; jdx +=1)
			{
				grid[idx][jdx] = val_map.get(new Point(idx,jdx));
			}
		for(int idx = 0; idx < height; idx +=1)
		{
			out += "\n";
			for(int jdx = 0; jdx < width; jdx +=1)
			{
				out += grid[idx][jdx] + "|";
			}
			out += "\n" + bar;
		}
		return out;
	}
	
	public int hashCode()
	{
		return this.toString().hashCode();
	}
}
