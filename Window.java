import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

class Window extends JFrame implements ActionListener, ChangeListener {
	public final static int WIDTH = 955;
	public final static int HEIGHT = 730;
	private Life life;
	private final String[] presets = { "Clear", "Small Spaceship", "Pulsar", "Glider Gun", "10 Cell Row", "Random" };
	private PaintCanvas drawingPanel;

	private JPanel menuPanel;
	private JPanel presetPnl;
	private JPanel speedPnl;
	private JPanel controlPnl;
	private JPanel analyzePnl;
	
	private JComboBox presetDesigns;
	private JLabel speedSliderLabel;
	private JSlider speedSlider;
	private JButton prevBtn;
	private JButton toggleRunBtn;
	private JButton stepBtn;
	private JButton clearBtn;
	private JButton binaryBtn;
	private JButton redoStartingStateBtn;

	public static JLabel generationCounter;

	public Window() {

		setTitle("John Conway's Game of Life");

		Dimension canvasSize = new Dimension(Window.WIDTH, (int) (Window.HEIGHT * .85));
		life = new Life((int) canvasSize.getWidth(), (int) canvasSize.getHeight());
		drawingPanel = new PaintCanvas(canvasSize, life);

		// Panels
		menuPanel = new JPanel();
		menuPanel.setPreferredSize(new Dimension(WIDTH, (int) (HEIGHT * .15)));

		presetPnl = new JPanel();
		presetPnl.setBorder(BorderFactory.createRaisedBevelBorder());

		speedPnl = new JPanel();
		speedPnl.setBorder(BorderFactory.createRaisedBevelBorder());

		controlPnl = new JPanel();
		controlPnl.setBorder(BorderFactory.createRaisedBevelBorder());

		analyzePnl = new JPanel();
		analyzePnl.setBorder(BorderFactory.createRaisedBevelBorder());

		
		// Components
		presetDesigns = new JComboBox(presets);
		presetDesigns.addActionListener(this);

		speedSlider = new JSlider(1, 150, 25);
		speedSlider.addChangeListener(this);

		toggleRunBtn = new JButton("Start");
		toggleRunBtn.addActionListener(this);

		stepBtn = new JButton("Next Gen");
		stepBtn.addActionListener(this);

		prevBtn = new JButton("Prev Gen");
		prevBtn.addActionListener(this);

		clearBtn = new JButton("Clear");
		clearBtn.addActionListener(this);

		binaryBtn = new JButton("Binary State");
		binaryBtn.addActionListener(this);

		redoStartingStateBtn = new JButton("Redo");
		redoStartingStateBtn.addActionListener(this);

		speedSliderLabel = new JLabel("Slow                                                 Fast");
		generationCounter = new JLabel("Generation: 0");

		//Window Layout
		GroupLayout layout = new GroupLayout(this.getContentPane());
		setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(
			layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(drawingPanel)
					.addComponent(menuPanel))
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(drawingPanel)
				.addComponent(menuPanel)
		);
		
		//Set layout for sub-panels
		GroupLayout menu = new GroupLayout(menuPanel);
		menuPanel.setLayout(menu);
		menu.setAutoCreateGaps(true);
		menu.setAutoCreateContainerGaps(true);
		menu.setHorizontalGroup(
			menu.createSequentialGroup()
				.addComponent(presetPnl)
				.addComponent(speedPnl)
				.addComponent(controlPnl)
				.addComponent(analyzePnl)
		);
		menu.setVerticalGroup(
			menu.createSequentialGroup()
				.addGroup(menu.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(presetPnl)
					.addComponent(speedPnl)
					.addComponent(controlPnl)
					.addComponent(analyzePnl))
		);
		
