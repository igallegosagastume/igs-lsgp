package commons.test.replGraph256test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardInputChecker implements Runnable {

	private ATester tester = null;
	
	public KeyboardInputChecker(ATester tester) {
		this.tester = tester;
	}
	@Override
	public void run() {
		synchronized (this.tester) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				boolean finish = false;
			
				while (!finish) {
					if (br.readLine().equals("F")) {
						finish=true;
						this.tester.finish=true;
					}
					Thread.sleep(4000);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
