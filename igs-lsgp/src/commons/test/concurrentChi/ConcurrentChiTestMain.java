package commons.test.concurrentChi;



public class ConcurrentChiTestMain {

	public static void main(String[] args) {

		
		if (args.length!=2) {
			System.out.println("Usage: java -jar cchiTest.jar [ jacomatt | swapping | graph ] <LS order>");
			System.out.println("Example 1: swapping 4");
			System.out.println("Example 2: jacomatt 5");
			return;
		}
		
		String option = args[0]; 
		
		int order = new Integer(args[1]);

		
		GeneratorLSsChiTest tester = new GeneratorLSsChiTest(option, order);

		Thread testerThread = new Thread(tester);
		
		Thread keyboardThread = new Thread(new KeyboardInputChecker(tester));
		
		
		keyboardThread.start();
		testerThread.start();
	}

}