		//Set layout for preset panel
		GroupLayout presetGroup = new GroupLayout(presetPnl);
		presetPnl.setLayout(presetGroup);
		presetGroup.setAutoCreateGaps(true);
		presetGroup.setAutoCreateContainerGaps(true);
		presetGroup.setHorizontalGroup(
			presetGroup.createSequentialGroup()
				.addComponent(presetDesigns, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		);
		presetGroup.setVerticalGroup(
			presetGroup.createSequentialGroup()
				.addComponent(presetDesigns, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
		);
		
		//Set layout for speed panel
		GroupLayout speedGroup = new GroupLayout(speedPnl);
		speedPnl.setLayout(speedGroup);
		speedGroup.setAutoCreateGaps(true);
		speedGroup.setAutoCreateContainerGaps(true);
		speedGroup.setHorizontalGroup(
			speedGroup.createSequentialGroup()
				.addGroup(speedGroup.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(speedSliderLabel)
					.addComponent(speedSlider)
					.addComponent(generationCounter))
		);
		speedGroup.setVerticalGroup(
			speedGroup.createSequentialGroup()
				.addComponent(speedSliderLabel)
				.addComponent(speedSlider)
				.addComponent(generationCounter)
		);
		
		
		//Set layout for control panel
		GroupLayout controlGroup = new GroupLayout(controlPnl);
		controlPnl.setLayout(controlGroup);
		controlGroup.setAutoCreateGaps(true);
		controlGroup.setAutoCreateContainerGaps(true);
		controlGroup.setHorizontalGroup(
			controlGroup.createSequentialGroup()
				.addComponent(prevBtn)
				.addComponent(toggleRunBtn)
				.addComponent(stepBtn)
		);
		controlGroup.setVerticalGroup(
			controlGroup.createSequentialGroup()
				.addGroup(controlGroup.createParallelGroup(GroupLayout.Alignment.BASELINE)
					.addComponent(prevBtn)
					.addComponent(toggleRunBtn)
					.addComponent(stepBtn))
		);

		//Set layout for analyze panel
		GroupLayout analyzeGroup = new GroupLayout(analyzePnl);
		analyzePnl.setLayout(analyzeGroup);
		analyzeGroup.setAutoCreateGaps(true);
		analyzeGroup.setAutoCreateContainerGaps(true);
		analyzeGroup.setHorizontalGroup(
			analyzeGroup.createSequentialGroup()
			.addGroup(analyzeGroup.createParallelGroup(GroupLayout.Alignment.CENTER)
					.addComponent(clearBtn)
					.addComponent(redoStartingStateBtn)
					.addComponent(binaryBtn))
		);
		analyzeGroup.setVerticalGroup(
			analyzeGroup.createSequentialGroup()
				.addComponent(clearBtn)
				.addComponent(redoStartingStateBtn)
				.addComponent(binaryBtn)
		);		

		validate();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String title;
		JButton srcButton;
		JComboBox srcCombo;

		if (e.getSource() instanceof JButton) {
			srcButton = (JButton) e.getSource();

			if (srcButton == clearBtn) {
				presetDesigns.setSelectedIndex(0);
				if (toggleRunBtn.getText().equals("Stop")) {
					toggleRunBtn.setText("Start");
					drawingPanel.game.toggleRunning();
				}
				life.clearGrid();
			} else if (srcButton == toggleRunBtn) {
				if (srcButton.getText().equals("Start")) {
					srcButton.setText("Stop");
				} else {
					srcButton.setText("Start");
				}
				drawingPanel.game.toggleRunning();

			} else if (srcButton == stepBtn) {
				life.step();

			} else if (srcButton == binaryBtn) {
				JFrame binaryWindow = new JFrame("Binary State");
				JPanel panel = new JPanel();
				TextArea binaryTA = new TextArea("", life.getNumColumns(), life.getNumRows());
				String binaryStr = "";

				for (int r = 0; r < life.getNumRows(); r++) {
					for (int c = 0; c < life.getNumColumns(); c++) {
						if (life.checkCell(c, r)) {
							binaryStr += "1";
						} else {
							binaryStr += "0";
						}
					}
					binaryStr += "\n";
				}

				binaryTA.setText(binaryStr);
				panel.add(binaryTA);
				binaryWindow.add(panel);

				binaryWindow.pack();
				binaryWindow.setResizable(false);
				binaryWindow.setLocationRelativeTo(null);
				binaryWindow.setVisible(true);

			} else if (srcButton == redoStartingStateBtn) {
				if (toggleRunBtn.getText().equals("Stop")) {
					toggleRunBtn.setText("Start");
					drawingPanel.game.toggleRunning();
				}
				life.redoState();
				repaint();

			} else if (srcButton == prevBtn) {
				life.previousState();
				repaint();
			}
		} else {
			title = "clear";
			srcCombo = (JComboBox) e.getSource();
			try {
				switch (srcCombo.getSelectedIndex()) {

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
				life.presetDesign(title);
			} catch (Exception e2) {
			}
			repaint();
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() instanceof JSlider) {
			if (e.getSource() == speedSlider) {
				drawingPanel.game.setTickSpeed(((JSlider) e.getSource()).getValue());
			}
		}
	}
}