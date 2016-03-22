/**
 * Creation date: 14/03/2016
 * 
 */
package commons.generators;

import commons.model.ILatinSquare;


/**
 * @author igallego
 *
 */
public interface IRandomLatinSquareGenerator extends IRandomStructureGenerator {

	public ILatinSquare generateLS();
	
}
