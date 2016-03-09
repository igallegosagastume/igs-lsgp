/**
 * Creation date: 09/03/2016
 * 
 */
package mckaywormald.test;

import mckaywormald.model.LatinRectangle;

import org.junit.Test;

import basicImpl.model.LatinSquare;

import commons.ILatinRectangle;
import commons.ILatinSquare;

/**
 * @author igallego
 *
 */
public class McKayGenerationTest {

	
	
	@Test
	public void test() throws Exception {
		ILatinRectangle lr = new LatinRectangle(5, 10);
		
		ILatinRectangle lr2 = new LatinRectangle(5, 5);
		
		System.out.println(lr);
		
		System.out.println(lr2);
		
		System.out.println(lr.equals(lr2));
		
		ILatinSquare ls = new LatinSquare(5);
		
		System.out.println(ls);
		
		System.out.println(lr2.equals(ls));
		
		McKayLRGenerationMethod gen = new McKayLRGenerationMethod();
		
		LatinRectangle lr3 = gen.generateLR(3, 5);
		
		System.out.println(lr3);
	}
	
}
