package commons.test.concurrentChi;

import jacomatt.model.EfficientIncidenceCube;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import basicImpl.model.SimpleGen;
import basicImpl.model.SimpleGenWithRandomSwapping;
import basicImpl.model.SimpleGenWithReplGraph;
import commons.ILatinSquare;
import commons.test.ChiSquareTest;

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

		

		SimpleGen generator = new SimpleGen(4); //default
		//creates the generator
		if (option.equalsIgnoreCase("swapping")) 
			generator = new SimpleGenWithRandomSwapping(order);
		if (option.equalsIgnoreCase("graph"))
			generator = new SimpleGenWithReplGraph(order);
			
		ILatinSquare ls;

		HashMap<Integer, byte[]> cuads = new HashMap<Integer, byte[]>();
		HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
		int i = 0;
		
		if (option.equalsIgnoreCase("swapping")
				|| option.equalsIgnoreCase("graph")) {
			for (i=0; !finish ; i++) {
				ls = generator.genLS();
				byte[] dig1 = ls.hashCodeOfLS();
				
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

				byte[] dig1 = ls.hashCodeOfLS();
				
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
		System.out.println("Max count:"+max+". Min count:"+min);
	}
	
}
