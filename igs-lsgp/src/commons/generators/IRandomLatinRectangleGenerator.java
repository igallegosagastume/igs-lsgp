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

	/**
	 * The result of this method must be a random Latin Rectangle.
	 * 
	 * @return
	 */
	public ILatinRectangle generateLR();
		
}
