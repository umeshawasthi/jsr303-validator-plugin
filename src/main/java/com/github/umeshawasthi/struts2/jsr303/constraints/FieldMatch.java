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
package com.github.umeshawasthi.struts2.jsr303.constraints;

import com.github.umeshawasthi.struts2.jsr303.constraints.impl.FieldMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 *
 * Validation annotation to validate that 2 fields have the same value.
 * An array of fields and their matching confirmation fields can be supplied.
 *
 * Example, compare 1 pair of fields:
 * <pre>
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match")
 * </pre>
 *
 * Example, compare more than 1 pair of fields:
 * <pre>
 * @FieldMatch.List({
 * @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
 * @FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match")})
 *  </pre>
 * @author Umesh Awasthi
 * @version 1.0
 */

@Constraint(validatedBy = FieldMatchValidator.class)
@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface FieldMatch {
    String message() default "Fields are not matching";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * @return The first field
     */
    String first();

    /**
     * @return The second field
     */
    String second();

    /**
     * Defines several <code>@FieldMatch</code> annotations on the same element
     *
     * @see FieldMatch
     */
    @Target({TYPE, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        FieldMatch[] value();
    }
}
