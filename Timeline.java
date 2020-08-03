import java.util.Deque;
import java.util.LinkedList;

class Timeline {
	private int cols;
	private int rows;
	private int maxHistory;
	private Deque<boolean[][]> history;
	private boolean[][] initState;
	
	public Timeline(int maxHistory, int columns, int rows){
		this.cols = columns;
		this.rows = rows;
		this.maxHistory = maxHistory;
		
		initState = new boolean[this.cols][this.rows];
		history = new LinkedList<boolean[][]>();
	}
	
	public boolean[][] getActiveGeneration(){
		return history.peekLast();
	}
	
	public void moveToPrevGeneration(){
		history.removeLast();
	}
	
	public void addNextGen(boolean[][] nextGen){
//		boolean[][] nextGen = new boolean[cols][rows];
//		
//		for(int c = 0; c < cols; c++) {
//			for(int r = 0; r < rows; r++) {
//				nextGen[c][r] = temp[c][r];
//				
//				if(history.size() == 0) {
//					initState[c][r] = temp[c][r];
//				}
//			}
//		}
		
		history.addLast(nextGen);
	
		if(history.size() > maxHistory) {
			history.removeFirst();
		}
	}
	
	public void clearHistory() {
		history.clear();
	}
}