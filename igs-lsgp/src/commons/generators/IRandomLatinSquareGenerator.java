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

	/**
	 * The result of this method must be a random LS.
	 * 
	 * @return
	 */
	public ILatinSquare generateLS();
	
}