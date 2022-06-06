import java.util.*;
import java.awt.*;
import java.io.*;

class Life{
	private Timeline time;
	private static String file = "gridPresets.txt";
	public static final int SQR_SIZE = 20;
	private boolean[][] snapshotGrid;
	private static int columns;
	private static int rows;
	private static PaintCanvas canvas;
	private static int generationCount = 0;
	private boolean wrapAround = true;

	public Life(int width, int height){
		columns = width / SQR_SIZE;
		rows = height / SQR_SIZE;

		System.out.printf("%d x %d\n", columns, rows);

		time = new Timeline(100, columns, rows);
		initGrid();
		
	}

	public boolean isWrappingAround(){
		return wrapAround;
	}

	public void setWrapAround(boolean toggle){
		wrapAround = toggle;
	}

	public void redoState(){
		generationCount = 0;
		
		time.clearHistory();
		// time.addNextGen(time.getInitialGeneration());
		
	}

	public void setCanvas(PaintCanvas myCanvas){
		canvas = myCanvas;
	}
	
	public int getGenerationCount(){
		return generationCount;
	}
	
	private void initGrid(){
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
		
		generationInitial[c][r] = !generationInitial[c][r];

		setActiveGenAsInitialGen();
	}

	public void clearGrid(){
		generationCount = 0;
		time.clearInitialState();
		time.clearHistory();
		
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
				snapshotGrid[i][j] = false;
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

	private int getCellNeighbors(int c, int r, boolean wrap){
		boolean[][] generationCurrent = time.getActiveGeneration();
		
		c = (c + getNumColumns());
		r = (r + getNumRows());

		int cPlus1 = (c + 1) % getNumColumns();
		int cMinus1 = (c - 1) % getNumColumns();

		int rPlus1 = (r + 1) % getNumRows();
		int rMinus1 = (r - 1) % getNumRows();

		c = c % getNumColumns();
		r = r % getNumRows();

		int neighbors = 0;

		//walls wrap-around
		if(wrap){
			if(generationCurrent[c][rMinus1]) neighbors++;			//check Top
			if(generationCurrent[cPlus1][rMinus1]) neighbors++;		//check Top-R
			if(generationCurrent[cPlus1][r]) neighbors++;			//check Right
			if(generationCurrent[cPlus1][rPlus1]) neighbors++;		//check Bottom-R
			if(generationCurrent[c][rPlus1]) neighbors++;			//check Bottom
			if(generationCurrent[cMinus1][rPlus1]) neighbors++;		//check Bottom-L
			if(generationCurrent[cMinus1][r]) neighbors++;			//check Left
			if(generationCurrent[cMinus1][rMinus1]) neighbors++;	//check Top-L
		//walls don't wrap-around
		} else {
			//if cell is on left wall
			if(c % getNumColumns() == 0){
				if(generationCurrent[cPlus1][r]) neighbors++;	//check right
				
				//if cell is on top wall
				if(r % getNumRows() == 0){
					if(generationCurrent[c][rPlus1]) neighbors++;		//check bottom
					if(generationCurrent[cPlus1][rPlus1]) neighbors++;	//check bottom-R
				//if cell is on bottom wall
				} else if(r % getNumRows() == getNumRows() - 1){
					if(generationCurrent[c][rMinus1]) neighbors++;		//check top
					if(generationCurrent[cPlus1][rMinus1]) neighbors++;	//check top-R
				//cell is somewhere between
				} else {
					if(generationCurrent[c][rMinus1]) neighbors++;		//check top
					if(generationCurrent[c][rPlus1]) neighbors++;		//check bottom
					if(generationCurrent[cPlus1][rMinus1]) neighbors++;	//check top-R
					if(generationCurrent[cPlus1][rPlus1]) neighbors++;	//check bottom-R
				}
			//if cell is on right wall
			} else if(c % getNumColumns() == getNumColumns() - 1){
				if(generationCurrent[cMinus1][r]) neighbors++;	//check left

				//if cell is on top wall
				if(r % getNumRows() == 0){
					if(generationCurrent[c][rPlus1]) neighbors++;		//check bottom
					if(generationCurrent[cMinus1][rPlus1]) neighbors++;	//check bottom-L
				//if cell is on bottom wall
				} else if(r % getNumRows() == getNumRows() - 1){
					if(generationCurrent[c][rMinus1]) neighbors++;		//check top
					if(generationCurrent[cMinus1][rMinus1]) neighbors++;//check top-L
				//cell is somewhere between
				} else {
					if(generationCurrent[c][rMinus1]) neighbors++;		//check top
					if(generationCurrent[c][rPlus1]) neighbors++;		//check bottom
					if(generationCurrent[cMinus1][rMinus1]) neighbors++;//check top-L
					if(generationCurrent[cMinus1][rPlus1]) neighbors++;	//check bottom-L
				}
			//cell is somewhere between
			} else {
				if(generationCurrent[cMinus1][r]) neighbors++;	//check left
				if(generationCurrent[cPlus1][r]) neighbors++;	//check right

				//if cell is on top wall
				if(r % getNumRows() == 0){
					if(generationCurrent[c][rPlus1]) neighbors++;		//check bottom
					if(generationCurrent[cMinus1][rPlus1]) neighbors++;	//check bottom-L
					if(generationCurrent[cPlus1][rPlus1]) neighbors++;	//check bottom-R

				//if cell is on bottom wall
				} else if(r % getNumRows() == getNumRows() - 1){
					if(generationCurrent[c][rMinus1]) neighbors++;		//check top
					if(generationCurrent[cMinus1][rMinus1]) neighbors++;//check top-L
					if(generationCurrent[cPlus1][rMinus1]) neighbors++;	//check top-R

				//cell is somewhere between
				} else {
					if(generationCurrent[c][rMinus1]) neighbors++;		//check top
					if(generationCurrent[cMinus1][rMinus1]) neighbors++;//check top-L
					if(generationCurrent[cPlus1][rMinus1]) neighbors++;	//check top-R
					if(generationCurrent[c][rPlus1]) neighbors++;		//check bottom
					if(generationCurrent[cMinus1][rPlus1]) neighbors++;	//check bottom-L
					if(generationCurrent[cPlus1][rPlus1]) neighbors++;	//check bottom-R

				}
			}
		}

		return neighbors;
	}

	public void setActiveGenAsInitialGen(){
		boolean[][] generationInitial = time.getInitialGeneration();
		boolean[][] generationCurrent = time.getActiveGeneration();

		for(int col = 0; col < generationCurrent.length; col++){
			for(int row = 0; row < generationCurrent[col].length; row++){
				generationInitial[col][row] = generationCurrent[col][row];
			}
		}
		generationCount = 0;

		time.clearHistory();
	}

	public void step(){
		boolean[][] generationCurrent = time.getActiveGeneration();
		int neighbors = 0;
		boolean wrap = isWrappingAround();

		for(int c = 0; c < columns; c++){
			for(int r = 0; r < rows; r++){
				
				neighbors = getCellNeighbors(c, r, wrap);

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