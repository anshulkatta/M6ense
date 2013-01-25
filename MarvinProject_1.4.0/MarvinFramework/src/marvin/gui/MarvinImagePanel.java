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

package marvin.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import marvin.image.MarvinImage;
import marvin.util.MarvinPluginHistory;

/**
 * Panel to display MarvinImages.
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinImagePanel extends JPanel{
	
	protected MarvinImage 			image;
	protected MarvinPluginHistory 	history;
	private boolean					fitSizeToImage;
	
	/**
	 * Constructor
	 */
	public MarvinImagePanel(){
		super();
		fitSizeToImage = true;
	}
	
	/**
	 * Enable history
	 */
	public void enableHistory(){
		history = new MarvinPluginHistory();
	}
	
	/**
	 * Disable history
	 */
	public void disableHistory(){
		history = null;
	}
	
	/**
	 * Returns if the history is enabled.
	 * @return true if the history is enabled, false otherwise
	 */
	public boolean isHistoryEnabled(){
		return (history != null);
	}
	
	/**
	 * Returns the MarvinPluginHistory associated with this panel.
	 * @return MarvinPluginHistory reference
	 */
	public MarvinPluginHistory getHistory(){
		return history;
	}
	
	/**
	 * Instantiates the MarvinImage object and returns its BufferedImage as off-screen 
	 * drawable image to be used for double buffering. 
	 * @param width 	image큦 width
	 * @param height	image큦 width
	 */
	public Image createImage(int width, int height){
		image = new MarvinImage(width, height);		
		setPreferredSize(new Dimension(width, height));		
		return image.getBufferedImage();
	}
	
	/**
	 * Associates a MarvinImage to the image panel.
	 * @param img	image큦 reference to be associated with the image panel.
	 */
	public void setImage(MarvinImage img){
		image = img;
		if(fitSizeToImage && img != null){
			setPreferredSize(new Dimension(image.getWidth(),image.getHeight()));
		}
		repaint();
	}
	
	
	/**
	 * Returns the MarvinImage associated with this panel.
	 * @return MarvinImage reference.
	 */
	public MarvinImage getImage(){
		return image;
	}
	
	/**
	 * Overwrite the paint method
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(image != null){
			g.drawImage(image.getBufferedImage(), 0,0,this);
		}
	}
	
	/**
	 * Update component큦 graphical representation
	 */
	public void update(){
		image.update();
		repaint();
	}
}
