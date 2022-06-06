import java.util.LinkedList;

class Timeline {
	private int cols;	//number of columns
	private int rows;	//number of rows
	private int maxHistory;	//max number of generations to track
	private LinkedList<boolean[][]> history;	//Total history of a run.
	private boolean[][] initState;	//starting state of a run from the most recently, manually edited generation.
	
	public Timeline(int maxHistory, int columns, int rows){
		this.cols = columns;
		this.rows = rows;
		this.maxHistory = maxHistory;
		
		initState = new boolean[this.cols][this.rows];
		history = new LinkedList<boolean[][]>();
		history.addLast(initState);
	}
	
	/*
	Purpose: Reads the most recent generation in the timeline to be displayed
	Effects: ...

	Called by:
	Life.render
	Life.checkCell
	Life.toggleCell
	Life.getCellNeighbors
	Life.step
	*/
	public boolean[][] getActiveGeneration(){
		return history.peekLast();
	}
	
	/*
	Purpose: Gets the starting state of the timeline.
	Effects: Able to restart a state from the last point in time it was manually changed.

	Called by:
	Life.toggleCell
	Life.presetDesign
	*/
	public boolean[][] getInitialGeneration(){
		return initState;
	}
	
	/*
	Purpose: Drops last state in timeline
	Effects: Able to view previous states

	Called by:
	Life.previousState
	*/
	public boolean moveToPrevGeneration(){
		if(history.size() > 1) {
			history.removeLast();
			return true;
		}
		
		return false;
	}
	

	/*
	Purpose: Appends the next state of the timeline to the list.
	Effects: New Active state and will remove the first states if the number of generations in the list exceeds the max.

	Called by:
	Life.redoState?
	Life.step
	*/
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
	
	/*
	Purpose: Clears all of the timeline and makes the initial state the only item in the timeline
	Effects: Will revert to the starting state as long as it wasn't preceded by clearInitialState()

	Called by:
	Life.clearGrid
	Life.redoState
	Life.toggleCell
	Life.presetDesign
	*/
	public void clearHistory() {
		history.clear();
		
		history.addLast(initState);
	}
	
	/*
	Purpose: Resets the initial state to empty bool[][]
	Effects: Can no longer revert to the starting state

	Called by:
	Life.clearGrid
	*/
	public void clearInitialState() {
		for(int c = 0; c < cols; c++) {
			for(int r = 0; r < rows; r++) {
				initState[c][r] = false;
			}
		}
	}
}