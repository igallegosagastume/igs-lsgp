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
																	Integer col, 
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
	
	protected void fixRowWithGraph(HashMap<Integer, HashSet<Integer>> map, ArrayList<Integer> row, Integer col, HashSet<Integer>[] availInCol, HashSet<Integer> availableInRow) {
		boolean finished = false;
//		HashSet<Integer> avail = new HashSet<Integer>();
//		avail.addAll(availInCol[col]);
//		avail.addAll(availableInRow);
		int old = RandomUtils.randomChoice(availInCol[col]);
		int idx_old = row.indexOf(old);
		int idx_new;
		
		
		while (!finished) {
			
			int newElem = RandomUtils.randomChoice(map.get(old));
			idx_new = row.indexOf(newElem);//index of this elem before replacement
						
			//replace 
			row.set(idx_old, newElem);
			
			//return to available
			if (row.indexOf(old)==-1)
				availableInRow.add(old);
			if (!availInCol[idx_old].contains(old))
				availInCol[idx_old].add(old);
			
			//subtract from available
			availableInRow.remove(newElem);//at most does not erase
			availInCol[idx_old].remove(newElem);
			
			finished = (map.get(newElem)==null || idx_new==-1);//there's not another element in graph
			
			idx_old = idx_new;
			old = newElem;
		}
	}
}
