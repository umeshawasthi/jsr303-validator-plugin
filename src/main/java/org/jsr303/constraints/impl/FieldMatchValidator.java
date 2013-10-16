/**
 * 
 */
package org.jsr303.constraints.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.BooleanUtils;
import org.jsr303.constraints.FieldMatch;
import org.jsr303.validation.interceptor.JSR303ValidationInterceptor;

import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * @author Umesh A
 *
 */
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object>
{
    
    private static final Logger LOG=LoggerFactory.getLogger(FieldMatchValidator.class);
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation)
    {
        this.firstFieldName = constraintAnnotation.first();
        this.secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context)
    {
        try
        {
           
            //final Object firstObj = BeanUtils.getProperty(value, this.firstFieldName);
            //final Object secondObj = BeanUtils.getProperty(value, this.secondFieldName);

            //return firstObj == null && secondObj == null || firstObj != null && firstObj.equals(secondObj);
        }
        catch (final Exception ex)
        {
            LOG.info( "Error while getting values from object", ex );
        }
        return true;
    }
}
