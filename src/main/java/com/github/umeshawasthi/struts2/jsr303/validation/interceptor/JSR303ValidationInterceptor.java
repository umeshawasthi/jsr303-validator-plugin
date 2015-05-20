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

package com.github.umeshawasthi.struts2.jsr303.validation.interceptor;

import com.github.umeshawasthi.struts2.jsr303.validation.constant.ValidatorConstants;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
import com.opensymphony.xwork2.util.AnnotationUtils;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.validator.DelegatingValidatorContext;
import com.opensymphony.xwork2.validator.ValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * JSR 303 bean Validation interceptor. This Interceptor do not itself provide any Bean validation functionality but
 * works as a bridge between JSR303 Bean validation specification and Struts2 validation mechanism.
 * </p>
 * <p>
 * Interceptor will create a Validation Factory based on the provider class and will validate requested method or Action
 * class. Hibernate bean validator will be used as a default validator in case of no provider class will be supplied to
 * the interceptor.
 * </p>
 *
 * @author Umesh Awasthi
 */
public class JSR303ValidationInterceptor extends MethodFilterInterceptor {


    private static final Logger LOG = LoggerFactory.getLogger(JSR303ValidationInterceptor.class);
    private static final long serialVersionUID = 1L;
    protected JSR303ValidationManager jsr303ValidationManager;
    protected boolean convertToUtf8 = false;
    protected String convertFromEncoding = "ISO-8859-1";


    @Inject()
    public void setJsr303ValidationManager(JSR303ValidationManager jsr303ValidationManager) {
        this.jsr303ValidationManager = jsr303ValidationManager;
    }

    @Inject(value = ValidatorConstants.CONVERT_MESSAGE_TO_UTF8, required = false)
    public void setConvertToUtf8(String convertToUtf8) {
        this.convertToUtf8 = Boolean.valueOf(convertToUtf8);
    }

    @Inject(value = ValidatorConstants.CONVERT_MESSAGE_FROM, required = false)
    public void setConvertFromEncoding(String convertFromEncoding) {
        this.convertFromEncoding = convertFromEncoding;
    }


    @SuppressWarnings("nls")
    @Override
    protected String doIntercept(ActionInvocation invocation) throws Exception {
        Validator validator = this.jsr303ValidationManager.getValidator();
        if (validator == null) {

            if (LOG.isDebugEnabled()) {
                LOG.debug("There is no Bean Validator configured in class path. Skipping Bean validation..");
            }
            return invocation.invoke();

        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Starting bean validation using " + validator.getClass());
        }

        Object action = invocation.getAction();
        ActionProxy actionProxy = invocation.getProxy();
        String methodName = actionProxy.getMethod();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Validating [#0/#1] with method [#2]", invocation.getProxy().getNamespace(), invocation.getProxy().getActionName(), methodName);
        }

        Collection<Method> annotatedMethods = AnnotationUtils.getAnnotatedMethods(action.getClass(),
                SkipValidation.class);

        if (!annotatedMethods.contains(getActionMethod(action.getClass(), methodName))) {
            // performing jsr303 bean validation
            performBeanValidation(action, validator);
        }

        return invocation.invoke();


    }

    @SuppressWarnings("nls")
    protected void performBeanValidation(Object action, Validator validator) {

        LOG.debug("Initiating bean validation..");

        Set<ConstraintViolation<Object>> constraintViolations;

        if (action instanceof ModelDriven) {
            LOG.debug("Performing validation on model..");
            constraintViolations = validator.validate(((ModelDriven) action).getModel());
        } else {
            LOG.debug("Performing validation on action..");
            constraintViolations = validator.validate(action);
        }

        addBeanValidationErrors(constraintViolations, action);
    }

    @SuppressWarnings("nls")
    private void addBeanValidationErrors(Set<ConstraintViolation<Object>> constraintViolations, Object action) {
        if (constraintViolations != null) {
            ValidatorContext validatorContext = new DelegatingValidatorContext(action);
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                String key = constraintViolation.getMessage();
                String message = key;
                try {
                    message = validatorContext.getText(key);
                    if(convertToUtf8 && StringUtils.isNotBlank(message)) {
                        message = new String(message.getBytes(convertFromEncoding), "UTF-8");
                    }
                } catch (Exception e) {
                    LOG.error("Error while trying to fetch message", e);
                }

                if (isActionError(constraintViolation)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding action error '#0'", message);
                    }
                    validatorContext.addActionError(message);
                } else {

                    ValidationError validationError = buildBeanValidationError(constraintViolation, message);
                    String fieldName = validationError.getFieldName();
                    if (action instanceof ModelDriven && fieldName.startsWith(ValidatorConstants.MODELDRIVEN_PREFIX)) {
                        fieldName = fieldName.replace("model.", ValidatorConstants.EMPTY_SPACE);
                    }
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Adding field error [#0] with message '#1'", fieldName, validationError.getMessage());
                    }
                    validatorContext.addFieldError(fieldName, validationError.getMessage());
                }
            }
         }
    }


    protected ValidationError buildBeanValidationError(ConstraintViolation<Object> violation, String message) {

        if (violation.getPropertyPath().iterator().next().getName() != null) {
            String fieldName = violation.getPropertyPath().toString();
            String finalMessage = StringUtils.removeStart(message, fieldName + ValidatorConstants.FIELD_SEPERATOR);
            return new ValidationError(fieldName, finalMessage);
        }

        return null;

    }

    /**
     * Decide if a violation should be added to the fieldErrors or actionErrors
     */
    protected boolean isActionError(ConstraintViolation<Object> violation) {
        return violation.getLeafBean() == violation.getInvalidValue();
    }

    /**
     * This is copied from DefaultActionInvocation
     */
    protected Method getActionMethod(Class actionClass, String methodName)
            throws NoSuchMethodException {
        Method method;

        try {
            method = actionClass.getMethod(methodName, new Class[0]);
        } catch (NoSuchMethodException e) {
            // hmm -- OK, try doXxx instead
            try {
                String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() +
                        methodName.substring(1);
                method = actionClass.getMethod(altMethodName, new Class[0]);
            } catch (NoSuchMethodException e1) {
                // throw the original one
                throw e;
            }
        }

        return method;
    }

    /**
     * Inner class for validation error
     * Nice concept taken from  Oval plugin.
     */

    class ValidationError {
        private final String fieldName;
        private final String message;

        ValidationError(String fieldName, String message) {
            this.fieldName = fieldName;
            this.message = message;
        }

        public String getFieldName() {
            return this.fieldName;
        }

        public String getMessage() {
            return this.message;
        }
    }

}
