import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class Window extends JFrame implements ActionListener, ChangeListener{
	public final static int WIDTH = 955;
	public final static int HEIGHT = 730;
	private final String[] presets = {"Clear", "Small Spaceship", "Pulsar", "Glider Gun", "10 Cell Row", "Random"};
	private PaintCanvas drawingPanel;
	private JPanel menuPanel;
	private JComboBox presetDesigns;
	private JLabel speedSliderLabel;
	private JSlider speedSlider;
	private JButton toggleRunBtn;
	private JButton stepBtn;
	private JButton clearBtn;
	public static JLabel generationCounter;

	public Window(){
		
		int xPos = 0;
		int yPos = 1;
		setTitle("John Conway's Game of Life");

		drawingPanel = new PaintCanvas(new Dimension(Window.WIDTH, (int)(Window.HEIGHT * .85)));

		menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(WIDTH, (int)(HEIGHT * .15)));
		menuPanel.setBorder(BorderFactory.createRaisedBevelBorder());

		presetDesigns = new JComboBox(presets);
		presetDesigns.addActionListener(this);

		speedSlider = new JSlider(1, 100);
		speedSlider.addChangeListener(this);

		toggleRunBtn = new JButton("Start");
		toggleRunBtn.addActionListener(this);
		
		stepBtn = new JButton("Step");
		stepBtn.addActionListener(this);

		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(this);

		speedSliderLabel = new JLabel("Slow                                       Fast");
		generationCounter = new JLabel("0");
		
		setLayout(new GridBagLayout());
		GridBagConstraints layoutConst = new GridBagConstraints();

		layoutConst.insets = new Insets(5, 5, 5, 5);
		layoutConst.gridx = 0;
		layoutConst.gridy = 0;
		add(drawingPanel, layoutConst);

		//Layout for the menu.
		menuPanel.setLayout(new GridBagLayout());
		GridBagConstraints menuConst = new GridBagConstraints();
		
		layoutConst.insets = new Insets(5, 5, 5, 5);
		layoutConst.gridx = 0;
		layoutConst.gridy = 1;
		add(menuPanel, layoutConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos;
		menuConst.gridy = yPos;
		menuPanel.add(presetDesigns, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos+1;
		menuConst.gridy = yPos-1;
		menuPanel.add(speedSliderLabel, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos+1;
		menuConst.gridy = yPos;
		menuPanel.add(speedSlider, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos+2;
		menuConst.gridy = yPos;
		menuPanel.add(toggleRunBtn, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos+3;
		menuConst.gridy = yPos;
		menuPanel.add(stepBtn, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos+4;
		menuConst.gridy = yPos;
		menuPanel.add(clearBtn, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = xPos+5;
		menuConst.gridy = yPos;
		menuPanel.add(generationCounter, menuConst);
		
		validate();
	} 

	@Override
	public void actionPerformed(ActionEvent e){
		String title;
		JButton srcButton;
		JComboBox srcCombo;

		if(e.getSource() instanceof JButton){
			srcButton = (JButton)e.getSource();

			if(srcButton == clearBtn){
				presetDesigns.setSelectedIndex(0);
				if(toggleRunBtn.getText().equals("Stop")){
					toggleRunBtn.setText("Start");
					Application.toggleRunning();
				}
			} else if(srcButton == toggleRunBtn){
				if(srcButton.getText().equals("Start")){
					srcButton.setText("Stop");
				} else {
					srcButton.setText("Start");
				}
				Application.toggleRunning();
			} else if(srcButton == stepBtn){
				Life.step();
				// repaint();
			}
		} else {
			title = "clear";
			srcCombo = (JComboBox)e.getSource();
			try{
				switch(srcCombo.getSelectedIndex()){

					case 0:
					break;
					case 1:
						title = "smallSpaceship";
					break;
					case 2:
						title = "pulsar";
					break;
					case 3:
						title = "gliderGun";
					break;
					case 4:
						title = "tenCellRow";
					break;
					case 5:
						title = "Random";
					break;
					default:
					break;
				}
				Life.presetDesign(title);
			} catch(Exception e2){
			}
			repaint();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e){
		if(e.getSource() instanceof JSlider){
			if(e.getSource() == speedSlider){
				Application.setTickSpeed(((JSlider)e.getSource()).getValue());
			}
		}
	}
}