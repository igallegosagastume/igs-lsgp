/**
 * Creation date: 08/11/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2014 Ignacio Gallego Sagastume
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

package commons.test.concurrentTest;


public class ConcurrentTestMain {

	public static void main(String[] args) {
		
		if (args.length!=1) {
			System.out.println("Usage: java -jar concTest.jar <order of LSs>");
			System.out.println("Then, press ENTER to conclude the test.");
			return;
		}
		
		CountLSsTester tester = new CountLSsTester(new Integer(args[0]));

		Thread testerThread = new Thread(tester);
		
		Thread keyboardThread = new Thread(new KeyboardInputChecker(tester));
		
		
		keyboardThread.start();
		testerThread.start();
	}

}
