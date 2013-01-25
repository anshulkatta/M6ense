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

package marvin.plugin;

import marvin.gui.MarvinAttributesPanel;
import marvin.gui.MarvinImagePanel;
import marvin.image.MarvinImage;
import marvin.image.MarvinImageMask;

public abstract class MarvinAbstractImagePlugin extends MarvinAbstractPlugin implements MarvinImagePlugin
{
	//private Marvin marvinApplication;
	private MarvinImagePanel 		imagePanel;
	
	private boolean valid;

	/**
	 * Associates the plug-in with an MarvinImagePanel
	 * @param imgPanel reference to a MarvinImagePanel object
	 */
	public void setImagePanel(MarvinImagePanel imgPanel){
		imagePanel = imgPanel;
	}
	
	/**
	 * @return a reference to the associated MarvinImagePanel. If no one MarvinImagePanel is associated with this plug-in,
	 * this method returns null.
	 */
	public MarvinImagePanel getImagePanel(){
		return imagePanel;
	}
	
	/**
	 * Executes the algorithm.
	 * @param imgIn				input image.
	 * @param imgOut			output image.
	 * @param attrOut			output attributes.
	 */
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut, 
		MarvinImageMask mask
	){
		process(imgIn, imgOut, null, mask, false);
	}
	
	/**
	 * Executes the algorithm.
	 * @param imgIn				input image.
	 * @param imgOut			output image.
	 */
	public void process
	(
		MarvinImage imgIn, 
		MarvinImage imgOut
	){
		process(imgIn, imgOut, null, MarvinImageMask.NULL_MASK, false);
	}	
}