import java.util.*;
import java.awt.*;
import java.io.*;

class Life{
	private Timeline time;
	private static String file = "gridPresets.txt";
	public static final int SQR_SIZE = 10;
//	public static boolean[][] startingGrid;
//	public static boolean[][] currentGrid;
	private boolean[][] snapshotGrid;
	private static int columns;
	private static int rows;
	private static PaintCanvas canvas;
	private static int generationCount = 0;

	public Life(int width, int height){
		columns = width / SQR_SIZE;
		rows = height / SQR_SIZE;

		System.out.printf("%d x %d\n", columns, rows);

		time = new Timeline(100, columns, rows);
		initGrid();
		
	}
 
	public void redoState(){
		generationCount = 0;
		
		time.clearHistory();
		time.addNextGen(time.getInitialGeneration());
		
//		for(int i = 0; i < columns; i++){
//			for(int j = 0; j < rows; j++){
//				currentGrid[i][j] = startingGrid[i][j];
//			}
//		}
	}

	public void setCanvas(PaintCanvas myCanvas){
		canvas = myCanvas;
	}
	
	public int getGenerationCount(){
		return generationCount;
	}
	
	private void initGrid(){
//		startingGrid = new boolean[columns][rows];
//		currentGrid = new boolean[columns][rows];
		snapshotGrid = new boolean[columns][rows];

		clearGrid();
	}

