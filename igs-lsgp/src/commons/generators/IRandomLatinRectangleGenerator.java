/**
 * Creation date: 14/03/2016
 * 
 */
package commons.generators;

import commons.model.ILatinRectangle;

/**
 * @author igallego
 *
 */
public interface IRandomLatinRectangleGenerator extends IRandomStructureGenerator {

	public ILatinRectangle generateLR();
		
}
