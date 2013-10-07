/*******************************************************************************
 *
 *  Struts2-JSR303-beanValidation-Plugin - An Open Source Struts2 Plugin to hook Bean Validator.
 *  =================================================================================================================
 *
 *  Copyright (C) 2012 by Umesh Awasthi
 *  http://code.google.com/p/struts2-jsr303-validator/
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

package org.jsr303.validation.interceptor;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.validator.DelegatingValidatorContext;
import com.opensymphony.xwork2.validator.ValidatorContext;

/**
 * <p>
 * JSR 303 bean Validation interceeptor.This Intercepter do not itself provide any Bean validation functionality but
 * works as a bridge between JSR303 Bean validation specification and Struts2 validation mechanism.
 * </p>
 * <p>
 * Intercepter will create a Validation Factory based on the provider class and will validate requested method or Action
 * class. Hibernate bean validator will be used as a default validator in case of no provider class will be supplied to
 * the intercepter.
 * </p>
 * 
 * @author Umesh Awasthi
 */
public class JSR303ValidationInterceptor extends MethodFilterInterceptor {

	
	private static final Logger LOG=LoggerFactory.getLogger(JSR303ValidationInterceptor.class);
	private static final long serialVersionUID = 1L;
    protected JSR303ValidationManager jsr303ValidationManager; 
	 
    
    @Inject()
    public void setJsr303ValidationManager( JSR303ValidationManager jsr303ValidationManager )
    {
        this.jsr303ValidationManager = jsr303ValidationManager;
    }


    @Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		/*if(jsr303ValidationManager.getValidator() ==null){
		    
		    if (LOG.isDebugEnabled()) {
                LOG.debug("There is not Bean Validator configured in class path. Skipping Bean validation..");
            }
		     return invocation.invoke();
		    
		}*/
		 if (LOG.isDebugEnabled()) {
                LOG.debug("Starting bean validation using "+jsr303ValidationManager.getValidator().getClass());
            }
		    
		    Object action=invocation.getAction();
		    ActionProxy actionProxy=invocation.getProxy();
		    ValueStack valueStack=invocation.getStack();
		    String methodName=actionProxy.getMethod();
		    String context = actionProxy.getConfig().getName();
		    
		    if (LOG.isDebugEnabled()) {
	            LOG.debug("Validating [#0/#1] with method [#2]", invocation.getProxy().getNamespace(), invocation.getProxy().getActionName(), methodName);
	        }
		    
		    // performing jsr303 bean validation
		    performBeanValidation(action, valueStack, methodName, context);
		    
		    return invocation.invoke();
		
		
	}
    
    protected void performBeanValidation(Object action, ValueStack valueStack, String methodName, String context) throws NoSuchMethodException{
        
        Validator validator = jsr303ValidationManager.getValidator();

        if ( validator == null )
        {

            if ( LOG.isDebugEnabled() )
            {
                LOG.debug( "There is not Bean Validator configured in class path. Skipping Bean validation.." );
            }

            // we will not do anything let's continue
            return;
        }
        
       Set<ConstraintViolation<Object>> constraintViolations= validator.validate(action);
       addBeanValidationErros(constraintViolations,action,valueStack,null);
     
       
    }
    
    // parentFieldName has been taken from Oval plugin ;)
    private void addBeanValidationErros( Set<ConstraintViolation<Object>> constraintViolations, Object action,
                                         ValueStack valueStack, String parentFieldname )
    {
        if ( constraintViolations != null )
        {
            ValidatorContext validatorContext = new DelegatingValidatorContext( action );
            while ( constraintViolations.iterator().hasNext() )
            {
                ConstraintViolation<Object> constraintViolation = constraintViolations.iterator().next();
                // get message
                String key = constraintViolation.getMessage();
                String message = key;
                try
                {
                    message = validatorContext.getText( key );
                }
                catch ( Exception e )
                {
                    LOG.error( "Error while trying to fetch message", e );
                }
                
                if (isActionError(constraintViolation)) {
                    if (LOG.isDebugEnabled()) {
                    LOG.debug("Adding action error '#0'", message);
                    }
                    validatorContext.addActionError(message);
                } else {
                    
                    ValidationError validationError = buildBeanValidationError(constraintViolation, message);
                }
            }
        }
    }
    
    
    protected ValidationError buildBeanValidationError(ConstraintViolation<Object> violation, String message){
        //violation.getPropertyPath().iterator().next().
        return null;
    }
    
    /**
     * Decide if a violation should be added to the fieldErrors or actionErrors
     */
    protected boolean isActionError(ConstraintViolation violation) {
        // need to add futture code here to check action level error
        return false;
    }
    
  /**
   * Inner class for validation error
   * Nice concept taken from  Oval plugin. 
   */
    
    class ValidationError {
        private String fieldName;
        private String message;

        ValidationError(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getMessage() {
            return message;
        }
    }

}
