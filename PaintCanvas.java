import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class PaintCanvas extends JPanel implements MouseListener, MouseMotionListener{
	private Life life;
	private int panelWidth;
	private int panelHeight;
	private Dimension canvasSize;
	private Graphics2D g;
	private boolean isDrawing = false;

	public PaintCanvas(Dimension dim){
		super();
		canvasSize = dim;
		setPreferredSize(canvasSize);
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);
		addMouseMotionListener(this);

		life = new Life((int)canvasSize.getWidth(), (int)canvasSize.getHeight());
		Life.setCanvas(this);
	}

	@Override
	public void mouseClicked(MouseEvent e){
		int col = e.getX() / Life.SQR_SIZE;
		int row = e.getY() / Life.SQR_SIZE;
		
		life.toggleCell(col, row);
		this.repaint();
		
	}

	@Override
	public void mousePressed(MouseEvent e){

	}

	@Override
	public void mouseReleased(MouseEvent e){

	}

	@Override
	public void mouseEntered(MouseEvent e){
		
	}

	@Override
	public void mouseExited(MouseEvent e){

	}

	@Override
	public void mouseMoved(MouseEvent e){
		
	}

	@Override
	public void mouseDragged(MouseEvent e){
		int col = e.getX() / Life.SQR_SIZE;
		int row = e.getY() / Life.SQR_SIZE;
		
		if(!life.checkCell(col, row)){
			life.toggleCell(col, row);
		}
		
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		panelWidth = this.getWidth();
		panelHeight = this.getHeight();
		g = (Graphics2D)graphics;
		
		//g.drawRect(0, 0, panelWidth, panelHeight);
		life.render(g);
		Window.generationCounter.setText(Life.getGeneration() + "");
	}	
}