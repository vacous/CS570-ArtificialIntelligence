import java.awt.Point;
import java.util.HashSet;

public class SimpleBoard {
	private HashSet<Point> unvisisted;
	private int height;
	private int width;
	private Point beigin_point;
	
	public void init(int limit_idx, int limit_jdx)
	{
		height = limit_idx;
		width = limit_jdx;
		initBoard();
	}
	
	private void initBoard()
	{
		unvisisted = new HashSet<>();
		for(int idx = 0; idx < height; idx +=1)
			for(int jdx = 0; jdx < width; jdx +=1)
				unvisisted.add(new Point(idx, jdx));
	}
	
	public void visit(Point p_v)
	{
		if(!unvisisted.contains(p_v)) System.out.println("Visited Point" + p_v);
		else
		{
			unvisisted.remove(p_v);
			beigin_point = p_v;
		}
	}
	
	public HashSet<Point> getUnvisited()
	{
		return unvisisted;
	}
	
	public Point getBegin()
	{
		return beigin_point;
	}
	
	
	private void setEqual(SimpleBoard other_board)
	{
		this.unvisisted = new HashSet<>();
		for(Point each: other_board.unvisisted)
		{
			this.unvisisted.add(new Point(each.x, each.y));
		}
		this.beigin_point = new Point(other_board.beigin_point.x, other_board.beigin_point.y);
		this.height = other_board.height;
		this.width = other_board.width;
	}
	
	public SimpleBoard clone()
	{
		SimpleBoard replicate = new SimpleBoard();
		replicate.setEqual(this);
		return replicate;
	}
	
	public String toString()
	{
		String out = "";
		for(int idx = 0; idx < height; idx +=1)
		{
			for(int jdx = 0; jdx < width; jdx +=1)
			{
				Point cur_Point = new Point(idx, jdx);
				if(unvisisted.contains(cur_Point)) out += "|" + "O";
				else if(cur_Point.equals(beigin_point)) out += "|" + "*";
				else out += "|" + "X";
			}
			out += "|" + "\n";
		}
		return out;
	}
}
