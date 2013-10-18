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
package org.tr.jsr303.validation.interceptor;

import javax.validation.Configuration;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.spi.ValidationProvider;

import org.apache.commons.lang3.StringUtils;
import org.tr.jsr303.validation.constant.ValidatorConstants;

import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * <p>
 * This is the central class for javax.validation (JSR-303) setup in a Struts2 : It bootstraps a
 * javax.validation.ValidationFactory and exposes it through the javax.validation.Validator interface and the. When
 * talking to an instance of this bean we will be talking to the default Validator of the underlying ValidatorFactory.
 * </p>
 * <p>
 * This is very convenient in that you don't have to perform yet another call on the factory, assuming that you will
 * almost always use the default Validator anyway.You need to pass provider class in order for this plugin to hook
 * itself to underlying validation Factory. Any of following Validation provider can be provided using
 * <code>jsr303.validation.providerClass</code>
 * <li>Hibernate Validator</li>
 * <li>Apache Bean Validator
 * </p>
 * </p>
 * 
 * @author Umesh Awasthi
 */
public class DefaultJSR303ValidationManager
    implements JSR303ValidationManager
{

    private static final Logger LOG = LoggerFactory.getLogger( DefaultJSR303ValidationManager.class );

    @SuppressWarnings( "rawtypes" )
    protected Class<? extends ValidationProvider> providerClass;

    private ValidatorFactory validationFactory;

    @SuppressWarnings( { "unchecked", "rawtypes", "nls" } )
    @Inject
    public DefaultJSR303ValidationManager( @Inject( value = ValidatorConstants.PROVIDER_CLASS, required = false )
    String providerClassName, @Inject( value = ValidatorConstants.IGNORE_XMLCONFIGURAITION, required = false )
    String ignoreXMLConfiguration )
    {
        super();
        LOG.info( "Initializing bean validation11 factory to get a validator" );

        if ( StringUtils.isNotBlank( providerClassName ) )
        {
            try
            {
                this.providerClass = (Class<? extends ValidationProvider>) Class.forName( providerClassName );
                LOG.info( this.providerClass.getName() + " validator found" );
            }
            catch ( ClassNotFoundException e )
            {
                LOG.error( "Unable to find any bean validator implimentation for " + providerClassName );
                LOG.error( "Unable to load bean validation prvider class", e );
            }

        }
        if (LOG.isDebugEnabled()) {
            if(this.providerClass == null){
                
                String message =
                                "**********No bean validator class defined **********\n"
                                    + "Falling back to default provider \n";
                LOG.debug( message );
               
            }
        }
        
        Configuration configuration =
            ( this.providerClass != null ? Validation.byProvider( this.providerClass ).configure()
                            : Validation.byDefaultProvider().configure() );
        if ( "true".equalsIgnoreCase( ignoreXMLConfiguration ) )
        {
            configuration.ignoreXmlConfiguration();
            String message =
                            "**********Setting ignoreXmlConfiguration flag to true **********\n"
                                + "XML configurations will be ignore by Validator \n"
                                + "To enable XML based validation, set ignoreXmlConfiguration to false.\n";
            LOG.info( message );
        }
        if ( configuration != null )
        {
            
            this.validationFactory = configuration.buildValidatorFactory();

        }

    }

    /**
     * <p>
     * Method to return Validator instance.This will take in to account the provider class will try to create a
     * validation factory from given Validator. Validator will be returned based on the user preference.Validator will
     * be created based on the following cases.
     * </p>
     * <p>
     * In case user has specify explicitly and in a type safe fashion the expected provider, it will be used to create
     * validation factory and an instance of javax.validation.Validator will be returned.
     * </p>
     * <p>
     * In this case, the default validation provider resolver will be used to locate available providers. The chosen
     * provider is defined as followed:
     * <li>if the XML configuration defines a provider, this provider is used</li>
     * <li>if the XML configuration does not define a provider or if no XML configuration is present the first provider
     * returned by the ValidationProviderResolver instance is used.</li>
     * </p>
     */
    @Override
    public Validator getValidator()
    {
        return this.validationFactory.getValidator();

    }

    /*
     * will be used in future release of plugin to provide more flexibility. private MessageInterpolator
     * messageInterpolator; private TraversableResolver traversableResolver;
     */
}
