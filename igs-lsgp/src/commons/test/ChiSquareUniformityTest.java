/**
 * Creation date: 08/11/2014
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

package commons.test;
import jacomatt.model.EfficientIncidenceCube;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import basicImpl.model.generators.SimpleGenWithBacktracking;
import basicImpl.model.generators.SimpleGenWithRandomSwapping;
import basicImpl.model.generators.SimpleGenWithReplGraph;
import commons.ILatinSquare;

public class ChiSquareUniformityTest {

	public static void main(String[] args) throws Exception {
		long startTime = System.nanoTime();

		if (args.length<3) {
			System.out.println("Usage: [ jacomatt | swapping | graph ] <generations> <LS order>");
			System.out.println("Example 1: swapping 1000000 4");
			System.out.println("Example 2: jacomatt 10000000 5");
			return;
		}
		String option = args[0]; 
		
		int cantExperim = new Integer(args[1]);//1000000;
		int order = new Integer(args[2]);

		SimpleGenWithBacktracking generator = new SimpleGenWithBacktracking(4); //default
		//creates the generator
		if (option.equalsIgnoreCase("swapping")) 
			generator = new SimpleGenWithRandomSwapping(order);
		if (option.equalsIgnoreCase("graph"))
			generator = new SimpleGenWithReplGraph(order);
			
		ILatinSquare ls;

		HashMap<Integer, ILatinSquare> cuads = new HashMap<Integer, ILatinSquare>();
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		
		for (int i=0; i<cantExperim ; i++) {
			
			if (option.equalsIgnoreCase("swapping")
				|| option.equalsIgnoreCase("graph")) {
				ls = generator.generateLS();
			} else {
				ls = new EfficientIncidenceCube(order);//creates cyclic IC
				((EfficientIncidenceCube)ls).shuffle();//shuffles IC = n^3 ops
			}
			
			boolean found = false;
			Iterator<Integer> cuadrados = cuads.keySet().iterator();
			
			for (; cuadrados.hasNext() && !found;) {
				Integer index = cuadrados.next();
				if (ls.equals(cuads.get(index))) {
					found = true;
					counts.put(index, counts.get(index)+1);
				}
			}
			if (!found) {
				Integer index2 = cuads.keySet().size(); 
				cuads.put(index2, ls);
				counts.put(index2, 1);
			}
			if (i%10000==0) {
				System.out.println(i);
			}
		}
		
		printCounts(counts);
		
//		printRepetitions(counts);
		
		ChiSquareTest test = new ChiSquareTest();
		
		test.doTest(counts, cantExperim, order);
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		double mins = secs / 60;
		
		printMaxMin(counts);
		System.out.println("Uniformity test concluded after "+mins+" minutes.  Generation method: "+option);
	}
	
	private static void printCounts(HashMap<Integer, Integer> counts) {
		ArrayList<Integer> lsi = new ArrayList<Integer>();
		lsi.addAll(counts.keySet());
		Collections.sort(lsi);
		
		Iterator<Integer> idx = lsi.iterator();
		
		System.out.println("The following is a list with the count of each LS i:");
		while (idx.hasNext()) {
			Integer i = (Integer) idx.next();
			System.out.print(counts.get(i)+",");			
		}
	}
	
	private static void printMaxMin(HashMap<Integer, Integer> counts) {
		ArrayList<Integer> cantidades = new ArrayList<Integer>();
		cantidades.addAll(counts.keySet());
		Collections.sort(cantidades);
		
		Iterator<Integer> idx = cantidades.iterator();
		int max = 1;
		int min = 999999999;
		while (idx.hasNext()) {
			Integer cantKey = (Integer) idx.next();
			Integer cant = counts.get(cantKey); 
			if (cant>max)
				max = cant;
			if (cant!=0 && cant<min)
				min = cant;
		}
		System.out.println("Max count.:"+max+". Min count.:"+min);
	}
	
	@SuppressWarnings("unused")
	private static void printRepetitions(HashMap<Integer, Integer> counts) {
		Iterator<Integer> cuadrados = counts.keySet().iterator();
		HashMap<Integer, Integer> results = new HashMap<Integer,Integer>();
		
		final int maxCount = 1000;
		for (int i=1; i<maxCount; i++)  {
			results.put(i, 0);
		}
		for (;cuadrados.hasNext();) {
			int index = cuadrados.next();
			boolean found = false;
			for (int i=1; i<maxCount && !found; i++)  {
				if (counts.get(index)==i) {
					results.put(i, results.get(i)+1);
					found = true;
				}
			}
		}
		for (int i=1; i<maxCount; i++)  {
			if (results.get(i)!=0)
				System.out.println("Cantidad de cuadrados con "+i+" repeticiones: "+results.get(i));
		}
	}

}
