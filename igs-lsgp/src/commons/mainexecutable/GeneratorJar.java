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
package commons.mainexecutable;

import jacomatt.model.generators.JacobsonMatthewsLSGenerator;
import mckaywormald.model.generators.McKayLRGenerationMethod;
import basicImpl.model.generators.KoscielnyProduct;
import basicImpl.model.generators.SimpleGenWithBacktracking;
import basicImpl.model.generators.SimpleGenWithRandomSwapping;
import basicImpl.model.generators.SimpleGenWithReplGraph;
import basicImpl.model.generators.SimpleGenWithRestartRow;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.ILatinSquare;
import commons.utils.FileUtils;

/**
 * 
 * This class is prepared to be called from a runnable jar in a system console.
 *  It provides an access point to all the methods for generating LSs in the project. 
 * 
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class GeneratorJar {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length<2) {
			System.out.println("_____________________________________________________________________________");
			System.out.println("");
			System.out.println("igs-lsgp (Ignacio Gallego Sagastume's Latin Square generation package).");
			System.out.println("© 2014-2016 by Mg. Ignacio Gallego Sagastume.");
			System.out.println("_____________________________________________________________________________");
			System.out.println("");
			System.out.println("Usage: <method> <order> [write <path>]");
			System.out.println("Where <method> ::= simple | product | swapping | restart | graph | jm | mckay");
			System.out.println("_____________________________________________________________________________");
			return;
		}
		
		int n = new Integer(args[1]);
		String path = null;
		if (args.length>2) {
			if (args[2]!=null && args[2].equalsIgnoreCase("write")) {
				if (args.length==4)
					path = args[3];
				else {
					System.out.println("Bad usage. The write option needs a path.");
					return;
				}
			} else {
				System.out.println("Bad usage. Parameter 3 must be 'write'.");
				return;
			}
		}
		IRandomLatinSquareGenerator generator;
		
		if (args[0].equalsIgnoreCase("simple")) {
			generator = new SimpleGenWithBacktracking(n);
			computeTimeFor(generator, path);
			return;
		}
		
		if (args[0].equalsIgnoreCase("product")) {
			generator = new KoscielnyProduct(n);
			computeTimeFor(generator, path);  //does not generate LS uniformly distributed
			return;
		}

		if (args[0].equalsIgnoreCase("swapping")) {
			generator = new SimpleGenWithRandomSwapping(n);
			computeTimeFor(generator, path);  // the most acceptable simple method
			return;
		}
		
		if (args[0].equalsIgnoreCase("restart")) {
			generator = new SimpleGenWithRestartRow(n);
			computeTimeFor(generator, path);//improvements to simple method?
			return;
		}
		
		if (args[0].equalsIgnoreCase("graph")) {
			generator = new SimpleGenWithReplGraph(n);
			computeTimeFor(generator, path);
			return;
		}
		
		if (args[0].equalsIgnoreCase("jm")) {
			generator = new JacobsonMatthewsLSGenerator(n);
			computeTimeFor(generator, path);
			return;
		}
		
		if (args[0].equalsIgnoreCase("mckay")) {
			if (n<=2) {
				System.out.println("Could not generate structure");
				return;
			}
			double cubicRoot = Math.pow(n, 1.0/3.0);
			int k = (int)cubicRoot+1;
			
			generator = new McKayLRGenerationMethod(k,n);
			computeTimeFor(generator, path);
			return;
		}
		
		System.out.println("Option not supported.");

	}
	
	public static void computeTimeFor(IRandomLatinSquareGenerator generator, String path) {
		long startTime = System.nanoTime();
		ILatinSquare ls = generator.generateLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		System.out.println("Random structure generated in "+secs+" seconds. Generation method: "+generator.getMethodName());		
	}
	
//	@SuppressWarnings("unchecked")
//	public static void debugRandomSwapping() throws Exception {
//		ArrayListLatinSquare ls = new ArrayListLatinSquare(5);
//		SimpleGenWithRandomSwapping rs = new SimpleGenWithRandomSwapping(5);
//		
//		ls.setValueAt(0, 0, 0);
//		ls.setValueAt(0, 1, 1);
//		ls.setValueAt(0, 2, 2);
//		ls.setValueAt(0, 3, 3);
//		ls.setValueAt(0, 4, 4);
//		
//		ls.setValueAt(1, 0, 4);
//		ls.setValueAt(1, 1, 0);
//		ls.setValueAt(1, 2, 1);
//		ls.setValueAt(1, 3, 2);
//		ls.setValueAt(1, 4, 3);
//		
//		ls.setValueAt(2, 0, 1);
//		ls.setValueAt(2, 1, 2);
//		ls.setValueAt(2, 2, 3);
//		ls.setValueAt(2, 3, 4);
//		ls.setValueAt(2, 4, 0);
//		
//		ArrayList<Integer> row = new ArrayList<Integer>();
//		row.add(2);
//		row.add(3);
//		row.add(0);
//		row.add(1);
//		row.add(4);
//		
//		Set<Integer> columnsWithRepetitions = new HashSet<Integer>();
//		HashSet<Integer>[] availableInCol = new HashSet[5];
//		
//		columnsWithRepetitions.add(4);
//		availableInCol[0] = new HashSet<Integer>();
//		availableInCol[0].add(3);
//		
//		availableInCol[1] = new HashSet<Integer>();
//		availableInCol[1].add(4);
//		
//		availableInCol[2] = new HashSet<Integer>();
//		availableInCol[2].add(4);
//		
//		availableInCol[3] = new HashSet<Integer>();
//		availableInCol[3].add(0);
//		
//		availableInCol[4] = new HashSet<Integer>();
//		availableInCol[4].add(1);availableInCol[4].add(2);
//		
//		rs.fixRow(3, row, columnsWithRepetitions);
//		
//		System.out.println(ls);
//		System.out.println(row);
//	}
//	
//	
//	@SuppressWarnings("unchecked")
//	public static void debugReplGraph() throws Exception {
//		ArrayListLatinSquare ls = new ArrayListLatinSquare(6);
//		SimpleGenWithReplGraph rg = new SimpleGenWithReplGraph(6);
//		
//		ls.setValueAt(0, 0, 3);
//		ls.setValueAt(0, 1, 1);
//		ls.setValueAt(0, 2, 5);
//		ls.setValueAt(0, 3, 2);
//		ls.setValueAt(0, 4, 0);
//		ls.setValueAt(0, 5, 4);
//		
//		ls.setValueAt(1, 0, 2);
//		ls.setValueAt(1, 1, 3);
//		ls.setValueAt(1, 2, 4);
//		ls.setValueAt(1, 3, 1);
//		ls.setValueAt(1, 4, 5);
//		ls.setValueAt(1, 5, 0);
//
//		ArrayList<Integer> row = new ArrayList<Integer>();
//		row.add(4);
//		row.add(5);
//		row.add(2);
//		row.add(3);
//		row.add(1);
//		
//		HashSet<Integer>[] availableInCol = new HashSet[6];
//		
//		availableInCol[0] = new HashSet<Integer>();
//		availableInCol[0].add(0);
//		availableInCol[0].add(1);
//		availableInCol[0].add(5);
//		
//		availableInCol[1] = new HashSet<Integer>();
//		availableInCol[1].add(0);
//		availableInCol[1].add(2);
//		availableInCol[1].add(4);
//		
//		availableInCol[2] = new HashSet<Integer>();
//		availableInCol[2].add(0);
//		availableInCol[2].add(1);
//		availableInCol[2].add(3);
//		
//		availableInCol[3] = new HashSet<Integer>();
//		availableInCol[3].add(0);
//		availableInCol[3].add(4);
//		availableInCol[3].add(5);
//		
//		availableInCol[4] = new HashSet<Integer>();
//		availableInCol[4].add(2);
//		availableInCol[4].add(3);
//		availableInCol[4].add(4);
//		
//		availableInCol[5] = new HashSet<Integer>();
//		availableInCol[5].add(1);
//		availableInCol[5].add(2);
//		availableInCol[5].add(3);
//		availableInCol[5].add(5);
//		
//		HashSet<Integer>[] availInitial = new HashSet[6];
//		
//		availInitial[0] = new HashSet<Integer>();
//		availInitial[0].add(0);
//		availInitial[0].add(1);
//		availInitial[0].add(5);
//		
//		availInitial[1] = new HashSet<Integer>();
//		availInitial[1].add(0);
//		availInitial[1].add(2);
//		availInitial[1].add(4);
//		
//		availInitial[2] = new HashSet<Integer>();
//		availInitial[2].add(0);
//		availInitial[2].add(1);
//		availInitial[2].add(3);
//		
//		availInitial[3] = new HashSet<Integer>();
//		availInitial[3].add(0);
//		availInitial[3].add(4);
//		availInitial[3].add(5);
//		
//		availInitial[4] = new HashSet<Integer>();
//		availInitial[4].add(2);
//		availInitial[4].add(3);
//		availInitial[4].add(4);
//		
//		Integer col = 4;
//		HashMap<Integer, HashSet<Integer>> map = rg.constructReplGraph(row, col, availInitial);
//		
//		HashSet<Integer> availableInRow = new HashSet<Integer>();
//		availableInRow.add(0);
//		availableInRow.add(0);
//		int elem = 2;
//		rg.makeElemAvailable(elem, map, row, col, availableInRow);
//		System.out.println(row);
//		
//		System.exit(0);
//	}
}
