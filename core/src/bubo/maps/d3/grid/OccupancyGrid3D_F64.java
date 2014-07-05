/*
 * Copyright (c) 2013-2014, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Project BUBO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package bubo.maps.d3.grid;

/**
 * Interface for 64bit floating point 3D occupancy grid.  An occupancy grid stores the probability of a square region
 * being occupied by an obstacle or not.  A value of 1 means 100% and 0 means 0%, 50% is unknown or equal probability.
 *
 * @author Peter Abeles
 */
public interface OccupancyGrid3D_F64 extends OccupancyGrid3D {
	/**
	 * Sets the specified cell to 'value'.
	 *
	 * @param x     x-coordinate of the cell.
	 * @param y     y-coordinate of the cell.
	 * @param value The cell's new value.
	 */
	public void set(int x, int y, int z, double value);

	/**
	 * Gets the value of the cell at the specified coordinate.
	 *
	 * @param x x-coordinate of the cell.
	 * @param y y-coordinate of the cell.
	 * @return The cell's value.
	 */
	public double get(int x, int y, int z);

	/**
	 * Checks to see if the provided value is within the valid range.
	 *
	 * @param value the value being tested
	 * @return if it is valid or not
	 */
	public boolean isValid(double value);
}
