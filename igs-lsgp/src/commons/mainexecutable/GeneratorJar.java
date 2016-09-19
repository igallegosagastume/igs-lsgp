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
import koscielny.model.generators.KoscielnyProductGenerator;
import mckaywormald.model.generators.McKayLRGenerationMethod;
import selvi_et_al.model.generators.OCarrollWithRestartLSGenerator;
import selvi_et_al.model.generators.SelviEtAlLSGenerator;
import seqgen.model.generators.SeqGenWithBacktracking;
import seqgen.model.generators.SeqGenWithRandomSwapping;
import seqgen.model.generators.SeqGenWithReplGraph;
import seqgen.model.generators.SeqGenWithRestartRow;

import commons.generators.IRandomLatinSquareGenerator;
import commons.model.latinsquares.ILatinSquare;
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
	 * 
	 *  Construct a graph with the method passed as parameter, of any order, and writes to file if requested.
	 *   
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length<2) {
			System.out.println("________________________________________________________________________________");
			System.out.println("");
			System.out.println("igs-lsgp (Ignacio Gallego Sagastume's Latin Square generation package).");
			System.out.println("© 2014-2016 by Mg. Ignacio Gallego Sagastume.");
			System.out.println("________________________________________________________________________________");
			System.out.println("");
			System.out.println("Usage: <method> <order> [write <path> | repeat <times>]");
			System.out.println("Where <method> ::= back       | ");
			System.out.println("                   product    | ");
			System.out.println("                   swapping   | ");
			System.out.println("                   restart    | ");
			System.out.println("                   graph      | ");
			System.out.println("                   jm         | ");
			System.out.println("                   mckay      | ");
			System.out.println("                   ocarrollr  | ");
			System.out.println("                   selvi");
			System.out.println("________________________________________________________________________________");
			return;
		}
		
		int n = new Integer(args[1]);
		int times = 1;
		String path = null;
		if (args.length>2) {
			if (args[2]!=null && args[2].equalsIgnoreCase("write")) {
				if (args.length==4)
					path = args[3];
				else {
					System.out.println("Bad usage. The WRITE option needs a path.");
					return;
				}
			} else { 
				if (args[2]!=null && args[2].equalsIgnoreCase("repeat")) {
					
					if (args.length==4)
						times = new Integer(args[3]);
					else {
						System.out.println("Bad usage. The REPEAT option needs a number of repetitions.");
						return;
					}
				}
			}
		}
		IRandomLatinSquareGenerator generator;
		
		String method = args[0];
		
		if (!method.equalsIgnoreCase("back") &&
			!method.equalsIgnoreCase("product") &&
			!method.equalsIgnoreCase("swapping") &&
			!method.equalsIgnoreCase("restart") &&
			!method.equalsIgnoreCase("graph") &&
			!method.equalsIgnoreCase("jm") &&
			!method.equalsIgnoreCase("mckay") &&
			!method.equalsIgnoreCase("ocarrollr") &&
			!method.equalsIgnoreCase("selvi") 
			) {
				System.out.println("Method not supported: "+method);
				return;
		}
		
		if (method.equalsIgnoreCase("back")) {
			generator = new SeqGenWithBacktracking(n);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("product")) {
			generator = new KoscielnyProductGenerator(n);
			repeatGeneration(generator, path, times);
		}

		if (method.equalsIgnoreCase("swapping")) {
			generator = new SeqGenWithRandomSwapping(n);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("restart")) {
			generator = new SeqGenWithRestartRow(n);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("graph")) {
			generator = new SeqGenWithReplGraph(n);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("jm")) {
			generator = new JacobsonMatthewsLSGenerator(n);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("mckay")) {
			if (n<=2) {
				System.out.println("Could not generate structure");
				return;
			}
			double cubicRoot = Math.pow(n, 1.0/3.0);
			int k = (int)cubicRoot+1;
			generator = new McKayLRGenerationMethod(k,n);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("ocarrollr")) {//o'carroll with restart
			generator = new OCarrollWithRestartLSGenerator(n);
			generator.setVerbose(false);
			repeatGeneration(generator, path, times);
		}
		
		if (method.equalsIgnoreCase("selvi")) {//selvi et.al. algorithm (variation of ocarroll with backtracking)
			generator = new SelviEtAlLSGenerator(n);
			generator.setVerbose(false);
			repeatGeneration(generator, path, times);
		}
		
	}
	
	/**
	 * Computes the time and generates the LS of requested order.
	 * 
	 * @param generator
	 * @param path
	 */
	public static double computeTimeFor(IRandomLatinSquareGenerator generator, String path, boolean showFinalMessage) {
		long startTime = System.nanoTime();
		ILatinSquare ls = generator.generateLS();
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		
		System.out.println(ls);
		
		FileUtils.writeLS(ls, path);
		
		if (showFinalMessage)
			System.out.println("Random structure generated in "+secs+" seconds. Generation method: "+generator.getMethodName());
		
		if (!ls.preservesLatinProperty()) {
			System.out.println();
			System.out.println("ERROR: the generated structure does not preserve the Latin property.");
			System.exit(0);
		} else {
			//System.out.println("The structure preserves the Latin property.");
		}
		return secs;
	}

	
	
	private static void repeatGeneration(IRandomLatinSquareGenerator generator, String path, int times) {

		long startTime = System.nanoTime();

		
		if (times==1) {
			computeTimeFor(generator, path, true);
			return;
		}
		//List<Double> generationTimes = new ArrayList<Double>();
		Double sum = 0.0;
		for (int i=1; i<=times; i++) {
			double secsForAGen = computeTimeFor(generator, path, false);
//			generationTimes.add(secs);
			sum +=secsForAGen;
		}
		long endTime = System.nanoTime();

		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		double mins = secs / 60;
		double hours = mins/60;
		
		Double averageTime = sum / (double)times;
		System.out.println("");
		if (hours>=1)
			System.out.println("Finished "+times+" repetitions after "+hours+" hours.");
		else 
			if (mins>=1)
				System.out.println("Finished "+times+" repetitions after "+mins+" minutes.");
			else
				System.out.println("Finished "+times+" repetitions after "+secs+" seconds.");
		System.out.println("Generation method: "+generator.getMethodName());
		System.out.println("Average time of method is "+averageTime+" seconds.");
	}
//	@SuppressWarnings("unchecked")
//	public static void debugRandomSwapping() throws Exception {
//		ArrayListLatinSquare ls = new ArrayListLatinSquare(5);
//		SeqGenWithRandomSwapping rs = new SeqGenWithRandomSwapping(5);
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
//		SeqGenWithReplGraph rg = new SeqGenWithReplGraph(6);
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
