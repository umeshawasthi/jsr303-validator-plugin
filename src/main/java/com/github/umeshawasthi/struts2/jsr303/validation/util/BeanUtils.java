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
package com.github.umeshawasthi.struts2.jsr303.validation.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Umesh A
 */
public final class BeanUtils {

    private BeanUtils() {

    }

    public static BeanUtils newInstance() {
        return new BeanUtils();
    }

    @SuppressWarnings("nls")
    public Object getPropertyValue(Object bean, String property)
            throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        if (bean == null) {
            throw new IllegalArgumentException("No bean specified");
        }
        if (property == null) {

            throw new IllegalArgumentException("No name specified for bean class '" + bean.getClass() + "'");
        }
        Class<?> beanClass = bean.getClass();
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, property);
        if (propertyDescriptor == null) {
            throw new IllegalArgumentException("No such property " + property + " for " + beanClass + " exists");
        }

        Method readMethod = propertyDescriptor.getReadMethod();
        if (readMethod == null) {
            throw new IllegalStateException("No getter available for property " + property + " on " + beanClass);
        }
        return readMethod.invoke(bean);
    }

    private PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName)
            throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        PropertyDescriptor propertyDescriptor = null;
        for (PropertyDescriptor currentPropertyDescriptor : propertyDescriptors) {
            if (currentPropertyDescriptor.getName().equals(propertyName)) {
                propertyDescriptor = currentPropertyDescriptor;
            }

        }
        return propertyDescriptor;
    }
}
