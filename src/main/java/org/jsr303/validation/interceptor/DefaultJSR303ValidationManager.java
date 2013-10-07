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

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * <p>
 * This is the central class for javax.validation (JSR-303) setup in a Struts2 : It bootstraps a
 * javax.validation.ValidationFactory and exposes it through the javax.validation.Validator
 * interface and the. When talking to an instance of this bean we will be talking to the default Validator of the underlying ValidatorFactory.
 * </p>
 * <p>
 * This is very convenient in that you don't have to perform yet another call on the factory, assuming that you will
 * almost always use the default Validator anyway.You need to pass provider class in order for this plugin to hook itself to underlying
 * validation Factory.
 * Any of following Validation provider can be provided using <code>jsr303.validation.providerClass</code>
 * 
 * <li>Hibernate Validator</li>
 * <li>Apache Bean Validator </p>
 * </p>
 * @author Umesh Awasthi
 */
public class DefaultJSR303ValidationManager implements JSR303ValidationManager
{

    private static final Logger LOG=LoggerFactory.getLogger(DefaultJSR303ValidationManager.class);
    
   
    
    @Inject
    public DefaultJSR303ValidationManager(@Inject Container container)
    {
        super();
        LOG.info("Initializing bean validation factory to get a validator");
        //container.getInstance(ValidationProvider.class, "jsr303.validation.providerClass");
        //setProviderClass(container.getInstance(Class<? extends ValidationProvider>, "jsr303.validation.providerClass"));
         
        //container.getInstance(Class<? extends ValidationProvider>, "jsr303.validation.providerClass");
        //List<ValidationProvider> list=
        //javax.validation.bootstrap
       
       
    }


    @SuppressWarnings( "rawtypes" )
    protected Class providerClass;
    private ValidatorFactory validationFactory;
   
    
    
    /**
     * Set Validation provider class which will be used to do actual validation.
     * Provider class can be any of the implementation which adhere to the JSR303 specifications.
     */
    @SuppressWarnings( "rawtypes" )
   // @Inject(value="providerClass")
    public void setProviderClass( Class<? extends ValidationProvider> providerClass)
    {
        this.providerClass = providerClass;
    }

   



   
  
    /**
     * <p>Method to return Validator instance.This will take in to account the 
     * provider class will try to create a validation factory from given Validator.
     * Validator will be returned based on the user preference.Validator will be created based on the following cases.</p>
     * 
     * <p>In case user has specify explicitly and in a type safe fashion the expected provider, it will be used to create validation factory and 
     * an instance of javax.validation.Validator will be returned.</p>
     * <p>
     * In this case, the default validation provider resolver will be used to locate available providers. The chosen provider is defined as followed:
     * <li>if the XML configuration defines a provider, this provider is used</li>
     * <li>if the XML configuration does not define a provider or if no XML configuration is present the first provider returned by the ValidationProviderResolver instance is used.</li>
     * </p>
     */
    @Override
    public Validator getValidator()
    {
       
        if(providerClass ==null){
             String message =
                "**********No EXPLICT VALIDATOR HAS BEEN PROVIDED **********\n"
                    + "Looks like no bean validator has been provided by you for web app! \n"
                    + "Building default validation factory.\n";
            LOG.info( message );
           
        }
        
        
        return getValidationFactory().getValidator();
    }
    
    @SuppressWarnings("unchecked")
    private ValidatorFactory getValidationFactory(){
        if(validationFactory ==null){
        	if(providerClass !=null){
        		LOG.info("Creating validation factory for {}", providerClass.getName());
        		this.validationFactory=Validation.byProvider(this.providerClass).configure().buildValidatorFactory();
        	}
        	else{
        		LOG.info("No provider class has been given...falling back to default validation factory");
        		this.validationFactory=Validation.buildDefaultValidatorFactory();
        	}
       }
        
        return validationFactory;
    }
    
    
    
    /*public enum ValidationFactory{
      SINGLE_INSTANCE {

          ValidatorFactory validationFactory =
              Validation.byProvider().configure().buildValidatorFactory();

          @Override
          public Validator getValidator() {
              return validationFactory.getValidator();
          }

      };

      public abstract Validator getValidator(); 
  }*/

    
    
    /* will be used in future release of plugin to provide more flexibility.
    private MessageInterpolator messageInterpolator;
    private TraversableResolver traversableResolver;*/
}
