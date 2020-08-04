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
		history.addLast(initState);
	}
	
	public boolean[][] getActiveGeneration(){
		return history.peekLast();
	}
	
	public boolean[][] getInitialGeneration(){
		return initState;
	}
	
	public boolean moveToPrevGeneration(){
		if(history.size() > 1) {
			history.removeLast();
			return true;
		}
		
		return false;
	}
	
	public void addNextGen(boolean[][] snapshotRef){
		boolean[][] nextGen = new boolean[cols][rows];
		
		for(int c = 0; c < cols; c++) {
			for(int r = 0; r < rows; r++) {
				nextGen[c][r] = snapshotRef[c][r];
				
				if(history.size() == 0) {
					initState[c][r] = snapshotRef[c][r];
				}
			}
		}
		
		if(history.size() == 0) {
			initState = nextGen;
		}
		
		history.addLast(nextGen);
	
		if(history.size() > maxHistory) {
			history.removeFirst();
		}
	}
	
	public void clearHistory() {
		history.clear();
		
		history.addLast(initState);
	}
	
	public void clearInitialState() {
		for(int c = 0; c < cols; c++) {
			for(int r = 0; r < rows; r++) {
				initState[c][r] = false;
			}
		}
	}
}