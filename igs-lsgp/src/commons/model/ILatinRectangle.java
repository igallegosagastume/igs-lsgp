/**
 * Creation date: 09/03/2016
 * 
 */
package commons.model;

/**
 *  All Latin rectangle classes must implement comparation and return new dimensions.
 *  
 * 
 * @author igallego
 *
 */
public interface ILatinRectangle extends ILatinSquare {
		//properties
		public int colSize();
		public int rowSize();
		
		//querys
		public boolean equals(ILatinRectangle lr) throws Exception;
		public boolean preservesLatinProperty();
		
		public boolean isASquare();
}
