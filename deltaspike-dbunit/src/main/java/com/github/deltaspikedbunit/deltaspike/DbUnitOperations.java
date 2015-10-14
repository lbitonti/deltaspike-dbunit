/*
 * Copyright 2002-2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.deltaspikedbunit.deltaspike;

import com.github.deltaspikedbunit.annotation.DatabaseSetup;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.annotation.ExpectedDatabase;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;


/**
 * Indicates that a test will execute (if requested) database setup, teardown and
 * verification operations. These operations are specified using the
 * {@link DatabaseSetup DatabaseSetup}, {@link DatabaseTearDown DatabaseTearDown}
 * and {@link ExpectedDatabase ExpectedDatabase} annotations.
 *
 * @author Luigi Bitonti
 */
@InterceptorBinding
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DbUnitOperations {
}
