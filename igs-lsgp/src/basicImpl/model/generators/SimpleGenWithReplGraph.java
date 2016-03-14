/**
 * Creation date: 12/05/2015
 * 
 */

/**
 * � Copyright 2012-2015 Ignacio Gallego Sagastume
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
package basicImpl.model.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import commons.ILatinSquare;
import commons.RandomUtils;

/**
 * @author igallego
 *
 */
public class SimpleGenWithReplGraph extends SimpleGenWithBacktracking {

	public SimpleGenWithReplGraph(int n) {
		super(n);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Integer> generateRow(int i_row, int n, ILatinSquare ls, Set<Integer>[] availableInCol, Integer[] failedAttemptsPerRow, int[][] collisions) {
		Set<Integer> availableInRow = new HashSet<Integer>();
	    for (int j=0; j<n; j++) {
	    	availableInRow.add(j);
	    }
	    
	    Set<Integer>[] initialAvailableInCol = new HashSet[n];
	    
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
	    	
	    	/*List<Integer> available = new ArrayList<Integer>();
	    	available.addAll(availableInCol[i_col]);
	    	available.retainAll(availableInRow);*/

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
	        	int elem = RandomUtils.randomChoice(availableInCol[i_col]);
	        	this.makeElemAvailable(elem, map, row, i_col, availableInCol, availableInRow);
	        }
	    }
//	    System.out.println("Row "+i_row);//: "+row+".");
	    return row;
	}


	public HashMap<Integer, HashSet<Integer>> constructReplGraph(ArrayList<Integer> row, 
																 Integer col, 
																 Set<Integer>[] initialAvailInCol, 
																 Set<Integer>[] availInCol) {
		
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		
		for(int j=col; j>=0; j--) {

			HashSet<Integer> set = new HashSet<Integer>();
			set.addAll(initialAvailInCol[j]);

			if (set.size()>0)
				map.put(j, set);//the element in position j could potentially be changed for one in the set
		}

		return map;
	}
	
	public void makeElemAvailable(Integer old, HashMap<Integer, HashSet<Integer>> map, 
								  ArrayList<Integer> row, Integer col, 
								  Set<Integer>[] availInCol, 
								  Set<Integer> availableInRow) {
		boolean finished = false;
		
		int firstElem = new Integer(old);
		
		this.eraseFirstElemFromGraph(map, firstElem);
	
		int idx_old = row.indexOf(old);
		int idx_new;

		int i=0;
		
		HashSet<Integer> path = new HashSet<Integer>();

		while (!finished) {
//			if (idx_old==-1) {//there are no repetitions, but the element is still in the row
//				idx_old = row.indexOf(firstElem);
//				old = firstElem;
//			}
			
			Set<Integer> avail = new HashSet<Integer>();
			avail.addAll(map.get(idx_old));
			avail.removeAll(path);
			
			if (avail.isEmpty()) {
				//Path no good, begin again
				path = new HashSet<Integer>();
				avail.addAll(map.get(idx_old));
			}
			
			Integer newElem = RandomUtils.randomChoice(avail);
			idx_new = row.indexOf(newElem);//index of this elem before replacement because it will be repeated
						
			//replace 
			row.set(idx_old, newElem);
		
			//store in path 
			path.add(newElem);
			
			if (row.indexOf(old)==-1) //if the old element is not in the row
				availableInRow.add(old);
			availableInRow.remove(newElem);

			//if (availInCol[idx_old].indexOf(old)==-1)//check to avoid repetition
			availInCol[idx_old].add(old);
			availInCol[idx_old].remove(newElem);
			
			finished = (availableInRow.contains(firstElem) && idx_new==-1);// || (path.size()==n);//the element is now available in row and there are no repetitions
			
			idx_old = idx_new;
			old = newElem;
			
			i++;
			if (i%100000==0) {
				System.out.println(i);
				if (i%1000000==0) {
					System.out.println("Is this an infinite loop?");
					System.out.println(firstElem);
					System.out.println(idx_old);
					System.out.println(map);
					System.out.println(row);
				}
			}
		}
	}


	@Override
	public String getMethodName() {
		return "Generation row by row with replacement graph.";
	}
	

	private void eraseFirstElemFromGraph(HashMap<Integer, HashSet<Integer>> map, Integer firstElem) {
		Iterator<Integer> iter = map.keySet().iterator();
		
		while (iter.hasNext()) {
			Integer elem = iter.next();
			HashSet<Integer> set = map.get(elem);
			set.remove(firstElem);
			map.put(elem, set);
		}
	}
}
