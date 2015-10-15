/**
 * Creation date: 11/06/2014
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
package jacomatt.utils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class ArrayUtils {
	
	public static int indexOf(int[] arr, int elem) {
		int i = -1;
		boolean found = false;
		for (int j=0; j<arr.length && !found; j++) {
			found = (arr[j]==elem); //found
			if (found)
				i = j;//save the index
		}
		return i;
	}
	
	public static boolean contains(int[] arr, int elem) {
		return indexOf(arr, elem)!=-1;
	}
	
	public static int indexOfFirstPositiveElem(int[] arr) {
		return indexOfFirstPositiveElemStartingAt(arr, 0);
	}
	
	public static int indexOfFirstNegativeElem(int[] arr, int nullValue) {
		return indexOfFirstNegativeElemStartingAt(arr, 0, nullValue);
	}
	public static int indexOfFirstNegativeElemStartingAt(int[] arr, int index, int nullValue) {
		int i = -1;
		boolean found = false;
		for (int j=index; j<arr.length && !found; j++) {
			found = (arr[j]<0 && arr[j]!=nullValue); //found
			if (found)
				i = j;//save the index
		}
		return i;
	}
	public static int indexOfFirstPositiveElemStartingAt(int[] arr, int index) {
		int i = -1;
		boolean found = false;
		for (int j=index; j<arr.length && !found; j++) {
			found = (arr[j]>=0); //found
			if (found)
				i = j;//save the index
		}
		return i;
	}
	
	public static int indexOfSecondPositiveElem(int[] arr) {
		int first = indexOfFirstPositiveElem(arr);
		int second = indexOfFirstPositiveElemStartingAt(arr, first+1);
		return second;
	}
	
	public static boolean has2Negatives(int[] arr, int nullValue) {
		int i = indexOfFirstNegativeElem(arr, nullValue);
		int j = indexOfFirstNegativeElemStartingAt(arr, i+1, nullValue);
		return (j>=0);
	}
}
