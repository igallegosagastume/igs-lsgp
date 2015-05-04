/**
 * Creation date: 23/09/2014
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

import java.security.SecureRandom;
import java.util.HashSet;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags 
 */
public class RandomUtils {
	private static SecureRandom rand;
	
	
	public static void initRand() {
		try {
			rand = new SecureRandom();
		} catch (Exception e) {
			rand = null;				
		}
	}

	public static Integer randomChoice(HashSet<Integer> set) {
		int size = set.size();
		
		//For cryptography, the Random object should be truly Random
		int item = rand.nextInt(size);
		int i = 0;
		for(Integer obj : set)
		{
		    if (i == item)
		        return obj;
		    i = i + 1;
		}
		return null;
	}

	public static HashSet<Integer> oneToN(int n) {
		HashSet<Integer> numbers = new HashSet<Integer>();
		
		for (int i=0; i<n; i++) {
			numbers.add(i);
		}
		return numbers;
	}
}
