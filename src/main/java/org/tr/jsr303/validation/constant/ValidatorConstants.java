/**
 * 
 */
package org.tr.jsr303.validation.constant;

/**
 * <p>Class consisting various constant values being used within 
 * bean validator plugin </p>
 * 
 * <p>
 * These values can be overridden using struts.xml file by 
 * providing custom values.
 * </p>
 * 
 * @author Umesh Awasthi
 * @version 1.0
 * 
 *
 */
@SuppressWarnings( "nls" )
public final class ValidatorConstants {
	
    public static final String PROVIDER_CLASS = "struts.beanValidator,providerClass";
	public static final String IGNORE_XMLCONFIGURAITION="struts.beanValidator.ignoreXMLConfiguration";
	public static final String FIELD_SEPERATOR=".";
	public static final String MODELDRIVEN_PREFIX="model";
	public static final String EMPTY_SPACE="";
	
}
