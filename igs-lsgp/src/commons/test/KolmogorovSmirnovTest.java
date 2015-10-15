/**
 * Creation date: 10/11/2014
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
package commons.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags 
 */
public class KolmogorovSmirnovTest {

	/**
	 * Given a array of N values, DATA, and given a user-supplied function of a single variable FUNC 
	 * which is cumulative distribution function ranging from 0 (for smallest values of argument) to 1 
	 * (for largest values of its argument), this routine returns the K-S statistic D and the significance 
	 * level PROB. Small values of PROB show that the cumulative distribution function of DATA is Significantly 
	 * different from FUNC. The array DATA is modified by being sorted into ascending order.
	 * @param n
	 */
	public void doTest(HashMap<Integer, Integer> cantidades, int cantExperim, int order) {
		List<Double> data = new ArrayList<Double>();
		
		Iterator<Integer> idx = cantidades.keySet().iterator();
		
		data.add((double)0);// el primer lugar lo relleno con 0 porque no se usa
		
		int cantCLs = 0;
		
		if (order==2)
			cantCLs=2;
		if (order==3)
			cantCLs = 12;
		if (order==4)
			cantCLs = 576;
		if (order==5)
			cantCLs = 161280;
		if (order>=6 || order <2) {
			System.out.println("Order not supported");
			return;
		}
		
		Double suma = 0.0;
		while (idx.hasNext()) {
			Integer cant = (Integer) idx.next();
			suma += ((double)cantidades.get(cant))/cantCLs;
			data.add(suma);
//			Double datai = ((double)cantidades.get(cant))/cantCLs;
//			data.add(datai);			
		}
		
		for (int k=data.size(); k<=cantExperim; k++) {
			data.add((double)0);
		}
		
//		data = this.fakeData();
//		cantExperim = data.size()-1;
		
		//CALL SORT(n,data)     //If the data are already sorted into ascending order, then this call can be omitted.
		Collections.sort(data);
		
//		this.showData(data);
		
		this.ksone(cantExperim, data);
	}

	private void ksone(int cantExperim, List<Double> data) {
		double en=cantExperim;
		double d=0;
		double fo=0;  //data's c.d.f. before the next step.
		for (int j=1; j<=cantExperim; j++) {     //Loop over the sorted data points.
		     double fn=j/en;     //data's c.d.f. after this step.
		     double ff= func(data.get(j));   //Compare to the user-supplied function.
		     double dt=  Math.max(Math.abs(fo-ff),Math.abs(fn-ff));    //Maximum distance.
		     if(dt>d)
		    	 d=dt;
		     fo=fn;
		}
		double prob = probks(Math.sqrt(en)*d);     //Compute significance.
		System.out.println("");
		System.out.println("TEST Kolmogorov-Smirnov concluded with significance: "+prob+" and maximum distance: "+d+".");
		System.out.println("Statistic KS="+d+" must be < 1.36/sqrt("+cantExperim+")="+(1.36/Math.sqrt(cantExperim))+" to accept hypotesis with significance level of 5%.");		
		return;
	}
	private double func(double t) {
		if (t<=0) 
			return 0;
		else if (t<=1) 
				return t;
			else 
				return 1;
	}
	
	private double probks(double alam) {
		double eps1=0.001;
		double eps2=0.00000001;
		double a2=(-2)*(Math.pow(alam, 2));
		double fac=2;
		double probks=0;
		double termbf=0;     //Previous term in sum.
		for (int j=1; j<=100; j++) {
		     double term=fac*Math.exp(a2*Math.pow(j,2));
		     probks=probks+term;
		     if ((Math.abs(term) < (eps1*termbf)) || (Math.abs(term) < eps2*probks)) 
		    	 return probks;
		     fac=-fac;     //Alternating signs in sum.
		     termbf=Math.abs(term);
		}
		probks=1;     //Get here only by failing to converge.
		return probks;
	}
	
	private void showData(List<Double> data) {
		Double suma = 0.0;
		for (int k=0; k<data.size(); k++) {
			System.out.println("Data["+k+"]:"+data.get(k));
			suma +=data.get(k);
		}
		System.out.println("Sum of data: "+suma);
		return;
	}
	
	@Deprecated
	private List<Double> fakeData() {
		List<Double> data = new ArrayList<Double>();
		data.add(0.0);
		data.add(0.05);
		data.add(0.1);
		data.add(0.14);
		data.add(0.20);
		data.add(0.25);
		data.add(0.30);
		data.add(0.35);
		data.add(0.39);
		data.add(0.44);
		data.add(0.50);
		data.add(0.55);
		data.add(0.6);
		data.add(0.64);
		data.add(0.71);
		data.add(0.76);
		data.add(0.80);
		data.add(0.85);
		data.add(0.90);
		data.add(0.95);
		data.add(0.96);
		return data;
	}
}
