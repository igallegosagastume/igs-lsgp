/**
 * Creation date: 12/05/2015
 * 
 */
package basicImpl.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import basicImpl.utils.RandomUtils;

/**
 * @author igallego
 *
 */
public class SimpleGenWithReplGraph extends SimpleGen {

	
	public SimpleGenWithReplGraph(int n) {
		super(n);
	}
	
	
	@Override
	public ArrayList<Integer> generateRow(int i_row, int n, LatinSquare ls, HashSet<Integer>[] availableInCol, Integer[] failedAttemptsPerRow, int[][] collisions) {
		HashSet<Integer> availableInRow = new HashSet<Integer>();
	    for (int j=0; j<n; j++) {
	    	availableInRow.add(j);
	    }
	    
	    HashSet<Integer>[] initialAvailableInCol = new HashSet[n];
	    
	    for (int j=0; j<n; j++) {
	    	initialAvailableInCol[j] = new HashSet<Integer>();
	    	initialAvailableInCol[j].addAll(availableInCol[j]);
	    }
	    
	    //result of this method
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    while (i_col < n) {//when i_col is n, there are n chosen numbers
	        //available is:
	        HashSet<Integer> available = new HashSet<Integer>();
	        available.addAll(availableInCol[i_col]);
	    	available.retainAll(availableInRow);

	        if (!available.isEmpty()) { //if there are available
	            //choose a symbol at random
	            Integer symbol = RandomUtils.randomChoice(available);
	            //count the chosen symbol
	            availableInCol[i_col].remove(symbol);
	            availableInRow.remove(symbol);
	            row.add(symbol);
	            i_col++;
	        } else {//collision
	        	HashMap<Integer, HashSet<Integer>> map = this.constructReplGraph(row, i_col, initialAvailableInCol, availableInCol);
	        	
	        	this.fixRowWithGraph(map, row, i_col, availableInCol, availableInRow);
	        }
	    }
	    
	    return row;
	}


	protected HashMap<Integer, HashSet<Integer>> constructReplGraph(ArrayList<Integer> row, 
																	int col, 
																	HashSet<Integer>[] initialAvailInCol, 
																	HashSet<Integer>[] availInCol) {
		
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		
		for(int j=col-1; j>=0; j--) {
			int elem = row.get(j);
			HashSet<Integer> set = new HashSet<Integer>();
			set.addAll(initialAvailInCol[j]);
//			set.removeAll(row);
			set.remove(elem);
			if (set.size()>0)
				map.put(elem, set);
		}
		
		return map;
	}
	
	protected void fixRowWithGraph(HashMap<Integer, HashSet<Integer>> map, ArrayList<Integer> row, int col, HashSet<Integer>[] availInCol, HashSet<Integer> availableInRow) {
		boolean finished = false;
		int elemToFind = RandomUtils.randomChoice(availInCol[col]);
		int index;
		int nextIndex = row.indexOf(elemToFind);
		
		while (!finished) {
			index = nextIndex;
			int newElem = RandomUtils.randomChoice(map.get(elemToFind));
			
			nextIndex = row.indexOf(newElem);
			
			//before replacement
			elemToFind = row.get(index);
			
			//replace 
			row.set(index, newElem);
			
			//return to available
			availableInRow.add(elemToFind);
			availInCol[index].add(elemToFind);
			
			//substract from available
			availableInRow.remove(newElem);
			availInCol[index].remove(newElem);
			
			finished = (map.get(elemToFind)==null);
		}
	}
}
