/**
 * 
 */
package org.jsr303.constraints.impl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.jsr303.constraints.FieldMatch;
import org.jsr303.validation.util.BeanUtils;

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
