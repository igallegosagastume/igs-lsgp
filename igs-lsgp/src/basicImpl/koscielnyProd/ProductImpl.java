/**
 * Creation date: 08/07/2014
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
package basicImpl.koscielnyProd;

import java.util.ArrayList;

import basicImpl.model.SimpleGen;
import basicImpl.model.LatinSquare;
import basicImpl.utils.OrderedPair;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class ProductImpl {

	private int n = 0;
	public ProductImpl(int n) {
		this.n = n;
	}
	
	protected LatinSquare product(LatinSquare ls1, LatinSquare ls2) throws Exception {
		int n1 = ls1.size();
		int n2 = ls2.size();
		
		LatinSquare result = new LatinSquare(n1*n2);
    
		for (int x=0; x < n1*n2; x++) {
			for (int y=0; y < n1*n2; y++) {
				result.setValueAt(x, y, (n2 * (ls1.getValueAt(x/n2,y/n2)) + ls2.getValueAt(x%n2,y%n2)));
			}
		}
		return result;
	}


	private OrderedPair factors(int n) {
	    ArrayList<OrderedPair> list = new ArrayList<OrderedPair>();
	    
	    for (int x=1; x<=n/2; x++) {
	    	if (n%x==0) {
	    		list.add(new OrderedPair(x,n/x));
	        }
	    }
	    return list.get(list.size()/2);//median
	}

	public  LatinSquare genLSMult() throws Exception {
		OrderedPair pair = this.factors(n);
		int n1 = pair.x;
		int n2 = pair.y;

		if (n2==1) {
			//n is prime: cannot use Product to improve
			return new SimpleGen(n).genLS();//#genLS.genLS(n)
		}

		//print("generacion de LS("+repr(n)+") usando mult de N1="+repr(n1)+" * N2="+repr(n2))
		LatinSquare ls1 = new SimpleGen(n1).genLS();
		LatinSquare ls2 = new SimpleGen(n2).genLS();

		//genLS.mostrarLS(ls1)
		//genLS.mostrarLS(ls2)
		LatinSquare ls = this.product(ls1, ls2);
		//write.writeMatrixToFile(ls, 'producto.txt')
		return ls;
	}
}
