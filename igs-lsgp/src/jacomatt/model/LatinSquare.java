/**
 * Creation date: 08/07/2014
 * 
 * Master thesis on Latin Squares generation
 * 
 */

/**
 * © Copyright 2012-2015 Ignacio Gallego Sagastume
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

import jacomatt.opengl.DrawLatinSquare;
import jacomatt.utils.DrawingOptions;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.awt.GLCanvas;
import javax.swing.JFrame;

import com.jogamp.opengl.util.FPSAnimator;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
@Deprecated
public class LatinSquare {
	protected int n = 26;
	protected char[][] square = {};
	protected DrawingOptions drawingOptions;
	
	public LatinSquare() {
		this.init();//default size=26
	}
	public LatinSquare(int n) {
		this.n = n;//size of the ls
		this.init();
	}
	
	public void init() {
		this.square = new char[n][n];
		//initialization as cyclic ls
		char letter = 'a';//char 97
		for (int x=0; x<n ; x++) {
			letter = (char)(x + 97);
			for (int y=0; y<n ; y++) {
				square[x][y] = letter;
				letter++;
				if (letter==123) //z
					letter = 97; //a
			}
		}
		
		this.drawingOptions = new DrawingOptions();
	}
	
	public void atPut(int x, int y, char c) {
		square[x][y] = c;
	}
	
	public char letterOf(int x, int y) {
		return square[x][y];
	}
	
	public int zCoordOf(int x, int y) {
		char a = square[x][y];
		int res = a - 97;
		return res;
	}
	
	public int size() {
		return this.n;
	}
	
	public void drawIncidenceCube() {
		// Create the OpenGL rendering canvas
		GLCanvas canvas = new GLCanvas(); // heavy-weight GLCanvas
		
		DrawingOptions opts = this.getDrawingOptions();
		
		//canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		DrawLatinSquare renderer = new DrawLatinSquare(this);
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
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Latin Square of size "+n+":\n");
		for (int x=0; x<n ; x++) {
			sb.append("Row "+x+":");
			for (int y=0; y<n ; y++) {
				sb.append(square[x][y]+" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public DrawingOptions getDrawingOptions() {
		return drawingOptions;
	}
	public void setDrawingOptions(DrawingOptions drawingOptions) {
		this.drawingOptions = drawingOptions;
	}

}
