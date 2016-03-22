/**
 * Creation date: 14/03/2016
 * 
 */
package commons.generators;

import commons.model.ILatinSquare;


/**
 *  This interface lists the methods that LS generators must implement.
 * @author igallego
 *
 */
public interface IRandomLatinSquareGenerator extends IRandomStructureGenerator {

	public ILatinSquare generateLS();
	
}