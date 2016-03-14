/**
 * Creation date: 08/07/2014
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

package jacomatt.main;

import jacomatt.model.EfficientIncidenceCube;
import jacomatt.model.IncidenceCube;
import jacomatt.model.IncidenceCubeWithDebugging;

import java.security.SecureRandom;

import commons.FileUtils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class Main {

	/** The entry main() method */
	public static void main(String[] args) {		

		if (args.length<3) {
			/*System.out.println("Usage: <action> <option> <LS order> [write <path>]");
			System.out.println("Where <action> ::= debug | draw | compute");
			System.out.println("  and <option> ::= ic | efficient | shuffle");*/
			System.out.println("Available options are:");
			System.out.println("debug   ic        <LS order>                 |");
			System.out.println("debug   shuffle   <LS order>                 |");
			System.out.println("draw    ic        <LS order>                 |");
			System.out.println("draw    shuffle   <LS order>                 |");
			System.out.println("compute ic        <LS order> [write <path>]  |");
			System.out.println("compute efficient <LS order> [write <path>]");
			System.out.println("----------------------------------------------");
			System.out.println("Example 1: debug ic 25");
			System.out.println("Example 2: compute efficient 256");
			return;
		}
		
		if (args[0].equalsIgnoreCase("debug")) {
			boolean shuffle = args[1].equalsIgnoreCase("shuffle"); 
			debugIncidenceCube(new Integer(args[2]), shuffle);
			return;	
		}
		if (args[0].equalsIgnoreCase("debug") && args[1].equalsIgnoreCase("efficient")) {
			System.out.println("Method not implemented yet.");
			return;
		}
		if (args[0].equalsIgnoreCase("draw")) {
			boolean shuffle = args[1].equalsIgnoreCase("shuffle");
			drawIncidenceCube(new Integer(args[2]), shuffle);
			return;
		}
		String path = null;
		if (args.length>3) {
			if (args[3]!=null && args[3].equalsIgnoreCase("write")) {
				if (args.length==5)
					path = args[4];
				else {
					System.out.println("Bad usage. The write option needs a path.");
					return;
				}
			} else {
				System.out.println("Bad usage. Parameter 4 must be 'write'.");
				return;
			}
		}
		if (args[0].equalsIgnoreCase("compute") && args[1].equalsIgnoreCase("ic")) {
			computeICTime(new Integer(args[2]), path);
			return;
		}
		if (args[0].equalsIgnoreCase("compute") && args[1].equalsIgnoreCase("efficient")) {
			computeICTimeForEfficientLS(new Integer(args[2]), path);
			return;
		}
		if (args[0].equalsIgnoreCase("compute") && args[1].equalsIgnoreCase("shuffle")) {
			System.out.println("The option 'compute' already shuffles the result. Allowed options are 'compute ic <n>' or 'compute efficient <n>'.");
			return;
		}

		System.out.println("Option not supported.");
//		testIncidenceCubeToString();
//		pruRandom();
	}
	
	
	
	public static void pruRandom() {
//		System.setProperty(MH_SecureRandom.USER, "bluemontag@gmail.com");
//		Security.addProvider(new RjgodoyProvider());
//		SecureRandom srandom = null;
//		try {
//			srandom = SecureRandom.getInstance("MH_TRNG");
//			System.out.print(srandom.nextInt());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		SecureRandom random = new SecureRandom();
		System.out.println(random.nextInt(100));
	}
	
	public static void debugIncidenceCube(int n, boolean shuffle) {
		IncidenceCubeWithDebugging ic = new IncidenceCubeWithDebugging(n);
		if (shuffle)
			ic.shuffle();
		ic.drawIncidenceCube();
	}
	
	public static void drawIncidenceCube(int n, boolean shuffle) {
		IncidenceCube ic = new IncidenceCube(n);
		if (shuffle)
			ic.shuffle();
		ic.drawIncidenceCube();
	}
	
	public static void computeICTime(int n, String path) {
		long startTime = System.nanoTime();
		IncidenceCube ic = new IncidenceCube(n);
		
		int i = ic.shuffle();
		
		
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ic);
		
		FileUtils.writeLS(ic, path);
		
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: J&M clear implementation.");
		System.out.println("Iterations: "+i);
	}
	
	public static void computeICTimeForEfficientLS(int n, String path) {
		long startTime = System.nanoTime();
		EfficientIncidenceCube ic = new EfficientIncidenceCube(n);
		
		int i = ic.shuffle();

		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		
		System.out.println(ic);
		
		FileUtils.writeLS(ic, path);
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: J&M efficient method.");
		System.out.println("Iterations: "+i);
	}
	
	public static void testIncidenceCubeToString() {
//		EfficientIncidenceCube ic = new EfficientIncidenceCube(5);
		IncidenceCube ic = new IncidenceCube(5);
		for (int i=0; i <100; i++) {
			if (ic.proper())
				ic.moveFromProper();
			else
				ic.moveFromImproper();
			System.out.println(ic);
		}
	}
}
