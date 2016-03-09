/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import mckaywormald.model.LatinRectangle;

import commons.RandomUtils;

/**
 * @author igallego
 *
 */
public class McKayLRGenerationMethod {
	
	private Set<Integer> initiallyAvInRow = null;
	private int conflictsCount = 0;
	private int overlappingConflicts = 0;
	
	public McKayLRGenerationMethod() {
		RandomUtils.initRand();
	}
	
	public LatinRectangle generateLR(int k, int n) {
		this.initiallyAvInRow = RandomUtils.oneToN(n);
		this.conflictsCount = 0;
		this.overlappingConflicts = 0;
		
		boolean rejected = false;
		LatinRectangle a = null;
		
		do {
			do {
		    	a = randomMemberOfMkn(k, n);
		    } while (this.conflictsCount>Math.pow(n, 2) && this.overlappingConflicts>0);
	
		    rejected = false;
		    
		    while (this.conflictsCount>0 && !rejected) {
		    	rejected = true;
		    }
		} while (rejected);
	    return a;
	}
	
	private LatinRectangle randomMemberOfMkn(int k, int n) {
		LatinRectangle lr = new LatinRectangle(k, n);
	    
	    for (int i=0; i<n; i++) {
	    	ArrayList<Integer> row = this.generateRow(i, n, lr);
	    	
	    	lr.setRow(i, row);
	    }
	    return lr;
	}
	        
	private ArrayList<Integer> generateRow(int i_row, int n, LatinRectangle ls) {
	    HashSet<Integer> availableInRow = new HashSet<Integer>();
	    availableInRow.addAll(this.initiallyAvInRow);
	    
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int j = 0;
	    while (j < n) {
	            Integer symbol = RandomUtils.randomChoice(availableInRow);
	            availableInRow.remove(symbol);
	            row.add(symbol);
	            j++;
	    }
	    
	    return row;
	}
	        
	public String getMethodName() {
		return "McKay generation of Latin rectangle.";
	}

}
		        