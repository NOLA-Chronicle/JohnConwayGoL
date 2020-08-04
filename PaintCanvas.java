import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class PaintCanvas extends JPanel implements MouseListener, MouseMotionListener{
	private static final long serialVersionUID = 2035075294128405792L;
	private Life life;
	@SuppressWarnings("unused")
	private int panelWidth;
	@SuppressWarnings("unused")
	private int panelHeight;
	private Graphics2D g;
	public GameLoop game;
	
	public PaintCanvas(Dimension dim, Life life){
		super();
		setPreferredSize(dim);
		setBackground(new Color(255, 255, 255));
		addMouseListener(this);
		addMouseMotionListener(this);

		this.life = life;
		life.setCanvas(this);
		
		game = new GameLoop(life);
		game.start();
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		int col = e.getX() / Life.SQR_SIZE;
		int row = e.getY() / Life.SQR_SIZE;
		
		// System.out.println(e.getX() + " x " + e.getY());
		
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
		
		if(SwingUtilities.isLeftMouseButton(e)) {
			if(!life.checkCell(col, row)){
				life.toggleCell(col, row);
			}
		} else if(SwingUtilities.isRightMouseButton(e)) {
			if(life.checkCell(col, row)){
				life.toggleCell(col, row);
			}
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
		Window.generationCounter.setText("Generation: " + life.getGenerationCount());
	}
}