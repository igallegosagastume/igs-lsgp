/**
 * Creation date: 14/03/2016
 * 
 */
package jacomatt.model.generators;

import jacomatt.model.EfficientIncidenceCube;
import commons.generators.IRandomLatinSquareGenerator;
import commons.model.latinsquares.ILatinSquare;

/**
 * @author igallego
 *
 */
public class JacobsonMatthewsLSGenerator implements IRandomLatinSquareGenerator {

	private EfficientIncidenceCube cube;
	private int n;
	
	
	public JacobsonMatthewsLSGenerator(int n) {
		this.n = n;
	}
	
	
	@Override
	public String getMethodName() {
		return "Optimized Jacobson & Matthews generation method.";
	}

	@Override
	public ILatinSquare generateLS() {
		cube = new EfficientIncidenceCube(n);
		cube.shuffle();
		
		return cube;
	}

}
