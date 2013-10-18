/*******************************************************************************
 *
 *  Struts2-JSR303-beanValidation-Plugin - An Open Source Struts2 Plugin to hook Bean Validator.
 *  =================================================================================================================
 *
 *  Copyright (C) 2013 by Umesh Awasthi
 *  https://github.com/umeshawasthi/jsr303-validator-plugin
 *
 * **********************************************************************************************************************
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 *  an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations under the License.
 *
 * **********************************************************************************************************************/
package org.tr.jsr303.constraints.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.tr.jsr303.constraints.FieldMatch;
import org.tr.jsr303.validation.util.BeanUtils;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * @author Umesh Awasthi
 * @version 1.0
 *
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    
    private static final Logger LOG=LoggerFactory.getLogger(FieldMatchValidator.class);
    private String firstFieldName;
    private String secondFieldName;
    private BeanUtils beanUtils;

    @Override
    public void initialize(final FieldMatch constraintAnnotation)
    {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
        this.beanUtils=BeanUtils.newInstance();
    }

    @SuppressWarnings( "nls" )
    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        try
        {
            final Object firstObj = this.beanUtils.getPropertyValue(value, this.firstFieldName);
            final Object secondObj = this.beanUtils.getPropertyValue(value, this.secondFieldName);
            return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception ex)
        {
            LOG.info( "Error while getting values from object", ex );
            return false;
           
        }
       
    }
}
