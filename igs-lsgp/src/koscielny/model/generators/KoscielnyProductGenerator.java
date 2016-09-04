/**
 * Creation date: 08/07/2014
 * 
 */

/**
 * © Copyright 2012-2016 Ignacio Gallego Sagastume
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
package koscielny.model.generators;

import java.util.ArrayList;

import seqgen.model.generators.SeqGenWithReplGraph;
import commons.model.latinsquares.ILatinSquare;
import commons.model.latinsquares.PrimitiveIntArrayLatinSquare;
import commons.generators.IRandomLatinSquareGenerator;
import commons.model.OrderedPair;

/**
 *  This class implements a generator based in the concept of product by Koscielny of two LSs.
 *   The product of a LS of order n by a LS of order m returns a LS of order (n*m).
 *  This fact is used by the generator, two speed the generation of a larger LS.
 *  The results are not uniformly distributed, but the method operates in order n^2 (as fast as possible).
 * 
 * 
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class KoscielnyProductGenerator implements IRandomLatinSquareGenerator {

	private int n = 0;
	
	/**
	 * Creates the instance that generates LS of order n
	 * 
	 * @param n
	 */
	public KoscielnyProductGenerator(int n) {
		this.n = n;
	}
	
	/**
	 * Computes the Koscielny product of two LSs in order n^2.
	 * 
	 * @param ls1
	 * @param ls2
	 * @return
	 * @throws Exception
	 */
	protected ILatinSquare product(ILatinSquare ls1, ILatinSquare ls2) throws Exception {
		int n1 = ls1.size();
		int n2 = ls2.size();
		
		ILatinSquare result = new PrimitiveIntArrayLatinSquare(n1*n2);
    
		for (int x=0; x < n1*n2; x++) {
			for (int y=0; y < n1*n2; y++) {
				result.setValueAt(x, y, (n2 * ls1.getValueAt(x/n2,y/n2)) + ls2.getValueAt(x%n2,y%n2));
			}
		}
		return result;
	}

	/**
	 * Computes the factors of n to split the LS of order n in two LSs. Returns the median of the list.
	 * 
	 * @param n
	 * @return OrderedPair
	 */
	private OrderedPair factors(int n) {
	    ArrayList<OrderedPair> list = new ArrayList<OrderedPair>();
	    
	    for (int x=1; x<=n/2; x++) {
	    	if (n%x==0) {
	    		list.add(new OrderedPair(x,n/x));
	        }
	    }
	    return list.get(list.size()/2);//median
	}

	/**
	 *  Returns the LS of order n that results from computing the product of a LS of order n1 and a LS of order n2.
	 *  
	 */
	@Override
	public ILatinSquare generateLS() {
		OrderedPair pair = this.factors(n);
		int n1 = pair.x;
		int n2 = pair.y;

		if (n2==1) {
			//n is prime: cannot use Product to improve
			return new SeqGenWithReplGraph(n).generateLS();
		}

		ILatinSquare ls1 = new SeqGenWithReplGraph(n1).generateLS();
		ILatinSquare ls2 = new SeqGenWithReplGraph(n2).generateLS();

		ILatinSquare ls = null;
		try {
			ls = this.product(ls1, ls2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;
	}
	
	/**
	 * Overrides the method name
	 */
	@Override
	public String getMethodName() {
		return "Koscielny product of two Latin Squares.";
	}

	@Override
	public void setVerbose(boolean show) {
		//to be implemented soon...
	}
}
