/**
 * Creation date: 23/09/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2015 Ignacio Gallego Sagastume
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

import basicImpl.model.latinsquares.ArrayListLatinSquare;
import commons.RandomUtils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public class SimpleGenWithRandomSwapping extends SimpleGenWithBacktracking {

	protected HashSet<Integer> symbols = null;
	
	public SimpleGenWithRandomSwapping(int n) {
		super(n);
		this.symbols = RandomUtils.oneToN(n);
	}
	
	@Override
	protected ArrayList<Integer> generateRow(int i_row, int n, ArrayListLatinSquare ls, Set<Integer>[] availableInCol, Integer[] failedAttemptsPerRow, int[][] collisions) {
	    Set<Integer> availableInRow = new HashSet<Integer>();
	    
	    //inicialmente los elementos de 0 a n-1
	    for (int j=0; j<n; j++) {
	    		availableInRow.add(j);
	    }
	    
	    //resultado del algoritmo
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    Set<Integer> columnsWithRepetitions = new HashSet<Integer>();
	    
	    while (i_col < n) {//cuando llega a n, elegi n numeros
	        //conjunto de disponibles es:
	        Set<Integer> available = new HashSet<Integer>();
	        available.addAll(availableInCol[i_col]);
	    	available.retainAll(availableInRow);
	    	
	        if (!available.isEmpty()) { //si me quedan disponibles
	            //saco un simbolo al azar:
	            Integer symbol = RandomUtils.randomChoice(available);
	            //System.out.print("Saco simbolo:"+repr(simbolo)+" de available: "+repr(available))
	            
	            //contabilizo el elegido
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
//	            repetitionsInCol[i_col].add(symbol);

	            columnsWithRepetitions.add(i_col);
	            
	            i_col++;
	        }
	    }
	    
	    if (columnsWithRepetitions.size()>0) {
	    	this.fixRow(i_row, n, ls, row, columnsWithRepetitions, availableInCol);
	    } else {
	    	//System.out.println("Row "+(i_row)+" generated...");
	    }
	    return row;
	}

	public void fixRow(int i_row, int n, ArrayListLatinSquare ls, List<Integer> row, Set<Integer> columnsWithRepetitions, Set<Integer>[] availableInCol) {
		int columnCountBeforeSwap, columnCountAfterSwap;
		Integer lastCol1 = null, lastCol2 = null;
		do {
			//swap an element with repetitions in its column
			Integer columnWRep = RandomUtils.randomChoice(columnsWithRepetitions);
			Integer anotherCol = 0;
			
			//create a bag with all symbols, except the column with repetitions
			Set<Integer> bag = new HashSet<Integer>();
			bag.addAll(symbols);
			bag.remove(columnWRep);
			
			//if the last column was columnWRep, avoid trying the same path again
			if (lastCol1==columnWRep) 
				bag.remove(lastCol2);
			if (lastCol2==columnWRep) 
				bag.remove(lastCol1);
			
			anotherCol = RandomUtils.randomChoice(bag);//take another column for swap, but not the same
	
			columnCountBeforeSwap = columnsWithRepetitions.size();
			
			//swap the two columns
			this.swap(columnWRep, anotherCol, ls, row, columnsWithRepetitions, availableInCol);
			
			columnCountAfterSwap = columnsWithRepetitions.size();
			
			if (columnCountAfterSwap>columnCountBeforeSwap) {//if not better, un-swap (== case must let swap, if not can loop forever)
				this.swap(columnWRep, anotherCol, ls, row, columnsWithRepetitions, availableInCol);
			}
			
			//In any case (successful or not), save the last swap to avoid repeating paths 
			lastCol1 = columnWRep;
			lastCol2 = anotherCol;
//			System.out.println(columnCountAfterSwap);
	    } while (columnCountAfterSwap>0);
	}
	

	//swap in constant time
	private void swap(Integer columnWRep, Integer anotherCol,  ArrayListLatinSquare ls, List<Integer> row, Set<Integer> columnsWithRepetitions, Set<Integer>[] availableInCol) {
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
	
	public String getMethodName() {
		return "Generation row by row with random swapping.";
	}
	
}
