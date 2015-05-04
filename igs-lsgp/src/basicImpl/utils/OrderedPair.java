/**
 * Creation date: 07/06/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2014 Ignacio Gallego Sagastume
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
package basicImpl.utils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public class OrderedPair {
	public int x;
	public int y;
	
	public OrderedPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "("+x+", "+y+")";
	}
}
