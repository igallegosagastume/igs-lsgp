/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.test;

import jacomatt.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import mckaywormald.model.LatinRectangle;
import commons.OrderedTriple;
import commons.RandomUtils;

/**
 * @author igallego
 *
 */
public class McKayLRGenerationMethod {
	
	//auxiliary variables for the algorithm
	private Set<Integer> initiallyAvInRow = null;
	private HashSet<Integer>[] availableInCol = null;
	
	
	private int conflictsCount = 0;
	private int overlappingConflicts = 0;
	private Integer[] conflictsPerColumn = null; 
	private Set<OrderedTriple> conflicts = null; 
	
	
	public McKayLRGenerationMethod() {
		RandomUtils.initRand();
	}
	
	public LatinRectangle generateLR(int k, int n) {
		
		this.initiallyAvInRow = RandomUtils.oneToN(n);
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
		this.conflictsCount = 0;
		this.overlappingConflicts = 0;
		this.conflictsPerColumn = new Integer[n]; //tells how many conflicts there are for each column
		for (int i=0; i<n; i++) {
			this.conflictsPerColumn[i] = 0;
		}
		this.conflicts = new HashSet<OrderedTriple>();
		
		//from mckay
		int[][] rowHasValue = new int[k][n];
		
		
		this.availableInCol = new HashSet[n];
	    for (int i=0; i<n; i++) {//initially all numbers from 1 to n
	    	this.availableInCol[i] = new HashSet<Integer>();
	    	for (int j=0; j<n; j++) {
	    		this.availableInCol[i].add(j);
	    	}
	    }
	    
		LatinRectangle lr = new LatinRectangle(k, n);
	    
	    for (int i=0; i<k; i++) {
	    	ArrayList<Integer> row = this.generateRow(i, n, lr, rowHasValue);
	    	
	    	lr.setRow(i, row);
	    }
	    return lr;
	}
	        
	private ArrayList<Integer> generateRow(int rowIndex, int n, LatinRectangle ls, int[][] columnOfRowValue) {
	    HashSet<Integer> availableInRow = new HashSet<Integer>();
	    availableInRow.addAll(this.initiallyAvInRow);
	    
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int colIndex = 0;
	    while (colIndex < n) {
	    	//select symbol
	    	Integer symbol = RandomUtils.randomChoice(availableInRow);

	    	//conflict checks
            if (!this.availableInCol[colIndex].contains(symbol)) {
            	this.conflictsPerColumn[colIndex]++;
            	this.conflictsCount++;
            	int i = this.lastRowIndexOf(symbol, ls, rowIndex, colIndex);
            	this.conflicts.add(new OrderedTriple(rowIndex, i, colIndex));
            }

            columnOfRowValue[rowIndex][symbol] = colIndex;
            
            //remove from available
            availableInRow.remove(symbol);
            this.availableInCol[colIndex].remove(symbol);

            //put symbol in result
            row.add(symbol);

            colIndex++;
	    }
	    
	    return row;
	}
	
	private int lastRowIndexOf(int symbol, LatinRectangle ls, int row, int col) {
		int result = -1;
		boolean found = false;
		int i = row-1;
		while (!found && i>=0) {
			found = (ls.getValueAt(i,col)==symbol);
			if (found)
				result = i;
			i--;
		}
		return result;
	}
	        
	public String getMethodName() {
		return "McKay generation of Latin rectangle.";
	}

}
		        