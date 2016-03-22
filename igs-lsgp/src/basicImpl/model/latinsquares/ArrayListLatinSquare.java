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
package basicImpl.model.latinsquares;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the default implementation of AbstractLatinSquare class. The decision choice for the data structure is to make the operation
 *  setRow() efficient.
 * 
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class ArrayListLatinSquare extends AbstractLatinSquare {

	protected ArrayList<Integer>[] ls;
	
	@SuppressWarnings("unchecked")
	public ArrayListLatinSquare(int n) {
		super(n);
		
		this.ls = new ArrayList[n];
		 
		for (int i=0; i<n; i++) {
			ls[i] = new ArrayList<Integer>(n);
			for (int j=0; j<n; j++) {
				ls[i].add(0); //add initial n 0s
			}
		}
	}

	@Override
	public Integer getValueAt(int row, int col) {
		return ls[row].get(col);
	}

	@Override
	public void setValueAt(int row, int col, int value) {
		ls[row].set(col, value);
	}

	/**
	 * This method is the reason for the ls structure. It's efficient to set a complete row.
	 * 
	 * @param i
	 * @param row
	 */
	@Override
	public void setRow(int i, List<Integer> row) {
		ls[i] = (ArrayList<Integer>)row;
	}
}
