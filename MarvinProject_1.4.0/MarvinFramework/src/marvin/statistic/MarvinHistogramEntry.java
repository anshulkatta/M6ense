/**
Marvin Project <2007-2009>

Initial version by:

Danilo Rosetto Munoz
Fabio Andrijauskas
Gabriel Ambrosio Archanjo

site: http://marvinproject.sourceforge.net

GPL
Copyright (C) <2007>  

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package marvin.statistic;

import java.awt.Color;

/**
 * Histogram entry.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinHistogramEntry
{
	protected String name;
	protected double valueX;
	protected double valueY;
	protected Color color;

	public MarvinHistogramEntry(){
		this("",0,0,Color.black);
	}

	public MarvinHistogramEntry(double valueX, double valueY){
		this("", valueX, valueY, Color.black);
	}

	public MarvinHistogramEntry(double valueX, double valueY, Color a_color){
		this("", valueX, valueY, a_color);
	}

	public MarvinHistogramEntry(String a_name, double valueX, double valueY){
		this(a_name, valueX, valueY, Color.black);
	}

	public MarvinHistogramEntry(String a_name, double valueX, double valueY, Color a_color){
		name = a_name;
		this.valueX = valueX;
		this.valueY = valueY;
		color = a_color;
	}

	public String getName(){
		return name;
	}

	public double getValueX(){
		return valueX;
	}

	public double getValueY(){
		return valueY;
	}

	public Color getColor(){
		return color;
	}
};