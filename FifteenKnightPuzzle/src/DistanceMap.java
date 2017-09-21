import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DistanceMap {
	private HashMap<HashSet<Point>, Integer> dist_map;
	private int height;
	private int width;
	private int knight_distance;
	
	public DistanceMap(int in_height, int in_width, int in_distance)
	{
		height = in_height;
		width = in_width;
		knight_distance = in_distance;
		dist_map = new HashMap<>();
	}
	
	public HashMap<HashSet<Point>, Integer> constructMap()
	{
		SimpleBoard board = new SimpleBoard();
		board.init(height, width);
		HashSet<Point> all_locs = board.getUnvisited();
		for(Point from: all_locs)
			for(Point to: all_locs)
			{
				SimpleBoard cur_board = new SimpleBoard();
				cur_board.init(height, width);
				HashSet<Point> cur_pair = new HashSet<>();
				cur_pair.add(from);
				cur_pair.add(to);
				cur_board.visit(from);
				calDistPair(cur_board, to);
			}
		return dist_map;
	}
	
	private int calDistPair(SimpleBoard in_board, Point p2)
	{
//		distance is in the record, dfs to find the min path similar to Manhattan distance 
		HashSet<Point> cur_pair = new HashSet<>();
		Point p1 = in_board.getBegin();
		cur_pair.add(p1);
		cur_pair.add(p2);
		if(dist_map.containsKey(cur_pair))
		{
			return dist_map.get(cur_pair);
		}
		else if(in_board.getBegin().distanceSq(p2) == knight_distance)
		{
			dist_map.put(cur_pair, 1);
			return 1;
		}
		else if(cur_pair.size() == 1) // same point 
		{
			dist_map.put(cur_pair, 0);
			return 0;
		}
		else
		{
			int min_dist = Integer.MAX_VALUE;
			HashSet<Point> reachable = pointsToSearch(in_board, p1, knight_distance);
			if(reachable.size() != 0) 
			{
				for(Point possible: reachable)
				{
					SimpleBoard rep_board = in_board.clone();
					rep_board.visit(possible);
					int cur_dist = calDistPair(rep_board, p2);
					if(cur_dist < min_dist) min_dist = cur_dist;
				}
			}
			else 
//				End and can't reach the goal state 
			{
				return Integer.MAX_VALUE/2;
			}
			dist_map.put(cur_pair, min_dist + 1);
			return min_dist + 1;
		}
		
	}
	
	private HashSet<Point> pointsToSearch(SimpleBoard in_board, Point in_p, int knight_dist_sq)
	{	
		HashSet<Point> out_points = new HashSet<>();
		for(Point each: in_board.getUnvisited())
		{
			if(each.distanceSq(in_p) == knight_dist_sq)
			{
				out_points.add(each);
			}
		}
		return out_points;
	}
	
	public String toString()
	{
		String out = "";
		for(HashSet<Point> each_set: dist_map.keySet())
		{
			out += "\n" ;
			for(Point each_p: each_set)
			{
				out += "(";
				out += each_p.x;
				out += ",";
				out += each_p.y;
				out += ")";
			}
			out += ":";
			out += dist_map.get(each_set);
		}
		return out;
	}
	
}
