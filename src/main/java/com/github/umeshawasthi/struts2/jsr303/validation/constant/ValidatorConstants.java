/**
 * ****************************************************************************
 * <p/>
 * Struts2-JSR303-beanValidation-Plugin - An Open Source Struts2 Plugin to hook Bean Validator.
 * =================================================================================================================
 * <p/>
 * Copyright (C) 2013 by Umesh Awasthi
 * https://github.com/umeshawasthi/jsr303-validator-plugin
 * <p/>
 * **********************************************************************************************************************
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * <p/>
 * *********************************************************************************************************************
 */
package com.github.umeshawasthi.struts2.jsr303.validation.constant;

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
@SuppressWarnings("nls")
public final class ValidatorConstants {

    public static final String PROVIDER_CLASS = "struts.jsr303.beanValidator.providerClass";
    public static final String IGNORE_XMLCONFIGURAITION = "struts.jsr303.beanValidator.ignoreXMLConfiguration";
    public static final String FIELD_SEPERATOR = ".";
    public static final String MODELDRIVEN_PREFIX = "model";
    public static final String EMPTY_SPACE = "";

}
