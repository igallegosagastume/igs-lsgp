package commons.test.concurrentTest;
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

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import commons.ILatinSquare;
import basicImpl.model.generators.SimpleGenWithBacktracking;
import basicImpl.model.generators.SimpleGenWithReplGraph;

public class CountLSsTester implements Runnable {

	private int order = 5;
	
	
	public CountLSsTester(int n) {
		
		this.order = n;
	}
	
	
//	private void printCounts(HashMap<Integer, Integer> counts) {
//		ArrayList<Integer> lsi = new ArrayList<Integer>();
//		lsi.addAll(counts.keySet());
//		Collections.sort(lsi);
//		
//		Iterator<Integer> idx = lsi.iterator();
//		
//		System.out.println("The following is a list with the count of each LS i:");
//		while (idx.hasNext()) {
//			Integer i = (Integer) idx.next();
//			System.out.print(counts.get(i)+",");			
//		}
//	}
	
	public boolean finish = false;
	
	@Override
	public void run() {
		
		long startTime = System.nanoTime();	
		
		//create the generator
		SimpleGenWithBacktracking generator = new SimpleGenWithReplGraph(this.order);
			
		ILatinSquare ls;

		HashMap<Integer, byte[]> cuads = new HashMap<Integer, byte[]>();
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
	
		for (int i=0; !finish ; i++) {
			
			ls = generator.generateLS();
			byte[] dig1 = ls.hashCodeOfStructure();
			
			boolean found = false;
			Iterator<Integer> cuadrados = cuads.keySet().iterator();
			
			for (; cuadrados.hasNext() && !found;) {
				Integer index = cuadrados.next();
				try {
					if (MessageDigest.isEqual(dig1, cuads.get(index))) {
					
//					if (ls.equals(cuads.get(index))) {
						found = true;
						counts.put(index, counts.get(index)+1);
					}
				} catch (Exception e) {System.out.println("Exception trying to compare 2 LSs."); }
			}
			if (!found) {
				Integer index2 = cuads.keySet().size(); 
//				cuads.put(index2, ls);
				cuads.put(index2, dig1);
				counts.put(index2, 1);
			}
			System.out.println(i);
		}
		
//		printCounts(counts);
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		double mins = secs / 60;
		double hours = mins/60;
		
		printMaxMin(counts);
		
		if (mins>60)
			System.out.println("Test concluded after "+hours+" hours.  Generation method: Replacement graph.");
		else
			System.out.println("Test concluded after "+mins+" minutes.  Generation method: Replacement graph.");

		System.out.println("Order of the generated LSs:"+this.order);
	}




	private void printMaxMin(HashMap<Integer, Integer> counts) {
		ArrayList<Integer> cantidades = new ArrayList<Integer>();
		cantidades.addAll(counts.keySet());
		Collections.sort(cantidades);

		Iterator<Integer> idx = cantidades.iterator();
		int max = 1;
		int min = 999999999;
		while (idx.hasNext()) {
			Integer cantKey = (Integer) idx.next();
			Integer cant = counts.get(cantKey);
			if (cant > max)
				max = cant;
			if (cant != 0 && cant < min)
				min = cant;
		}
		System.out.println("");
		System.out.println("Max count.:" + max + ". Min count.:" + min);
	}

}
