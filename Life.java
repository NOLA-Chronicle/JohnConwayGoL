import java.util.*;
import java.awt.*;
import java.io.*;

class Life{
	private static String file = "gridPresets.txt";
	public static final int SQR_SIZE = 15;
	public static boolean[][] currentGrid;
	public static boolean[][] snapshotGrid;
	private static int columns;
	private static int rows;
	private static PaintCanvas canvas;

	public Life(int width, int height){
		columns = width / SQR_SIZE;
		rows = height / SQR_SIZE;

		System.out.printf("%d x %d\n", columns, rows);

		initGrid();
	}

	public static void setCanvas(PaintCanvas myCanvas){
		canvas = myCanvas;
	}

	private void initGrid(){
		currentGrid = new boolean[columns][rows];
		snapshotGrid = new boolean[columns][rows];

		clearGrid();
	}

	public void render(Graphics2D g){
		//draw background grid
		int width = currentGrid.length * SQR_SIZE;
		int height = currentGrid[0].length * SQR_SIZE;

		g.drawRect(0, 0, width, height);

		for(int x = 0; x < currentGrid.length; x++){
			g.drawLine(x * SQR_SIZE, 0, x * SQR_SIZE, height);
		}

		for(int y = 0; y < currentGrid[0].length; y++){
			g.drawLine(0, y * SQR_SIZE, width, y * SQR_SIZE);
		}

		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
				if(currentGrid[i][j]){
					g.setColor(Color.BLACK);
					g.fillRect(i * SQR_SIZE, j * SQR_SIZE, SQR_SIZE, SQR_SIZE);
				}
			}
		}
	}

	public void toggleCell(int c, int r){
		currentGrid[c][r] = !currentGrid[c][r];
	}

	public static void clearGrid(){
		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
				currentGrid[i][j] = false;
				snapshotGrid[i][j] = false;
			}
		}
	}

	public static void presetDesign(String title) throws IOException{
		FileInputStream inStream = null;
		Scanner in = null;

		clearGrid();

		try{
			inStream = new FileInputStream(file);
			in = new Scanner(inStream);

			while(in.hasNext()){
				if(in.nextLine().equals(title)){
					// System.out.println(in.nextByte() + " printed");
					for(int r = 0; r < currentGrid[0].length; r++){
						String line = in.nextLine();
						for(int c = 0; c < line.length(); c++){
							if(line.charAt(c) == '1'){
								currentGrid[c][r] = true;
							}
						}
					}
				}
			}
		} catch(IOException e){
		}finally{
			in.close();
			inStream.close();
		}
	}

	public static void step(){
		int neighbors = 0;

		for(int c = 0; c < columns; c++){
			for(int r = 0; r < rows; r++){
				neighbors = 0;
				if(c == 0){
					if(currentGrid[c+1][r])
						neighbors++;
					
					if(r == 0){
						if(currentGrid[c+1][r+1])
							neighbors++;
						if(currentGrid[c][r+1])
							neighbors++;
					} else if(r == rows - 1){
						if(currentGrid[c+1][r-1])
							neighbors++;
						if(currentGrid[c][r-1])
							neighbors++;
					} else {
						if(currentGrid[c][r-1])
							neighbors++;
						if(currentGrid[c+1][r-1])
							neighbors++;
						if(currentGrid[c+1][r+1])
							neighbors++;
						if(currentGrid[c][r+1])
							neighbors++;
					}
				} else if(c == columns - 1){
					if(currentGrid[c-1][r])
						neighbors++;

					if(r == 0){
						if(currentGrid[c-1][r+1])
							neighbors++;
						if(currentGrid[c][r+1])
							neighbors++;
					} else if(r == rows - 1){
						if(currentGrid[c-1][r-1])
							neighbors++;
						if(currentGrid[c][r-1])
							neighbors++;
					} else {
						if(currentGrid[c][r-1])
							neighbors++;
						if(currentGrid[c-1][r-1])
							neighbors++;
						if(currentGrid[c-1][r+1])
							neighbors++;
						if(currentGrid[c][r+1])
							neighbors++;
					}
				} else if(r == 0){
					if(currentGrid[c-1][r])
						neighbors++;
					if(currentGrid[c-1][r+1])
						neighbors++;
					if(currentGrid[c][r+1])
						neighbors++;
					if(currentGrid[c+1][r+1])
						neighbors++;
					if(currentGrid[c+1][r])
						neighbors++;
				} else if(r == rows - 1){
					if(currentGrid[c-1][r])
						neighbors++;
					if(currentGrid[c-1][r-1])
						neighbors++;
					if(currentGrid[c][r-1])
						neighbors++;
					if(currentGrid[c+1][r-1])
						neighbors++;
					if(currentGrid[c+1][r])
						neighbors++;
				} else {
					if(currentGrid[c][r-1])
						neighbors++;
					if(currentGrid[c][r+1])
						neighbors++;
					if(currentGrid[c-1][r])
						neighbors++;
					if(currentGrid[c+1][r])
						neighbors++;
					if(currentGrid[c+1][r-1])
						neighbors++;
					if(currentGrid[c+1][r+1])
						neighbors++;
					if(currentGrid[c-1][r+1])
						neighbors++;
					if(currentGrid[c-1][r-1])
						neighbors++;
				}

				//System.out.printf("\ncol %d row %d : has %d neighbors", c, r, neighbors);
				if(currentGrid[c][r]){
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

		for(int i = 0; i < columns; i++){
			for(int j = 0; j < rows; j++){
				currentGrid[i][j] = snapshotGrid[i][j];
			}
		}

		canvas.repaint();
	}
}