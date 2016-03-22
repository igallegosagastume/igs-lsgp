/**
 * Creation date: 14/03/2016
 * 
 */
package commons.generators;

import commons.model.ILatinRectangle;

/**
 *  This interface lists the methods that LR generators must implement.
 *   
 * @author igallego
 *
 */
public interface IRandomLatinRectangleGenerator extends IRandomStructureGenerator {

	public ILatinRectangle generateLR();
		
}
