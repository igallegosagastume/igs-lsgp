/**
 * Creation date: 07/06/2014
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

/**
 *  
 *  Utility class that implements ordered pairs.
 * 
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */

public class OrderedPair {
	public Integer x;
	public Integer y;
	
	public OrderedPair(Integer x, Integer y) {
		this.x = x;
		this.y = y;
	}
	
	public String toString() {
		return "("+x+", "+y+")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof OrderedPair)) 
			return false;
		
		OrderedPair p = (OrderedPair)o;
		
		return this.x.intValue()==p.x.intValue() && 
			   this.y.intValue()==p.y.intValue();
	}
	
	@Override
	public int hashCode() {
		return this.x.hashCode()+this.y.hashCode();
	}
}
