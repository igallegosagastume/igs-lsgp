/**
 * Creation date: 08/05/2015
 * 
 */
package basicImpl.model;

import java.util.ArrayList;
import java.util.HashSet;

import basicImpl.utils.RandomUtils;

/**
 * @author igallego
 *
 */
public class SimpleGenWithRestartRow extends SimpleGen {
	
	
	public SimpleGenWithRestartRow(int n) {
		super(n);
	}
	
//	private HashSet<Integer>[] copyCollection(HashSet<Integer>[] col) {
//		@SuppressWarnings("unchecked")
//		HashSet<Integer>[] newCol =  new HashSet[n];
//		
//		for(int i=0; i<n; i++) {
//			newCol[i] = new HashSet<Integer>();
//			newCol[i].addAll(col[i]);
//		}
//		
//		return newCol;
//	}
	
	@Override
	public ArrayList<Integer> generateRow(int i_row, int n, LatinSquare ls, HashSet<Integer>[] availableInCol, Integer[] failedAttemptsPerRow, int[][] collisions) {
		HashSet<Integer> availableInRow = new HashSet<Integer>();
	    for (int j=0; j<n; j++) {
	    	availableInRow.add(j);
	    }
	    
	    int colcount = 0;
	    
	    //result of this method
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
//	    ArrayList<Integer> beginWith = new ArrayList<Integer>();
	    
	    while (i_col < n) {//when i_col is n, there are n chosen numbers
	        //available is:
	        HashSet<Integer> available = new HashSet<Integer>();
	        available.addAll(availableInCol[i_col]);
	    	available.retainAll(availableInRow);
	    	
//	    	if (i_col==0) {
//	    		available.removeAll(beginWith);
//	    		if (available.isEmpty()) {
//	    			available.addAll(beginWith);
//	    		}
//	    	}
	    	
	        if (!available.isEmpty()) { //if there are available
	            //choose a symbol at random
	            Integer symbol = RandomUtils.randomChoice(available);
	            
//	            if (i_col==0) {
//	            	beginWith.add(symbol);
//	            }
	            
	            //count the chosen symbol
	            availableInCol[i_col].remove(symbol);
	            availableInRow.remove(symbol);
	            row.add(symbol);
	            i_col++;
	        } else {//collision
	        	colcount++;
	        	
	        	//remove all symbols in row and return to available
	        	while (i_col>0) {
	        		i_col--;
	        		Integer symbol = row.get(i_col);
	        		availableInCol[i_col].add(symbol);
	        		availableInRow.add(symbol);
	        		row.remove(i_col);
	        	}
//	        	
//	        	
//	        	row = new ArrayList<Integer>();
//	            availableInCol = this.copyCollection(initialColsAvailable);
//	            
//	            //return all elements to available in row
//	            availableInRow = new HashSet<Integer>();
//	            for (int j=0; j<n; j++) {
//		    		availableInRow.add(j);
//	            }
//	            i_col = 0;
	            if (colcount%10000==0) {
	            	System.out.println(colcount +" restarts!");
	            }
	        }
	    }
	    System.out.println("Row "+i_row+" finished.");
	    
	    return row;
	}

	public String getMethodName() {
		return "Generation row by row with restarting row.";
	}

}
