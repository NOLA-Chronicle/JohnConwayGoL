import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class Window extends JFrame implements ActionListener, ChangeListener{
	public final static int WIDTH = 605;
	public final static int HEIGHT = 600 * 3 / 4;
	private final String[] presets = {"Clear", "Small Spaceship", "Pulsar", "Glider Gun", "10 Cell Row"};
	private PaintCanvas drawingPanel;
	private JPanel menuPanel;
	private JComboBox presetDesigns;
	private JSlider speedSlider;
	private JButton toggleRunBtn;
	private JButton stepBtn;
	private JButton clearBtn;

	public Window(){
		setTitle("John Conway's Game of Life");

		drawingPanel = new PaintCanvas(new Dimension(Window.WIDTH, (int)(Window.HEIGHT * .85)));

		menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(WIDTH, (int)(HEIGHT * .15)));
		menuPanel.setBorder(BorderFactory.createRaisedBevelBorder());

		presetDesigns = new JComboBox(presets);
		presetDesigns.addActionListener(this);

		speedSlider = new JSlider(1, 20, 5);
		speedSlider.addChangeListener(this);

		toggleRunBtn = new JButton("Start");
		toggleRunBtn.addActionListener(this);
		
		stepBtn = new JButton("Step");
		stepBtn.addActionListener(this);

		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(this);

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
		menuConst.gridx = 0;
		menuConst.gridy = 0;
		menuPanel.add(presetDesigns, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = 1;
		menuConst.gridy = 0;
		menuPanel.add(speedSlider, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = 2;
		menuConst.gridy = 0;
		menuPanel.add(toggleRunBtn, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = 3;
		menuConst.gridy = 0;
		menuPanel.add(stepBtn, menuConst);

		menuConst.insets = new Insets(5, 5, 5, 5);
		menuConst.gridx = 4;
		menuConst.gridy = 0;
		menuPanel.add(clearBtn, menuConst);

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
			} else if(srcButton == stepBtn){
				Life.step();
				repaint();
			} else if(srcButton == toggleRunBtn){
				if(srcButton.getText().equals("Start")){
					srcButton.setText("Stop");
				} else {
					srcButton.setText("Start");
				}
				Application.toggleRunning();
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