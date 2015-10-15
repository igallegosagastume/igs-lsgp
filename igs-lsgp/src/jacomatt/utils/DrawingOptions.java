/**
 * Creation date: 06/06/2014
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
package jacomatt.utils;

/**
 * @author Ignacio Gallego Sagastume
 * @email ignaciogallego@gmail.com
 * @tags Java Latin Square generation
 */
public class DrawingOptions {

	//default options
	private float z = -80.0f;
	private int FPS = 40; // animator's target frames per second
	
	//window
	private String windowTitle = "Incidence Cube"; // window's
	private int frameWidth = 250;
	private int frameHeight = 250;
	private int frameXPosition = 500;
	private int frameYPosition = 500;
	
	//booleans
	private boolean fullScreen = true;
	private boolean showAxisLetters = true;
	private boolean showAxis = true;
	private boolean showGrid = false;
	private boolean showBoxes = true;
	
	/*	private final int CANVAS_WIDTH = 300; // width of the drawable
	private final int CANVAS_HEIGHT = 200; // height of the drawable*/

	public String getWindowTitle() {
		return windowTitle;
	}

	public float getZ() {
		return z;
	}
	public int getFPS() {
		return FPS;
	}
	public int getFrameWidth() {
		return frameWidth;
	}
	public int getFrameHeight() {
		return frameHeight;
	}
	public int getFrameXPosition() {
		return frameXPosition;
	}
	public int getFrameYPosition() {
		return frameYPosition;
	}
	public boolean isFullScreen() {
		return fullScreen;
	}
	public boolean isShowAxisLetters() {
		return showAxisLetters;
	}
	public boolean isShowAxis() {
		return showAxis;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public void setFPS(int fPS) {
		FPS = fPS;
	}

	public void setWindowTitle(String windowTitle) {
		this.windowTitle = windowTitle;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	public void setFrameXPosition(int frameXPosition) {
		this.frameXPosition = frameXPosition;
	}

	public void setFrameYPosition(int frameYPosition) {
		this.frameYPosition = frameYPosition;
	}

	public void setFullScreen(boolean fullScreen) {
		this.fullScreen = fullScreen;
	}

	public void setShowAxisLetters(boolean showAxisLetters) {
		this.showAxisLetters = showAxisLetters;
	}

	public void setShowAxis(boolean showAxis) {
		this.showAxis = showAxis;
	}

	public boolean isShowGrid() {
		return showGrid;
	}

	public void setShowGrid(boolean showGrid) {
		this.showGrid = showGrid;
	}

	public boolean isShowBoxes() {
		return showBoxes;
	}

	public void setShowBoxes(boolean showBoxes) {
		this.showBoxes = showBoxes;
	}
	

	
}
