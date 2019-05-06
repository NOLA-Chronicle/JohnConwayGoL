import java.awt.*;
import javax.swing.*;

class Application implements Runnable{
	private static int ticksPerSec = 1;
	private static boolean running = false;

	public static void main(String[] args) {
		Thread gameLoop = new Thread(new Application());
		Window window = new Window();

		window.pack();
		window.setResizable(false);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		gameLoop.start();
		
	}

	public static void setTickSpeed(int sliderValue){
		ticksPerSec = sliderValue;
	}

	public static void toggleRunning(){
		running = !running;
	}

	@Override
	public void run(){
		try{
			while(true){
				if(running){
					//System.out.println(ticksPerSec);
					Life.step();
					Thread.sleep(1000 / ticksPerSec);
				} else {
					System.out.print("");
				}
			}
		} catch(Exception e){}
	}
}