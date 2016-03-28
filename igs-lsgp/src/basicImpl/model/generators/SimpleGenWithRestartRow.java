/**
 * Creation date: 08/05/2015
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
import java.util.Set;

import commons.utils.RandomUtils;

/**
 * This method does not deal with conflicts: when one occurs, it simply erases the whole row and begin generating it again.
 *  For small orders (n<=20) it is very efficient, but for larger orders (n>=30) is very unlikely to finish. 
 * 
 * 
 * @author igallego
 *
 */
public class SimpleGenWithRestartRow extends AbstractSimpleGenerator {
	
	
	public SimpleGenWithRestartRow(int n) {
		super(n);
	}
	
	
	/**
	 * Generates the row i. When a conflict is found, restart generating the row again.
	 */
	@Override
	public ArrayList<Integer> generateRow(int i_row) {
		HashSet<Integer> availableInRow = new HashSet<Integer>(this.symbols);//initially all symbols
	    
	    int colcount = 0;
	    
	    //result of this method
	    ArrayList<Integer> row = new ArrayList<Integer>();
	    int i_col = 0;
	    
	    while (i_col < n) {//when i_col is n, there are n chosen numbers
	        //available is:
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

	            if (colcount%10000==0) {
	            	System.out.println(colcount +" restarts!");
	            }
	        }
	    }
	    System.out.println("Row "+i_row+" finished.");
	    
	    return row;
	}

	@Override
	public String getMethodName() {
		return "Generation row by row with restarting row.";
	}

}
