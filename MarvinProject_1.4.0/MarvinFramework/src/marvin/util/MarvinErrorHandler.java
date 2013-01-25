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

import java.security.InvalidParameterException;

import javax.swing.JOptionPane;

/**
 * Dialog message errors.
 * @version 1.0 03/28/08
 * 
 * @author Gabriel Ambrósio Archanjo
 * @author Danilo Rosetto Muñoz
 * @author Fabio Andrijauskas
 * 
 */
public class MarvinErrorHandler {
	/**
	 *	Error types
	 */
	public enum TYPE {	
		BAD_FILE, 
		ERROR_FILE_OPEN,
		ERROR_FILE_SAVE,
		ERROR_FILE_CHOOSE,
		ERROR_FILE_NOT_FOUND,
		NO_IMAGE_LOADED,
		IMAGE_RELOAD,
		ERROR_PLUGIN_NOT_FOUND
	};
	
	/**
	 * Handles error returning a new RuntimeException
	 * @param type Error type
	 * @param complement complementary message
	 */
	public static RuntimeException handle(TYPE type, String complement){
		return new RuntimeException(getErrorMessage(type)+complement);
	}
	
	/**
	 * Handles error returning a new RuntimeException
	 * @param type Error type
	 * @param complement complementary message
	 * @param e exception
	 */
	public static RuntimeException handle(TYPE type, String complement, Exception e){
		return new RuntimeException(getErrorMessage(type)+complement, e);
	}
	
	/**
	 * Handles error returning a new RuntimeException
	 * @param type Error type
	 * @param e exception
	 */
	public static RuntimeException handle(TYPE type, Exception e){
		return new RuntimeException(getErrorMessage(type), e);
	}
	
	/**
	 * Handles error showing the message based on the enum error types
	 * @param type Error type
	 */
	public static void handleDialog(TYPE type){
		handleDialog(getErrorMessage(type));
	}
	
	/**
	 * Handles error showing the message based on the enum error types and writing the StackTrace at console
	 * @param type Error type
	 * @param err Error object
	 */
	public static void handleDialog(TYPE type, Exception err){
		handleDialog(getErrorMessage(type), err);
	}
	
	/**
	 * Handles error showing the message based on the enum error types
	 * @param type Error type
	 * @param err Error message
	 */
	public static void handleDialog(TYPE type, String args){
		handleDialog(getErrorMessage(type), args);
	}
	
	private static String getErrorMessage(TYPE type){
		switch(type){
			case BAD_FILE: 					return "Bad file format!";							
			case ERROR_FILE_OPEN:			return "Error while opening the file:";
			case ERROR_FILE_SAVE:			return "Error while saving the image!";
			case ERROR_FILE_CHOOSE:			return "Error while choosing the file!";
			case ERROR_FILE_NOT_FOUND: 		return "Error! File not found:"; 
			case NO_IMAGE_LOADED:			return "No image loaded!";			
			case IMAGE_RELOAD:				return "Error while reloading the image!";
			case ERROR_PLUGIN_NOT_FOUND:	return "Error: plug-in not found!";
			default: throw new InvalidParameterException("Unknown error type");
		}
	}
	
	/**
	 * Handles error showing the message and writing the StackTrace at console
	 * @param msg Message about the error
	 * @param err Error object
	 */
	public static void handleDialog(String msg, Exception err){
		//Show the error message
		JOptionPane.showMessageDialog(null, msg, "Marvin - Error", JOptionPane.ERROR_MESSAGE);
		
		//Prints the StackTrace
		if (err != null) err.printStackTrace();
		
		//TODO Implements file logger
	}	
	
	/**
	 * Handles error showing the message
	 * @param msg Message about the error
	 */
	public static void handleDialog(String msg){
		//Show the error message
		JOptionPane.showMessageDialog(null, msg, "Marvin - Error", JOptionPane.ERROR_MESSAGE);		
	}	
	
	/**
	 * Handles error showing the message 
	 * @param msg Message about the error
	 * @param erro Error message
	 */
	public static void handleDialog(String msg, String err){
		//Show the error message 
		JOptionPane.showMessageDialog(null, msg+" "+err, "Marvin - Error", JOptionPane.ERROR_MESSAGE);		
	}
}
