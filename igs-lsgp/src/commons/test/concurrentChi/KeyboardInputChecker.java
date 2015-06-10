package commons.test.concurrentChi;


/**
 * Creation date: 08/06/2015
 * 
 * Master thesis on Latin Squares generation
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class KeyboardInputChecker implements Runnable {

	private GeneratorLSsChiTest tester = null;
	
	public KeyboardInputChecker(GeneratorLSsChiTest tester) {
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
