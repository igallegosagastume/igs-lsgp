/**
 * Creation date: 23/09/2014
 * 
 */

/**
 * © Copyright 2012-2016 Ignacio Gallego Sagastume
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import commons.utils.RandomUtils;

/**
 *  This class implements an efficient method for the generation of Random Latin Squares.
 *   While the current row has conflicts, the method makes swapping between elements 
 *  until the current row is free of conflicts.
 * 
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public class SimpleGenWithRandomSwapping extends AbstractSimpleGenerator {

	/**
	 * Constructs the generator of LSs of order n.
	 * 
	 * @param n
	 */
	public SimpleGenWithRandomSwapping(int n) {
		super(n);
	}
	
	/**
	 * Overrides the method for the generation of one row. First, it generates the complete row, and then correct the conflicts.
	 * 
	 */
	@Override
	protected ArrayList<Integer> generateRow(int i_row) {
	    Set<Integer> availableInRow = new HashSet<Integer>(this.symbols);
	    
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    Set<Integer> columnsWithRepetitions = new HashSet<Integer>();
	    
	    while (i_col < n) {//when i_col==n, there are n chosen numbers
	        //available set is:
	        Set<Integer> available = new HashSet<Integer>(availableInCol[i_col]);
	    	available.retainAll(availableInRow);
	    	
	        if (!available.isEmpty()) { //if there are available
	            //choose a symbol at random
	            Integer symbol = RandomUtils.randomChoice(available);
	            
	            //count the chosen symbol
	            availableInCol[i_col].remove(symbol);
	            availableInRow.remove(symbol);
	            row.add(symbol);
	            i_col++;
	        } else {
	            //collision
	        	Integer symbol = RandomUtils.randomChoice(availableInRow);

	            availableInCol[i_col].remove(symbol);
	            availableInRow.remove(symbol);
	            row.add(symbol);

	            columnsWithRepetitions.add(i_col);
	            
	            i_col++;
	        }
	    }
	    
	    if (columnsWithRepetitions.size()>0) {
	    	this.fixRow(i_row, row, columnsWithRepetitions);
	    } else {
	    }
	    return row;
	}

	/**
	 * Auxiliary method to fix the row with conflicts.
	 * 
	 * @param i_row
	 * @param row
	 * @param columnsWithRepetitions
	 */
	protected void fixRow(int i_row, List<Integer> row, Set<Integer> columnsWithRepetitions) {
		int columnCountBeforeSwap, columnCountAfterSwap;
		Integer lastCol1 = null, lastCol2 = null;
		do {
			//swap an element with repetitions in its column
			Integer columnWRep = RandomUtils.randomChoice(columnsWithRepetitions);
			Integer anotherCol = 0;
			
			//create a bag with all symbols, except the column with repetitions
			Set<Integer> bag = new HashSet<Integer>(this.symbols);
			bag.remove(columnWRep);
			
			//if the last column was columnWRep, avoid trying the same path again
			if (lastCol1==columnWRep) 
				bag.remove(lastCol2);
			if (lastCol2==columnWRep) 
				bag.remove(lastCol1);
			
			anotherCol = RandomUtils.randomChoice(bag);//take another column for swap, but not the same
	
			columnCountBeforeSwap = columnsWithRepetitions.size();
			
			//swap the two columns
			this.swap(columnWRep, anotherCol, row, columnsWithRepetitions);
			
			columnCountAfterSwap = columnsWithRepetitions.size();
			
			if (columnCountAfterSwap>columnCountBeforeSwap) {//if not better, un-swap (== case must let swap, if not can loop forever)
				this.swap(columnWRep, anotherCol, row, columnsWithRepetitions);
			}
			
			//In any case (successful or not), save the last swap to avoid repeating paths 
			lastCol1 = columnWRep;
			lastCol2 = anotherCol;
//			System.out.println(columnCountAfterSwap);
	    } while (columnCountAfterSwap>0);
	}
	

	/**
	 * Swap two elements in constant time.
	 * 
	 * @param columnWRep
	 * @param anotherCol
	 * @param row
	 * @param columnsWithRepetitions
	 */
	protected void swap(Integer columnWRep, Integer anotherCol, List<Integer> row, Set<Integer> columnsWithRepetitions) {
		Integer elem1 = row.get(columnWRep);
		Integer elem2 = row.get(anotherCol);
		
		row.set(columnWRep, elem2);
		row.set(anotherCol, elem1);
		
		boolean deleted2 = availableInCol[columnWRep].remove(elem2);
    	//availableInCol[columnWRep].add(elem1); element is repeated so it is not available
		if (deleted2) {//si el elemento estaba disponible
			columnsWithRepetitions.remove(columnWRep);//now the column has no repetitions 
		}
    	boolean deleted1 = availableInCol[anotherCol].remove(elem1);
    	
    	if (!columnsWithRepetitions.contains(anotherCol)) {//if the element was NOT repeated
    		//if (!availableInCol[anotherCol].contains(elem2))
    			availableInCol[anotherCol].add(elem2);
    	}
    	
    	if (deleted1) {	//if the element was available
    		columnsWithRepetitions.remove(anotherCol);//now the column has no repetitions
		} else {
			//if (!columnsWithRepetitions.contains(anotherCol)) 
				columnsWithRepetitions.add(anotherCol);
		}

	}

	/**
	 * Overrides to get the method's name.
	 * 
	 */
	@Override
	public String getMethodName() {
		return "Generation row by row with random swapping.";
	}
	
}
