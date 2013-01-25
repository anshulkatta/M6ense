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

/**
 * Algorithms can be divided in events, each event store your own attributes for future analysis.
 * 
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceEvent
{
	private String id;
	private String name;
	private long startTime;
	private long endTime;
	private int currentStep;
	
	/**
	 * 
	 * @param a_name
	 */
	public MarvinPerformanceEvent(String a_name){
		this(a_name, a_name);
	}

	/**
	 * 
	 * @param a_id
	 * @param a_name
	 */
	public MarvinPerformanceEvent(String a_id, String a_name){
		id = a_id;
		name = a_name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getID(){
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 */
	public void start(){
		startTime = System.currentTimeMillis();
		currentStep=0;
	}

	/**
	 * 
	 */
	public void finish(){
		endTime = System.currentTimeMillis();
	}

	/**
	 * 
	 */
	public void stepFinished(){
		currentStep++;
	}

	/**
	 * 
	 * @param a_steps
	 */
	public void stepsFinished(int a_steps){
		currentStep+=a_steps;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getCurrentStep(){
		return currentStep;
	}

	/**
	 * 
	 * @return
	 */
	public long getTotalTime(){
		return endTime-startTime;
	}
}