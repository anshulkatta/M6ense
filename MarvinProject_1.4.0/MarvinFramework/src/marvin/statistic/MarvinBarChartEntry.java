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
 * Bar Chart entry.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinBarChartEntry
{
	protected String name;
	protected double value;
	protected Color color;

	public MarvinBarChartEntry(){
		this("",0,Color.black);
	}

	public MarvinBarChartEntry(String name, double value){
		this(name, value, Color.black);
	}

	public MarvinBarChartEntry(String name, double value, Color color){
		this.name = name;
		this.value = value;
		this.color = color;
	}

	public String getName(){
		return name;
	}

	public double getValue(){
		return value;
	}

	public Color getColor(){
		return color;
	}
}