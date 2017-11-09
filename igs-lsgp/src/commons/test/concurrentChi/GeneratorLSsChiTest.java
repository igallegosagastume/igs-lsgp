/**
 * Creation date: 18/05/2015
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * � Copyright 2012-2015 Ignacio Gallego Sagastume
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

package commons.test.concurrentChi;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import commons.model.latinsquares.ILatinSquare;
import commons.test.ChiSquareTest;
import jacomatt.model.EfficientIncidenceCube;
import seqgen.model.generators.AbstractSequentialGenerator;
import seqgen.model.generators.SeqGenWithBacktracking;
import seqgen.model.generators.SeqGenWithRandomSwapping;
import seqgen.model.generators.SeqGenWithReplGraph;


/**
 * This class generates the LSs of order n<=7 needed for the test
 *  with the swapping, graph or the Jacobson &amp; matthews' method.
 * 
 * @author igallego
 *
 */
public class GeneratorLSsChiTest implements Runnable {
	private String option = "jacomatt";
	private int order = 5;
	public boolean finish = false;

	
	public GeneratorLSsChiTest(String option, int n) {
		this.order = n;
		this.option = option;
	}
	
	@Override
	public void run() {
	
		long startTime = System.nanoTime();

		

		AbstractSequentialGenerator generator = new SeqGenWithBacktracking(4); //default
		//creates the generator
		if (option.equalsIgnoreCase("swapping")) 
			generator = new SeqGenWithRandomSwapping(order);
		if (option.equalsIgnoreCase("graph"))
			generator = new SeqGenWithReplGraph(order);
			
		ILatinSquare ls;

		HashMap<Integer, byte[]> cuads = new HashMap<Integer, byte[]>();
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		int i = 0;
		
		if (option.equalsIgnoreCase("swapping")
				|| option.equalsIgnoreCase("graph")) {
			for (i=0; !finish ; i++) {
				ls = generator.generateLS();
				byte[] dig1 = ls.hashCodeOfStructure();
				
				boolean found = false;
				Iterator<Integer> cuadrados = cuads.keySet().iterator();
				
				for (; cuadrados.hasNext() && !found;) {
					Integer index = cuadrados.next();
	//				if (ls.equals(cuads.get(index))) {
					if (MessageDigest.isEqual(dig1, cuads.get(index))) {
						found = true;
						counts.put(index, counts.get(index)+1);
					}
				}
				if (!found) {
					Integer index2 = cuads.keySet().size(); 
	//				cuads.put(index2, ls);
					cuads.put(index2, dig1);
					counts.put(index2, 1);
				}
				if (i%10000==0)
					System.out.println(i);
			}
		} else {
			for (i=0; !finish ; i++) {
				ls = new EfficientIncidenceCube(order);//creates cyclic IC
				((EfficientIncidenceCube)ls).shuffle();//shuffles IC = n^3 ops

				byte[] dig1 = ls.hashCodeOfStructure();
				
				boolean found = false;
				Iterator<Integer> cuadrados = cuads.keySet().iterator();
				
				for (; cuadrados.hasNext() && !found;) {
					Integer index = cuadrados.next();
	//				if (ls.equals(cuads.get(index))) {
					if (MessageDigest.isEqual(dig1, cuads.get(index))) {
						found = true;
						counts.put(index, counts.get(index)+1);
					}
				}
				if (!found) {
					Integer index2 = cuads.keySet().size(); 
	//				cuads.put(index2, ls);
					cuads.put(index2, dig1);
					counts.put(index2, 1);
				}
				if (i%10000==0)
					System.out.println(i);
			}
		}

		if (order <= 7) {
			ChiSquareTest test = new ChiSquareTest();
			test.doTest(counts, i, order);
		} else {
			System.out.println("Chi test only supported for n<=7, skipping...");
			System.out.println("Total count of LSs generated: "+i);
		}
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		double mins = secs / 60;
		double hours = mins/60;
		
		printMaxMin(counts);
		
		System.out.println("Order of the generated LSs:"+this.order);
		
		if (mins>60)
			System.out.println("Uniformity test concluded after "+hours+" hours. Generation method: "+option);
		else
			System.out.println("Uniformity test concluded after "+mins+" minutes. Generation method: "+option);
	}
	
	private static void printMaxMin(HashMap<Integer, Integer> counts) {
		ArrayList<Integer> cantidades = new ArrayList<Integer>(counts.keySet());
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
		System.out.println("Max count:"+max+". Min count:"+min);
	}
	
}
