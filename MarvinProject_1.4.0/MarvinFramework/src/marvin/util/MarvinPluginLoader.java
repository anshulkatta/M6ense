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

package marvin.util;

import marvin.MarvinDefinitions;
import marvin.plugin.MarvinPlugin;
import marvin.plugin.MarvinImagePlugin;

/**
 * Load plug-ins via MarvinJarLoader
 * @author Gabriel Ambrosio Archanjo
 */
public class MarvinPluginLoader {
	
	/**
	 * Loads a MarvinPluginImage via MarvinJarLoader
	 * @param pluginPath		plug-in´s jar file path.
	 * @return 					a loaded MarvinPluginImage.
	 */
	public static MarvinImagePlugin loadImagePlugin(String pluginPath){
		MarvinImagePlugin l_plugin;
		String l_className = pluginPath.replace(".jar", "");
		if(l_className.lastIndexOf(".") != -1){
			l_className = l_className.substring(l_className.lastIndexOf(".")+1);
		}
		l_className = l_className.substring(0,1).toUpperCase()+l_className.substring(1);
		
		l_plugin = (MarvinImagePlugin)loadPlugin(MarvinDefinitions.PLUGIN_IMAGE_PATH+pluginPath, l_className);
		l_plugin.load();
		return l_plugin;
	}
	
	/**
	 * Loads a MarvinPlugin via MarvinJarLoader
	 * @param pluginPath	plug-in´s jar file path.
	 * @param className		plug-in´s class name.
	 * @return
	 */
	private static MarvinPlugin loadPlugin(String pluginPath, String className){
		if(!pluginPath.substring(pluginPath.length()-4, pluginPath.length()).equals(".jar")){
			pluginPath = pluginPath + ".jar";
		}
		
		MarvinPlugin l_plugin;
		MarvinJarLoader l_loader = new MarvinJarLoader(pluginPath);
		l_plugin = (MarvinPlugin)l_loader.getObject(className);
		return l_plugin;
	}
}
