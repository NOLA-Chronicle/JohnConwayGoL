
public class GameLoop extends Thread {
	private Life life;
	private int ticksPerSec = 25;
	private boolean running = false;

	public GameLoop(Life life) {
		this.life = life;

	}

	@Override
	public void run() {
		// Dumb me
		while (true) {
			if (running) {
				try {
					// System.out.println(ticksPerSec);
					life.step();
					Thread.sleep(1000 / ticksPerSec);
				} catch (Exception e) {
//					e.printStackTrace();
				}
			} else {
				System.out.print("");
			}
		}
	}

	public void setTickSpeed(int sliderValue) {
		ticksPerSec = sliderValue;
	}

	public void toggleRunning() {
		running = !running;
	}
}
