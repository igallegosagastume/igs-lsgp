/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.test;

import mckaywormald.model.generators.McKayLRGenerationMethod;

import org.junit.Test;

import commons.model.latinsquares.ILatinRectangle;

/**
 * @author igallego
 *
 */
public class McKayGenerationTest {

	
	
	@Test
	public void test() throws Exception {
//		ILatinRectangle lr = new LatinRectangle(5, 10);
//		
//		ILatinRectangle lr2 = new LatinRectangle(5, 5);
//		
//		System.out.println(lr);
//		
//		System.out.println(lr2);
//		
//		System.out.println(lr.equals(lr2));
//		
//		ILatinSquare ls = new LatinSquare(5);
//		
//		System.out.println(ls);
//		
//		System.out.println(lr2.equals(ls));
		
		long startTime = System.nanoTime();
		
		
		McKayLRGenerationMethod gen = new McKayLRGenerationMethod(10, 256);
		
		ILatinRectangle lr3 = gen.generateLR();
		
		long endTime = System.nanoTime();
		
		System.out.println(lr3);
		
		long duration = endTime - startTime;
		double secs = duration/1000000000d;
		System.out.println("Latin rectangle generated in "+secs+" seconds.");
	}
	
}
