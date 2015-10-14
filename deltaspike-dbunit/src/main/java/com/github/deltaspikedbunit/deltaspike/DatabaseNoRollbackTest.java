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

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.*;


/**
 * Indicates that a test is transactional as per {@link TransactionalTest} and
 * also allows database setup, teardown and verification as per {@link DatabaseTest}.
 * The transaction is committed at the end of the test method.
 *
 * @author Luigi Bitonti
 */
@TransactionalTest(rollback = false)
@DbUnitOperations
@InterceptorBinding
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DatabaseNoRollbackTest {
}
