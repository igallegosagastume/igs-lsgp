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
//	        	System.out.println(ls);
//	        	System.out.println("Map:"+map);
//	        	System.out.println("Avail:"+availableInCol);
//	        	System.out.println("Row:"+row);
	        	this.makeElemAvailable(map, row, i_col, availableInCol, availableInRow);
	        }
	    }
	    
	    return row;
	}


	public HashMap<Integer, HashSet<Integer>> constructReplGraph(ArrayList<Integer> row, 
																	Integer col, 
																	HashSet<Integer>[] initialAvailInCol, 
																	HashSet<Integer>[] availInCol) {
		
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		
		for(int j=col; j>=0; j--) {
//			int elem = row.get(j);
			HashSet<Integer> set = new HashSet<Integer>();
			set.addAll(initialAvailInCol[j]);
//			set.remove(elem);//the initial element in the row is removed from set
			
			if (set.size()>0)
				map.put(j, set);//the element in position j could potentially be changed for one in the set
		}
		
		return map;
	}
	
	public void makeElemAvailable(HashMap<Integer, HashSet<Integer>> map, ArrayList<Integer> row, Integer col, HashSet<Integer>[] availInCol, HashSet<Integer> availableInRow) {
		boolean finished = false;

		int old = RandomUtils.randomChoice(availInCol[col]);
		
		int firstElem = new Integer(old);
//		System.out.println(firstElem);
		
		int idx_old = row.indexOf(old);
		int idx_new;
		
		
		while (!finished) {
			
			int newElem;
			if (idx_old==-1) {//there are no repetitions, but the element is not yet available
				idx_old = row.indexOf(firstElem);
				old = firstElem;
			}
			
			newElem = RandomUtils.randomChoice(map.get(idx_old));
			idx_new = row.indexOf(newElem);//index of this elem before replacement because it will be repeated
						
			//replace 
			row.set(idx_old, newElem);
			
			if (row.indexOf(old)==-1) //if the old element is not in the row 
				availableInRow.add(old);
			availableInRow.remove(newElem);

			availInCol[idx_old].add(old);
			availInCol[idx_old].remove(newElem);
			
			finished = (availableInRow.contains(firstElem) && idx_new==-1);//the element is now available in row and there are no repetitions
			
			idx_old = idx_new;
			old = newElem;
		}
	}


	@Override
	public String getMethodName() {
		return "Generation row by row with replacement graph.";
	}
	
	
}
