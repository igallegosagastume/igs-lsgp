package commons.test.replGraph256test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import commons.ILatinSquare;

import basicImpl.model.SimpleGen;
import basicImpl.model.SimpleGenWithReplGraph;

public class ATester implements Runnable {

	private void printCounts(HashMap<Integer, Integer> counts) {
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
	
	public boolean finish = false;
	
	@Override
	public void run() {
		int order = 256;
		long startTime = System.nanoTime();	
		
		//create the generator
		SimpleGen generator = new SimpleGenWithReplGraph(order);
			
		ILatinSquare ls;

		HashMap<Integer, ILatinSquare> cuads = new HashMap<Integer, ILatinSquare>();
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
	
		for (int i=0; !finish ; i++) {
			
			ls = generator.genLS();
			
			boolean found = false;
			Iterator<Integer> cuadrados = cuads.keySet().iterator();
			
			for (; cuadrados.hasNext() && !found;) {
				Integer index = cuadrados.next();
				try {
					if (ls.equals(cuads.get(index))) {
						found = true;
						counts.put(index, counts.get(index)+1);
					}
				} catch (Exception e) {System.out.println("Exception trying to compare 2 LSs."); }
			}
			if (!found) {
				Integer index2 = cuads.keySet().size(); 
				cuads.put(index2, ls);
				counts.put(index2, 1);
			}
			System.out.println(i);
		}
		
		printCounts(counts);
		
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		double mins = secs / 60;
		
		printMaxMin(counts);
		System.out.println("Test concluded after "+mins+" minutes.  Generation method: Replacement graph...");
		 
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
		System.out.println("Max count.:" + max + ". Min count.:" + min);
	}

}
