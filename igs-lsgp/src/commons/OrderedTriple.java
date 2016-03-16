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
package commons;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class OrderedTriple {
	public int x;
	public int y;
	public int z;
	
	public OrderedTriple(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}

	@Override
	public boolean equals(Object obj) {
		OrderedTriple t=null;
		try {
			t = (OrderedTriple)obj;
		} catch (Exception e) {
			return false;
		}
		if (t==null) return false;
		else
			return (this.x==t.x &&
					this.y==t.y &&
					this.z==t.z);
					
	}

	@Override
	public int hashCode() {
		return (new Integer(x).hashCode())+
			   (new Integer(y).hashCode())+
			   (new Integer(z).hashCode());
	}
}