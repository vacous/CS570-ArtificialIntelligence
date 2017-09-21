import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SuperQueensBoard implements Comparable<SuperQueensBoard>{
	private static int MARK = 1;
	private static int KNIGHT_1 = 1;
	private static int KNIGHT_2 = 2;
	private HashMap<Point, Integer> loc_map;
	public int cur_row_num, board_size;
	public HashSet<HashSet<Point>> attackPoints;
	public SuperQueensBoard(int in_board_size)
	{
		loc_map = new HashMap<>();
		attackPoints = new HashSet<>();
		cur_row_num = 0;
		board_size = in_board_size;
	}
	
	public HashSet<Point> nextAvailables()
	{
		HashSet<Integer> occpuied_jdxs = new HashSet<>();
		for(Point each_exist: loc_map.keySet())
		{
			occpuied_jdxs.add(each_exist.y);
		}
		HashSet<Point> out = new HashSet<>();
		for(int jdx = 0; jdx < board_size; jdx +=1)
		{
			if(!occpuied_jdxs.contains(jdx))
				out.add(new Point(cur_row_num, jdx));
		}
		return out;
	}
	
	public void fillNext(Point next_pos)
	{
		if(nextAvailables().contains(next_pos))
		{
			loc_map.put(next_pos, MARK);
			cur_row_num += 1;
			totalAttacks();
		}
		else 
			throw new IllegalArgumentException("Wrong row to place the next move" + next_pos);
	}
	
	private boolean ifAttacked(Point p1, Point p2)
	{
		double x_diff_abs = Math.abs(p1.x - p2.x);
		double y_diff_abs = Math.abs(p1.y - p2.y);
		if(p1.equals(p2)) return false;
		return 	x_diff_abs == y_diff_abs  || // Diagonal
				(x_diff_abs == KNIGHT_1 && y_diff_abs == KNIGHT_2) || // knight
				(x_diff_abs == KNIGHT_2 && y_diff_abs == KNIGHT_1); //knight
	}
	
	public void totalAttacks()
	{
		ArrayList<Point> point_list = new ArrayList<>(loc_map.keySet());
		for(int idx = 0; idx < point_list.size(); idx +=1)
			for(int jdx = idx; jdx < point_list.size(); jdx += 1)
			{
				Point cur_p1 = point_list.get(idx);
				Point cur_p2 = point_list.get(jdx);
				if(ifAttacked(cur_p1, cur_p2))
				{
					HashSet<Point> cur_conflict = new HashSet<>();
					cur_conflict.add(cur_p1);
					cur_conflict.add(cur_p2);
					attackPoints.add(cur_conflict);
				}
			}
	}
	
	public boolean isFilled()
	{
		return cur_row_num == board_size;
	}
	
	public SuperQueensBoard clone()
	{
		SuperQueensBoard other_board = new SuperQueensBoard(this.board_size);
		other_board.loc_map = new HashMap<>();
		for(Point each_point: this.loc_map.keySet())
		{
			other_board.loc_map.put(each_point, this.loc_map.get(each_point));
		}
		other_board.cur_row_num = this.cur_row_num;
		other_board.board_size = this.board_size;
		other_board.attackPoints = new HashSet<>();
		for(HashSet<Point> each_pair: this.attackPoints)
		{
			HashSet<Point> cur_pair = new HashSet<>();
			for(Point each_point: each_pair)
				cur_pair.add((Point)each_point.clone());
		}
		return other_board;
	}

	@Override
	public int compareTo(SuperQueensBoard other_board) 
	{
		// TODO Auto-generated method stub
		return (this.attackPoints.size() - other_board.attackPoints.size());
	}
	
	
	public String toString()
	{
		String out = "";
		for(int idx = 0; idx < board_size; idx += 1)
		{
			out += "-------------";
			out += "\n";
			for(int jdx = 0; jdx < board_size; jdx +=1)
			{
				if(loc_map.containsKey(new Point(idx,jdx)))
					out += " " + "[X]";
				else 
					out += " " + "[ ]";
			}	
			out += "\n";
		}
		out += "-------------";
		return out;			
	}
}
