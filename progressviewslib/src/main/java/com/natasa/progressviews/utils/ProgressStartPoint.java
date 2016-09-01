/*
 * ProgressViews
 * Copyright (c) 2015  Natasa Misic
 *
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.natasa.progressviews.utils;
/***only for CircleProgressBar and CircleSegmentBar**/
public enum ProgressStartPoint {
	
	DEFAULT(-90), LEFT(180), RIGHT(0), BOTTOM(90);
	int value;
	private ProgressStartPoint(int value) {
		this.value = value;
	}
	public int getValue() {
		return value;
	}
}
