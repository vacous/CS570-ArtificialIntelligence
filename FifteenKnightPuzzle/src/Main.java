import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {
	public static void main(String[] args)
	{
//		calculate the distance matrix 
		int board_width = 4;
		int board_height = 4;
		int move1 = 1;
		int move2 = 2;
		int sq_dist = (int) (Math.pow(move1, 2) + (int)Math.pow(move2, 2));	
		DistanceMap dist_map = new DistanceMap(board_height, board_width, sq_dist);
		HashMap<HashSet<Point>, Integer> in_dist_map = dist_map.constructMap();
//		read in file
//		HERE!!!!!!!!!!!!!!!!!!!!!
		HashSet<ArrayList<Integer>> in_orders = boardReader("tilepuz-jqs33.txt");
		int solved_puzzle_counter = 0;
		for(ArrayList<Integer> cur_in_order: in_orders)
		{
			System.out.println(cur_in_order);
			long ini_time = System.nanoTime();
			PuzzleSolver solver = new PuzzleSolver();
			solver.readInPuzzle(board_height, board_width, cur_in_order, in_dist_map);
			solver.setRules(move1, move2);
			PuzzleBoard solution = solver.solvePuzzle();
			long elapsed_time = System.nanoTime() - ini_time;
			solved_puzzle_counter += 1;
			System.out.println("Solved Puzzle: " + solved_puzzle_counter + 
					"\n" + "Process Time: " + elapsed_time * Math.pow(10, -9) + " sec" +
					"\n" + cur_in_order + 
					"\n" + "Solution: \n" + solution.getMoves()
					+"\n");
		}
	}
	
	public static HashSet<ArrayList<Integer>> boardReader(String in_address)
	{
		HashSet<ArrayList<Integer>> ini_boards = new HashSet<>();
		ArrayList<Integer> board_list = new ArrayList<>(); 
		File in_file = new File(in_address);
		Scanner sc = null;
		try {
			sc = new Scanner(in_file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(sc.hasNextLine())
		{
			String cur_line = sc.nextLine();
			String[] cur_vals = cur_line.split(",");
			if(cur_vals.length == 1)
			{
				System.out.println(board_list);
				ini_boards.add(board_list);
				board_list = new ArrayList<>();
			}
			else
			{
				for(String each_val: cur_vals) 
					board_list.add(Integer.parseInt(each_val));	
			}
			if(!sc.hasNextLine()) ini_boards.add(board_list);
		}
		return ini_boards;
	}
}
