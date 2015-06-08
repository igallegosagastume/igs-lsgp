package commons.test.concurrentTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardInputChecker implements Runnable {

	private CountLSsTester tester = null;
	
	public KeyboardInputChecker(CountLSsTester tester) {
		this.tester = tester;
	}
	@Override
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			boolean finish = false;
		
			while (!finish) {
				int b = br.read();
				if (b == 13) {
					finish=true;
					synchronized (this.tester) {
						this.tester.finish=true;
					}
				}
				if (!finish)
					Thread.sleep(4000);
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}