	public void render(Graphics2D g){
		//draw background grid
		int width = columns * SQR_SIZE;
		int height = rows * SQR_SIZE;
		boolean[][] generation = time.getActiveGeneration();
		
		g.drawRect(0, 0, width, height);

		for(int x = 0; x < columns; x++){
			g.drawLine(x * SQR_SIZE, 0, x * SQR_SIZE, height);
		}

		for(int y = 0; y < rows; y++){
			g.drawLine(0, y * SQR_SIZE, width, y * SQR_SIZE);
		}

		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
				if(generation[i][j]){
					g.setColor(Color.BLACK);
					g.fillRect(i * SQR_SIZE, j * SQR_SIZE, SQR_SIZE, SQR_SIZE);
				}
			}
		}
	}

	public boolean checkCell(int c, int r){
		try{
			return time.getActiveGeneration()[c][r];
		} catch (ArrayIndexOutOfBoundsException aioobe){
			//ignore
		} catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public void toggleCell(int c, int r){
		boolean[][] generationInitial = time.getInitialGeneration();
		boolean[][] generationCurrent = time.getActiveGeneration();
		
//		time.clearHistory();
		
		try{
			// startingGrid[c][r] = !startingGrid[c][r];
			generationInitial[c][r] = !generationInitial[c][r];
			
			for(int col = 0; col < generationInitial.length; col++){
				for(int row = 0; row < generationInitial[col].length; row++){
					generationInitial[col][row] = generationCurrent[col][row];
				}
			}
			generationCount = 0;
			
			time.clearHistory();
		} catch (ArrayIndexOutOfBoundsException aioobe){
			//ignore
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public void clearGrid(){
		generationCount = 0;
		time.clearInitialState();
		time.clearHistory();
		
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
//				currentGrid[i][j] = false;
				snapshotGrid[i][j] = false;
//				startingGrid[i][j] = false;
			}
		}
	}

	public void previousState() {
		if(time.moveToPrevGeneration()) {
			generationCount--;
		}
	}
	
	public void presetDesign(String title) throws IOException{
		FileInputStream inStream = null;
		Scanner in = null;
		Random rand = new Random();
		boolean[][] generationInitial = time.getInitialGeneration();
		
		clearGrid();

		if(title.equals("Random")){
			for(int c = 0; c < columns; c++){
				for(int r = 0; r < rows; r++){
					boolean temp = rand.nextBoolean();
//					startingGrid[c][r] = temp;
//					currentGrid[c][r] = temp;
					generationInitial[c][r] = temp;
				}
			}
			time.clearHistory();
			
			return;
		}

		try{
			inStream = new FileInputStream(file);
			in = new Scanner(inStream);

			while(in.hasNext()){
				if(in.nextLine().equals(title)){
					// System.out.println(in.nextByte() + " printed");
					for(int r = 0; r < rows; r++){
						String line = in.nextLine();
						for(int c = 0; c < line.length(); c++){
							if(line.charAt(c) == ';'){
								return;
							}
							if(line.charAt(c) == '1'){
								generationInitial[c][r] = true;
							} else {
								generationInitial[c][r] = false;
							}
						}
					}
				}
			}
			
			time.clearHistory();
		} catch(IOException e){
			System.out.println(e);
		}finally{
			in.close();
			inStream.close();
		}
	}

	private int getCellNeighbors(int c, int r){
		boolean[][] generationCurrent = time.getActiveGeneration();
		int neighbors = 0;
		
		if(c == 0){
			if(generationCurrent[c+1][r])
				neighbors++;
			if(generationCurrent[columns-1][r])
				neighbors++;
		
			if(r == 0){
				if(generationCurrent[c+1][r+1])
					neighbors++;
				if(generationCurrent[c][r+1])
					neighbors++;
				if(generationCurrent[columns-1][r+1])
					neighbors++;
				if(generationCurrent[columns-1][rows-1])
					neighbors++;
				if(generationCurrent[c][rows-1])
					neighbors++;
				if(generationCurrent[c+1][rows-1])
					neighbors++;
			} else if(r == rows - 1){
				if(generationCurrent[c+1][r-1])
					neighbors++;
				if(generationCurrent[c][r-1])
					neighbors++;
				if(generationCurrent[columns-1][r-1])
					neighbors++;
				if(generationCurrent[columns-1][0])
					neighbors++;
				if(generationCurrent[c][0])
					neighbors++;
				if(generationCurrent[c+1][0])
					neighbors++;
			} else {
				if(generationCurrent[c][r-1])
					neighbors++;
				if(generationCurrent[c+1][r-1])
					neighbors++;
				if(generationCurrent[c+1][r+1])
					neighbors++;
				if(generationCurrent[c][r+1])
					neighbors++;
				if(generationCurrent[columns-1][r+1])
					neighbors++;
				if(generationCurrent[columns-1][r-1])
					neighbors++;
			}
		} else if(c == columns - 1){
			if(generationCurrent[c-1][r])
				neighbors++;
			if(generationCurrent[0][r])
				neighbors++;
			
			if(r == 0){
				if(generationCurrent[c-1][r+1])
					neighbors++;
				if(generationCurrent[c][r+1])
					neighbors++;
				if(generationCurrent[0][r+1])
					neighbors++;
				if(generationCurrent[0][rows-1])
					neighbors++;
				if(generationCurrent[c][rows-1])
					neighbors++;
				if(generationCurrent[c-1][rows-1])
					neighbors++;
			} else if(r == rows - 1){
				if(generationCurrent[c-1][r-1])
					neighbors++;
				if(generationCurrent[c][r-1])
					neighbors++;
				if(generationCurrent[0][r-1])
					neighbors++;
				if(generationCurrent[0][0])
					neighbors++;
				if(generationCurrent[c][0])
					neighbors++;
				if(generationCurrent[c-1][0])
					neighbors++;
			} else {
				if(generationCurrent[c][r-1])
					neighbors++;
				if(generationCurrent[c-1][r-1])
					neighbors++;
				if(generationCurrent[c-1][r+1])
					neighbors++;
				if(generationCurrent[c][r+1])
					neighbors++;
				if(generationCurrent[0][r+1])
					neighbors++;
				if(generationCurrent[0][r-1])
					neighbors++;
			}
		} else if(r == 0){
			if(generationCurrent[c-1][r])
				neighbors++;
			if(generationCurrent[c-1][r+1])
				neighbors++;
			if(generationCurrent[c][r+1])
				neighbors++;
			if(generationCurrent[c+1][r+1])
				neighbors++;
			if(generationCurrent[c+1][r])
				neighbors++;
			if(generationCurrent[c+1][rows-1])
				neighbors++;
			if(generationCurrent[c][rows-1])
				neighbors++;
			if(generationCurrent[c-1][rows-1])
				neighbors++;
		} else if(r == rows - 1){
			if(generationCurrent[c-1][r])
				neighbors++;
			if(generationCurrent[c-1][r-1])
				neighbors++;
			if(generationCurrent[c][r-1])
				neighbors++;
			if(generationCurrent[c+1][r-1])
				neighbors++;
			if(generationCurrent[c+1][r])
				neighbors++;
			if(generationCurrent[c+1][0])
				neighbors++;
			if(generationCurrent[c][0])
				neighbors++;
			if(generationCurrent[c-1][0])
				neighbors++;
		} else {
			if(generationCurrent[c][r-1])
				neighbors++;
			if(generationCurrent[c][r+1])
				neighbors++;
			if(generationCurrent[c-1][r])
				neighbors++;
			if(generationCurrent[c+1][r])
				neighbors++;
			if(generationCurrent[c+1][r-1])
				neighbors++;
			if(generationCurrent[c+1][r+1])
				neighbors++;
			if(generationCurrent[c-1][r+1])
				neighbors++;
			if(generationCurrent[c-1][r-1])
				neighbors++;
		}
		
		return neighbors;
	}

	public void step(){
		boolean[][] generationCurrent = time.getActiveGeneration();
		int neighbors = 0;

		for(int c = 0; c < columns; c++){
			for(int r = 0; r < rows; r++){
				
				neighbors = getCellNeighbors(c, r);

				//System.out.printf("\ncol %d row %d : has %d neighbors", c, r, neighbors);
				if(generationCurrent[c][r]){
					if(neighbors < 2 || neighbors > 3){
						snapshotGrid[c][r] = false;
					} else {
						snapshotGrid[c][r] = true;
					}
				} else {
					if(neighbors == 3){
						snapshotGrid[c][r] = true;
					} else {
						snapshotGrid[c][r] = false;
					}
				}
			}
		}
		
		//for the generation counter
		if(!Arrays.deepEquals(generationCurrent, snapshotGrid)){
			generationCount++;
		}

		
//		for(int i = 0; i < columns; i++){
//			for(int j = 0; j < rows; j++){
//				currentGrid[i][j] = snapshotGrid[i][j];
//			}
//		}

		time.addNextGen(snapshotGrid);
		
		canvas.repaint();
	}
	
	public int getNumColumns() {
		return columns;
	}
	
	public int getNumRows() {
		return rows;
	}
}