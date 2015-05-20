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

import javax.validation.Validator;

/**
 *Validation manager which is responsible for providing instance of {@link Validator} based on the underlying validation provider.
 *For any JSR303 complaint implementation,{@link Validator} should be implemented in thread safe way.
 *
 * @author Umesh Awasthi
 */
public interface JSR303ValidationManager {

    Validator getValidator();
}
