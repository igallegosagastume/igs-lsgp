/**
 * Creation date: 14/03/2016
 * 
 */
package commons.generators;

import commons.ILatinSquare;

/**
 * @author igallego
 *
 */
public interface IRandomLatinRectangleGenerator extends IRandomStructureGenerator {

	public ILatinSquare generateLR(int k, int n);
		
}
