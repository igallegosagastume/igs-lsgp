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
package commons.model;

import java.util.List;


/**
 *  All Latin square classes must know how to access and set its elements,
 *  how to write to file, print to String and compare.
 *  
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public interface ILatinSquare {

	/**
	 * Gets the value at specified row and column indexes.
	 */
	public Integer getValueAt(int row, int column);
	
	/**
	 * Sets the value at specified row and column indexes.
	 */
	public void setValueAt(int row, int column, int value);

	/**
	 * Sets an entire row
	 */
	public void setRow(int i, List<Integer> row);
	
	/**
	 * A LS implementation must know how to write to file.
	 * 
	 * @param fileName
	 */
	public void writeToFile(String fileName);
	
	/**
	 * A LS implementation must know how to print an instance.
	 * 
	 */
	public String toString();
	
	/**
	 * The order of the LS.
	 * 
	 * @return
	 */
	public int size();
	
	/**
	 *  Check if the structure has repetitions in some row or column.
	 *  
	 */
	public boolean preservesLatinProperty();
	
	/**
	 * A LS implementation must know how to compare with other LSs.
	 * 
	 */
	public boolean equals(ILatinSquare ls); 
	
	/**
	 *  Computes a hash of the LS.
	 * 
	 */
	public byte[] hashCodeOfStructure();
	
	/**
	 *  Prints the LS into a String without new line or space characters.
	 * @return
	 */
	public String serializeStructure();
	
	/**
	 *  Tells if two hashes are the same.
	 *  
	 * @param dig1
	 * @param dig2
	 * @return
	 */
	public boolean equalHash(byte[] dig1, byte[] dig2);
}
