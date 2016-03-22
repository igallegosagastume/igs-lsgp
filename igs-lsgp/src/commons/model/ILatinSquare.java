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
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public interface ILatinSquare {

	//getters and setters
	public Integer getValueAt(int row, int column);
	public void setValueAt(int row, int column, int value);

	public void setRow(int i, List<Integer> row);
	//utils
	public void writeToFile(String fileName);
	public String toString();
	
	public int size();
	
	
	//equality
	public boolean equals(ILatinSquare ls); 
	public byte[] hashCodeOfStructure();
	public String serializeStructure();
	public boolean equalHash(byte[] dig1, byte[] dig2);
}
