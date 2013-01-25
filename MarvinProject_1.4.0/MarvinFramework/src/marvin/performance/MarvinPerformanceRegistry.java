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

package marvin.performance;

import java.util.LinkedList;

/**
 * Stores performance entries.
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceRegistry
{
	private LinkedList<MarvinPerformanceEntry>listEntries;

	/**
	 * empty constructor
	 */
	public MarvinPerformanceRegistry(){
		listEntries = new LinkedList<MarvinPerformanceEntry>();
	}

	/**
	 * 
	 * @param a_entry
	 */
	public void addEntry(MarvinPerformanceEntry a_entry){
		listEntries.add(a_entry);
	}

	/**
	 * 
	 * @param a_index
	 * @return
	 */
	public MarvinPerformanceEntry getEntry(int a_index){
		return listEntries.get(a_index);
	}

	/**
	 * 
	 * @return
	 */
	public int size(){
		return listEntries.size();
	}

	/**
	 * 
	 * @return
	 */
	public long getTotalTime(){
		long l_totalTime=0;
		Object[] l_entries = listEntries.toArray();
		for(int i=0; i<l_entries.length; i++){
			l_totalTime+= ((MarvinPerformanceEntry)l_entries[i]).getTotalTime();
		}
		return l_totalTime;
	}
}