package commons.test.concurrentChi;

/**
 * Creation date: 18/05/2015
 * 
 * 
 */

/**
 * © Copyright 2012-2015 Ignacio Gallego Sagastume
 * 
 * This file is part of IGS-ls-generation package.
 * IGS-ls-generation package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * IGS-ls-generation package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with IGS-ls-generation package.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */


/**
 * This class is prepared to be called from a runnable jar.
 *  Implements a concurret test that finishes when the ENTER key is pressed. It generates LSs of order n<=7 and classifies them into equivalence classes 
 *  (equivalent classes are count under the same identifier).
 *   
 * @author igallego
 *
 */
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
