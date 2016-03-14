/**
 * Creation date: 08/07/2014
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

import commons.RandomUtils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class SimpleGenWithBacktracking extends AbstractSimpleGenerator {

	public SimpleGenWithBacktracking(int n) {
		super(n);
	}

	@SuppressWarnings("unchecked")
	protected List<Integer> generateRow(int i_row) {
	    HashSet<Integer> availableInRow = new HashSet<Integer>();//    = [ i for i in range(1,n+1) ]
	    
	    availableInRow.addAll(symbols);
	    
	    //resultado del algoritmo
	    List<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    //intentos fallidos por cada columna en la row actual
	    HashSet<Integer>[] failedAttemptsInCol = new HashSet[n];// = [[] for i in range(1,n+1)]
	    for (int i=0; i<n; i++) {
	    	failedAttemptsInCol[i] = new HashSet<Integer>();
	    }
	    int failedInRowCount = 0;
	    while (i_col < n) {//when i_col==n, row is complete
	        //available is:
	        Set<Integer> available = new HashSet<Integer>();
	        available.addAll(availableInCol[i_col]);
	    	available.retainAll(availableInRow);
	    	available.removeAll(failedAttemptsInCol[i_col]);
	    	
	        if (!available.isEmpty()) { //si me quedan disponibles
	            //choose random symbol
	            Integer symbol = RandomUtils.randomChoice(available);
	            
	            //count chosen symbol
	            availableInCol[i_col].remove(symbol);
	            i_col = i_col + 1;
	            availableInRow.remove(symbol);
	            row.add(symbol);
	        } else {
	            failedInRowCount = failedInRowCount + 1;

	            collisions[i_row][i_col] = collisions[i_row][i_col] + 1;
	            
	            //clean failed attempts from rightest columns
	            for (int i=i_col; i<n; i++) {
	            	failedAttemptsInCol[i] = new HashSet<Integer>();
	            }

	            //backtracking
	            i_col = i_col - 1;
	            //extract last symbol
	            Integer symbol = row.get(row.size()-1);
	            row.remove(row.size()-1);

	            //save failed attempt
	            failedAttemptsInCol[i_col].add(symbol);

	            //put again in available in row and column
	            availableInRow.add(symbol);
	            availableInCol[i_col].add(symbol);            
	        }
	    }
	    failedAttemptsPerRow[i_row] = failedInRowCount;
	    return row;
	}
	        
	@Override
	public String getMethodName() {
		return "Generation row by row with backtracking.";
	}

}
	        