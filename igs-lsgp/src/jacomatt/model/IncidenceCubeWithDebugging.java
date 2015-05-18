/**

 * Creation date: 09/06/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2014 Ignacio Gallego Sagastume
 * 
 * This file is part of IGS-ls-generation package.
 * IGS-ls-generation package is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 * 
 * IGS-ls-generation package is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with IGS-ls-generation package.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
package jacomatt.model;

import jacomatt.opengl.DrawIncidenceCube;
import jacomatt.opengl.DrawIncidenceCubeWithDebugging;
import jacomatt.utils.DrawingOptions;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;
import commons.OrderedTriple;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class IncidenceCubeWithDebugging extends IncidenceCube {
	private int state = 0; //cube is proper and before selecting cells to move
	
	private List<OrderedTriple> selectedCells = new ArrayList<OrderedTriple>();
	private OrderedTriple t = null;
	private int x1, y1, z1 = -1;
		
	public IncidenceCubeWithDebugging(int n) {
		super(n);
	}
	
	public void moveFromProperStep1() {
		selectedCells = new ArrayList<OrderedTriple>();
		
		t = this.select0Cell();
		
		x1 = this.plusOneXCoordOf(t.y, t.z);
		z1 = this.plusOneZCoordOf(t.x, t.y);
		y1 = this.plusOneYCoordOf(t.x, t.z);
		
		
		this.select8Cells();
		
		state = 1;//STATE = "SELECTED CELLS ON SCREEN"
	}
	
	private void select8Cells() {
		selectedCells.add(new OrderedTriple(t.x, t.y, t.z));
		selectedCells.add(new OrderedTriple(t.x, t.y, z1));
		selectedCells.add(new OrderedTriple(t.x, y1, t.z));
		selectedCells.add(new OrderedTriple(t.x, y1, z1));
		
		selectedCells.add(new OrderedTriple(x1, t.y, t.z));
		selectedCells.add(new OrderedTriple(x1, y1, t.z));
		selectedCells.add(new OrderedTriple(x1, t.y, z1));
		selectedCells.add(new OrderedTriple(x1, y1, z1));
	}
	
	public void moveFromProperStep2() {
		//changes in chosen sub-cube
		cube[t.x][t.y][t.z]++; //sum 1 to the selected "0" cell
		cube[t.x][y1][z1]++;
		cube[x1][y1][t.z]++;
		cube[x1][t.y][z1]++;
		
		cube[t.x][t.y][z1]--; //subtract 1 to the "1" cell	
		cube[t.x][y1][t.z]--;
		cube[x1][t.y][t.z]--;
		cube[x1][y1][z1]--;
		
		//check if improper
		//(only one cell can be -1)
		/*if (cube[t.x][t.y][z1]==-1) {
			proper = false;
			improperCell = new OrderedTriple(t.x, t.y, z1);
		}
		
		if (cube[t.x][y1][t.z]==-1) {
			proper = false;
			improperCell = new OrderedTriple(t.x, y1, t.z);
		}
		
		if (cube[x1][t.y][t.z]==-1) {
			proper = false;
			improperCell = new OrderedTriple(x1, t.y, t.z);
		}*/
		
		if (cube[x1][y1][z1]  ==-1) {
			proper = false;
			improperCell = new OrderedTriple(x1, y1, z1);
		}
		if (proper) 
			state = 0;//return to initial state
		else {
			state = 2;//improper with one -1 cell
		}
		
		//erase selected cells
		selectedCells = new ArrayList<OrderedTriple>();
	}
	
	public void moveFromImproperStep3() {
		//get the improper cell:
		t = this.improperCell;
		
		x1 = this.choosePlusOneXCoordOf(t.y, t.z);
		y1 = this.choosePlusOneYCoordOf(t.x, t.z);
		z1 = this.choosePlusOneZCoordOf(t.x, t.y);
	
		this.select8Cells();
		state = 3;
	}
	
	public void moveFromImproperStep4() {
		selectedCells = new ArrayList<OrderedTriple>();
		
		//changes in chosen sub-cube
		cube[t.x][t.y][t.z]++; //sum 1 to the selected "0" cell
		cube[t.x][y1][z1]++;
		cube[x1][y1][t.z]++;
		cube[x1][t.y][z1]++;
		
		cube[t.x][t.y][z1]--; //subtract 1 to the "1" cell	
		cube[t.x][y1][t.z]--;
		cube[x1][t.y][t.z]--;
		cube[x1][y1][z1]--;

		//this is the only cell that can result -1
		if (cube[x1][y1][z1]==-1) {
			this.proper = false;
			this.improperCell = new OrderedTriple(x1, y1, z1);
		} else {
			proper = true;
			improperCell = null;
		}
		
		if (proper) {
			state = 0;
		} else {
			state = 2;
		}
		
	}

	public List<OrderedTriple> getSelectedCells() {
		return selectedCells;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
	
	@Override
	public void drawIncidenceCube() {
		// Create the OpenGL rendering canvas
		GLCanvas canvas = new GLCanvas(); // heavy-weight GLCanvas
		
		DrawingOptions opts = this.getDrawingOptions();
		
		//canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		DrawIncidenceCube renderer = new DrawIncidenceCubeWithDebugging(this);
		canvas.addGLEventListener(renderer);
		canvas.addKeyListener(renderer);
		canvas.setFocusable(true); // To receive key event
		canvas.requestFocus();

		// Create a animator that drives canvas' display() at the specified FPS.
		final FPSAnimator animator = new FPSAnimator(canvas, opts.getFPS(), true);

		// Create the top-level container frame
		final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
		frame.getContentPane().add(canvas);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// Use a dedicate thread to run the stop() to ensure that the
				// animator stops before program exits.
				new Thread() {
					@Override
					public void run() {
						animator.stop(); // stop the animator loop
						System.exit(0);
					}
				}.start();
			}
		});
		
		
		frame.setTitle(opts.getWindowTitle());
		frame.pack();
		frame.setVisible(true);
		if (opts.isFullScreen())
			frame.setExtendedState(Frame.MAXIMIZED_BOTH); // full screen mode
		else
			frame.setBounds(opts.getFrameXPosition(), opts.getFrameYPosition(),
							opts.getFrameWidth(), opts.getFrameHeight());
	
		animator.start(); // start the animation loop
	}

}
