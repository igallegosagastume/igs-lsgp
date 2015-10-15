package commons.test;
/**
 * Creation date: 18/05/2015
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
import java.security.SecureRandom;

public class TestSecureRandom {

	public static void main(String[] args) {
		Long exps = 100000000001L;
		SecureRandom rand = new SecureRandom();
		double sum = 0;
		for (long i=0; i<exps; i++) {
			Integer k = rand.nextInt(256);
			sum = sum + k;
			
			if (i%10000000==0)
				System.out.println(i);
		}
		
		double prom = sum/exps;
		System.out.println("Prom:"+prom);
	}
}
