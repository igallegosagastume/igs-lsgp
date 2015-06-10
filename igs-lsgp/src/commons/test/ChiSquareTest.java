/**
 * Creation date: 01/12/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */
package commons.test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags 
 */
public class ChiSquareTest {

	
	
	public void doTest(HashMap<Integer, Integer> counts, int cantExperim, int order) {
		
		int border1 = cantExperim/10;
		
		int[] expected = new int[] {border1, border1, border1, border1, border1, border1, border1, border1, border1, border1 };
		
		int[] observed = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		
		Iterator<Integer> idx = counts.keySet().iterator();
		
		int i = 0; 
		Long cantCLs=null;
		
		if (order==2)
			cantCLs= 2L;
		if (order==3)
			cantCLs = 12L;
		if (order==4)
			cantCLs = 576L;
		if (order==5)
			cantCLs = 161280L;
		if (order==6)
			cantCLs = 812851200L;
		if (order==7)
			cantCLs = 61479419904000L;
//		if (order==8)
//			cantCLs = new BigDecimal(108776032459082956800L);//long too long... :/
		if (order>=8 || order <2) {
			System.out.println("Order not supported");
			return;
		}
		
		
		double border = (double)cantCLs/10.0;
		
		while(idx.hasNext()) {
			i = idx.next();
			
			if (i < border) {
				observed[0] += counts.get(i); 
				
			} else if ((border <= i) && (i < (2*border))) {
				observed[1] += counts.get(i);
				
			} else if (((2*border) <= i) && (i < (3*border))) {
				observed[2] += counts.get(i);
				
			} else if (((3*border) <= i) && (i < (4*border))) {
				observed[3] += counts.get(i);
				
			} else if (((4*border) <= i) && (i < (5*border))) {
				observed[4] += counts.get(i);
				
			} else if (((5*border) <= i) && (i < (6*border))) {
				observed[5] += counts.get(i);
				
			} else if (((6*border) <= i) && (i < (7*border))) {
				observed[6] += counts.get(i);
				
			} else if (((7*border) <= i) && (i < (8*border))) {
				observed[7] += counts.get(i);
				
			} else if (((8*border) <= i) && (i < (9*border))) {
				observed[8] += counts.get(i);
				
			} else if (((9*border) <= i) && (i <= cantCLs)) {
				observed[9] += counts.get(i);
				
			}
		}
		System.out.println("");
		System.out.println("Observed[i]=");
		for (int k=0; k<10; k++) {
			System.out.print(observed[k]+",");
		}
		
		double chi = this.chiSquare(expected, observed);
		System.out.println("");
		System.out.println("Chi-Square statistic: "+chi+". Must be < 29.67 for the data to be uniformly distributed (false positive in 0.005% of cases).");
//		System.out.println("");
		System.out.println("Total count of LSs generated: "+cantExperim);
		System.out.println("Different LSs generated: "+counts.size()+" over a total of "+cantCLs+" possible.");
	}
	
	
    /**
     * Computes Chi-Square statistic given observed and expected counts
     * @param observed array of observed frequency counts
     * @param expected array of expected frequency counts
     */
    private double chiSquare(int[] expected, int[] observed) {
        double sumSq = 0.0d;
        double dev = 0.0d;
        double n = observed.length;
        for (int i = 0; i< n; i++) {
            dev = (double)(observed[i] - ( expected[i]));
            sumSq += (dev*dev)/( (double)expected[i]);
        }
        return sumSq;
    } 
}
