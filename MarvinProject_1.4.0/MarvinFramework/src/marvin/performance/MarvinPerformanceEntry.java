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
 * Stores all the image process. An image process can be divided in events,
 * each event store attributes like start time, end time, number of steps etc. 
 * These attributes are used to do statistics analysis.
 * 
 * @version 1.0 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPerformanceEntry
{
	String id;
	String name;

	private LinkedList<MarvinPerformanceEvent>listEvents;
	private MarvinPerformanceEvent currentEvent;

	/**
	 * Constructs {@link MarvinPerformanceEntry}
	 * @param a_id
	 * @param a_name
	 */
	public MarvinPerformanceEntry(String a_id, String a_name){
		name = a_name;
		id = a_id;
		listEvents = new LinkedList<MarvinPerformanceEvent>();
	}

	/**
	 * 
	 * @param a_index
	 * @return
	 */
	public MarvinPerformanceEvent getEvent(int a_index){
		return listEvents.get(a_index);
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
	 * @return
	 */
	public int size(){
		return listEvents.size();
	}

	/**
	 * 
	 * @param a_id
	 * @param a_name
	 */
	public void startEvent(String a_id, String a_name){
		currentEvent = new MarvinPerformanceEvent(a_id, a_name);
		currentEvent.start();
		listEvents.add(currentEvent);
	}

	/**
	 * 
	 */
	public void finishEvent(){
		if(currentEvent != null){
			currentEvent.finish();
		}
	}

	/**
	 * 
	 */
	public void stepFinished(){
		if(currentEvent != null){
			currentEvent.stepFinished();
		}
	}

	/**
	 * 
	 * @param a_steps
	 */
	public void stepsFinished(int a_steps){
		if(currentEvent != null){
			currentEvent.stepsFinished(a_steps);
		}
	}

	/**
	 * 
	 * @return
	 */
	public long getCurrentStep(){
		long l_currentStep=0;
		Object[] l_events = listEvents.toArray();
		for(int i=0; i<l_events.length; i++){
			l_currentStep+= ((MarvinPerformanceEvent)l_events[i]).getCurrentStep();
		}
		return l_currentStep;
	}

	/**
	 * 
	 * @return
	 */
	public long getTotalTime(){
		long l_totalTime=0;
		Object[] l_events = listEvents.toArray();
		for(int i=0; i<l_events.length; i++){
			l_totalTime+= ((MarvinPerformanceEvent)l_events[i]).getTotalTime();
		}
		return l_totalTime;
	}
}