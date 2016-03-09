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
	
		Set<Integer> initiallyAvInRow = null;
	
		public McKayLRGenerationMethod() {
			RandomUtils.initRand();
		}
		
		public LatinRectangle generateLS(int n) {
			this.initiallyAvInRow = RandomUtils.oneToN(n);
			
			LatinRectangle a = null;
		    do {
		    	a = randomMemberOfMkn(n);
		    } while (a.hasOverlappingConflicts());

		    return a;
		}
		
		private LatinRectangle randomMemberOfMkn(int n) {
			LatinRectangle ls = new LatinRectangle(n, n);
    	    
		    for (int i=0; i<n; i++) {
		    	ArrayList<Integer> row = this.generateRow(i, n, ls);
		    	
		    	ls.setRow(i, row);
		    }
		    return ls;
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
		        