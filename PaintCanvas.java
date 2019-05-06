import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class PaintCanvas extends JPanel implements MouseListener{
	private Life life;
	private int panelWidth;
	private int panelHeight;
	private Dimension canvasSize;
	private Graphics2D g;

	public PaintCanvas(Dimension dim){
		super();
		canvasSize = dim;
		setPreferredSize(canvasSize);
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);

		life = new Life((int)canvasSize.getWidth(), (int)canvasSize.getHeight());
		Life.setCanvas(this);
	}

	@Override
	public void mouseClicked(MouseEvent e){
		int col = e.getX() / Life.SQR_SIZE;
		int row = e.getY() / Life.SQR_SIZE;
		// System.out.printf("\ncol %d row %d", col, row);

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
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);

		panelWidth = this.getWidth();
		panelHeight = this.getHeight();
		g = (Graphics2D)graphics;
		
		//g.drawRect(0, 0, panelWidth, panelHeight);
		life.render(g);
	}	
}