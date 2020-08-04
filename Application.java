import javax.swing.*;

class Application {

	public static void main(String[] args) {
		Window window = new Window();

		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

	}
}