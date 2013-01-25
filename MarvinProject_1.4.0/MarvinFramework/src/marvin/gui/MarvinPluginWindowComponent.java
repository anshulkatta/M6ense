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

import javax.swing.JComponent;

import marvin.util.MarvinAttributes;

/**
 * Generic component for PluginWindow.
 * @version 02/13/08
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginWindowComponent
{
	
	public enum ComponentType{
		COMPONENT_TEXTFIELD, COMPONENT_SLIDER, COMPONENT_COMBOBOX, 
		COMPONENT_LABEL, COMPONENT_IMAGE, COMPONENT_TEXTAREA, COMPONENT_CHECKBOX,
		COMPONENT_MATRIX_PANEL
	};
	
	protected String id;
	protected String attributeID;
	protected MarvinAttributes attributes;
	protected JComponent component;
	ComponentType type;
	
	/**
	 * Constructs a new {@link MarvinPluginWindowComponent}
	 * @param a_id
	 * @param attrID 
	 * @param attr {@link MarvinAttributes}
	 * @param comp {@link JComponent}
	 * @param type
	 */
	public MarvinPluginWindowComponent(String id, String attrID, MarvinAttributes attr, JComponent comp, ComponentType type){
		this.id = id;
		attributeID = attrID;
		attributes = attr;
		component = comp;
		this.type = type;
	}

	/**
	 * Returns the component큦 ID.
	 * @return the component큦 ID
	 */
	public String getID(){
		return id;
	}

	/**
	 * Returns the ID of the attribute associated with the component.
	 * @return attribute큦 ID.
	 */
	public String getAttributeID(){
		return attributeID;
	}

	/**
	 * Returns Atribute object큦 reference.
	 * @return MarvinAttribute reference.
	 */
	public MarvinAttributes getAttributes(){
		return attributes;
	}

	/**
	 * Returns the swing component representation of this component.
	 * @return the Swing component
	 */
	public JComponent getComponent(){
		return component;
	}

	/**
	 * Returns the type of the component.
	 * @return the component큦 type.
	 */
	public ComponentType getType(){
		return type;
	}
}