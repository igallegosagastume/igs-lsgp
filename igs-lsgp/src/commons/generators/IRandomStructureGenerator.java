/**
 * Creation date: 14/03/2016
 * 
 */
package commons.generators;

/**
 *  This interface lists the methods that all random structure generators must implement.
 * @author igallego
 *
 */
public interface IRandomStructureGenerator {

	/**
	 * No matter which structure does the generator generates, the method must have a name (to print into the console).
	 * 
	 * @return
	 */
	public String getMethodName();
	
	/**
	 *  If you like to see debug information while the generation is taking place
	 *  
	 */
	public void setVerbose(boolean show);
}
