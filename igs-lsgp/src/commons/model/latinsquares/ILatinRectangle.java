/**
 * Creation date: 09/03/2016
 * 
 */
package commons.model.latinsquares;

/**
 *  All Latin rectangle classes must implement comparison and return new dimensions.
 *  
 * 
 * @author igallego
 *
 */
public interface ILatinRectangle extends ILatinSquare {

		/**
		 * Must return number of columns
		 * 
		 * @return
		 */
		public int colSize();
		
		/**
		 * Must return number of rows
		 * 
		 * @return
		 */
		public int rowSize();
		
		/**
		 * Equality of Latin Rectangles
		 * 
		 * @return
		 */
		public boolean equals(ILatinRectangle lr) throws Exception;
		
		/**
		 *  Check if the structure has repetitions in some row or column.
		 *  
		 */
		public boolean preservesLatinProperty();
		
		/**
		 *  A LR can be a LS. Returns true if the LR has the same number of rows and columns.
		 *  
		 * @return
		 */
		public boolean isASquare();
}
