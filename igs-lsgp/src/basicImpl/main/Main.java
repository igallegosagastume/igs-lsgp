/**
 * Creation date: 08/07/2014
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
package basicImpl.main;

import java.util.ArrayList;
import java.util.HashSet;

import basicImpl.koscielnyProd.ProductImpl;
import basicImpl.model.LatinSquare;
import basicImpl.model.SimpleGen;
import basicImpl.model.SimpleGenWithCycleSwapping;
import basicImpl.model.SimpleGenWithRandomSwapping;
import basicImpl.model.SimpleGenWithReplGraph;
import basicImpl.model.SimpleGenWithRestartRow;
import basicImpl.model.SimpleGenWithSwapping;
import commons.FileUtils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
				
		if (args.length<2) {
			System.out.println("Usage: <method> <LS order> [write <path>]");
			System.out.println("Where <method> ::= simple | product | swapping | restart | graph ");
			return;
		}
		
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
		if (args[0].equalsIgnoreCase("product")) {
			computeTimeForKoscielnyProduct(new Integer(args[1]), path);  //does not generate LS uniformly distributed
			return;
		}
		
		if (args[0].equalsIgnoreCase("simple")) {
			computeTimeForSimpleGeneration(new Integer(args[1]), path);
			return;
		}

//		computeTimeForSimpleGenerationWithCycleSwapping(7);//there are cases that enter into an infinite Loop

		if (args[0].equalsIgnoreCase("swapping")) {
			simpleGenWithRandomSwapping(new Integer(args[1]), path);  // the most acceptable simple method
			return;
		}
		
		if (args[0].equalsIgnoreCase("restart")) {
			computeTimeForSimpleGenerationWithStartOverRow(new Integer(args[1]), path);//improvements to simple method?
			return;
		}
		
		if (args[0].equalsIgnoreCase("graph")) {
			computeTimeForSimpleGenerationWithReplGraph(new Integer(args[1]), path);//improvements to simple method?
			return;
		}
		
		System.out.println("Option not supported.");
//		debugRandomSwapping();
	}

	public static void computeTimeForSimpleGeneration(int n, String path) {
		long startTime = System.nanoTime();
		LatinSquare ls = new SimpleGen(n).genLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: Simple generation with backtracking.");
	}
	
	public static void computeTimeForKoscielnyProduct(int n, String path) throws Exception {
		long startTime = System.nanoTime();
		LatinSquare ls = new ProductImpl(n).genLSMult();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: Koscielny product.");
	}
	
	@Deprecated
	public static void computeTimeForSimpleGenerationWithSwapping(int n) {
		long startTime = System.nanoTime();
		LatinSquare ls = new SimpleGenWithSwapping(n).genLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		System.out.println("LS generated in: "+secs+" seconds.");
	}
	
	@Deprecated
	public static void computeTimeForSimpleGenerationWithCycleSwapping(int n) {
		long startTime = System.nanoTime();
		LatinSquare ls = new SimpleGenWithCycleSwapping(n).genLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		System.out.println("LS generated in: "+secs+" seconds.");
	}
	
	public static void simpleGenWithRandomSwapping(int n, String path) {
		long startTime = System.nanoTime();
		LatinSquare ls = new SimpleGenWithRandomSwapping(n).genLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: Random swapping.");
		
		
	}
	
	public static void computeTimeForSimpleGenerationWithStartOverRow(int n, String path) {
		long startTime = System.nanoTime();
		LatinSquare ls = new SimpleGenWithRestartRow(n).genLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: Simple generation with restart row.");
	}

	public static void computeTimeForSimpleGenerationWithReplGraph(int n, String path) {
		long startTime = System.nanoTime();
		LatinSquare ls = new SimpleGenWithReplGraph(n).genLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		System.out.println("LS generated in: "+secs+" seconds. Generation method: Simple generation with replacement graph.");
	}
	
	public static void debugRandomSwapping() throws Exception {
		LatinSquare ls = new LatinSquare(5);
		SimpleGenWithRandomSwapping rs = new SimpleGenWithRandomSwapping(5);
		
		ls.setValueAt(0, 0, 0);
		ls.setValueAt(0, 1, 1);
		ls.setValueAt(0, 2, 2);
		ls.setValueAt(0, 3, 3);
		ls.setValueAt(0, 4, 4);
		
		ls.setValueAt(1, 0, 4);
		ls.setValueAt(1, 1, 0);
		ls.setValueAt(1, 2, 1);
		ls.setValueAt(1, 3, 2);
		ls.setValueAt(1, 4, 3);
		
		ls.setValueAt(2, 0, 1);
		ls.setValueAt(2, 1, 2);
		ls.setValueAt(2, 2, 3);
		ls.setValueAt(2, 3, 4);
		ls.setValueAt(2, 4, 0);
		
		ArrayList<Integer> row = new ArrayList<Integer>();
		row.add(2);
		row.add(3);
		row.add(0);
		row.add(1);
		row.add(4);
		
		HashSet<Integer> columnsWithRepetitions = new HashSet<Integer>();
		HashSet<Integer>[] availableInCol = new HashSet[5];
		
		columnsWithRepetitions.add(4);
		availableInCol[0] = new HashSet<Integer>();
		availableInCol[0].add(3);
		
		availableInCol[1] = new HashSet<Integer>();
		availableInCol[1].add(4);
		
		availableInCol[2] = new HashSet<Integer>();
		availableInCol[2].add(4);
		
		availableInCol[3] = new HashSet<Integer>();
		availableInCol[3].add(0);
		
		availableInCol[4] = new HashSet<Integer>();
		availableInCol[4].add(1);availableInCol[4].add(2);
		
		rs.fixRow(3, 5, ls, row, columnsWithRepetitions, availableInCol);
		
		System.out.println(ls);
		System.out.println(row);
	}
	
}
