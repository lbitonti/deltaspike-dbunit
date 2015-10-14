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

package com.github.deltaspikedbunit.testutils;

import org.apache.deltaspike.core.util.AnnotationUtils;
import org.junit.Assert;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;


@Interceptor
@ExpectedFailure
public class ExpectedFailureInterceptor implements Serializable {

    @Inject
    private BeanManager beanManager;


    @AroundInvoke
    public Object checkFailure(InvocationContext invocationContext) throws Exception {
        Class targetClass = (invocationContext.getTarget() != null ? invocationContext.getTarget().getClass() : invocationContext.getMethod().getDeclaringClass());
        ExpectedFailure expectedFailure =
                AnnotationUtils.extractAnnotationFromMethodOrClass(beanManager, invocationContext.getMethod(), targetClass, ExpectedFailure.class);
        Object returnValue = null;
        try {
            returnValue = invocationContext.proceed();
        } catch (Throwable e) {
            if (expectedFailure != null && expectedFailure.failure().equals(e.getClass())) {
                return returnValue;
            }
            else {
                Assert.fail("Expected Failure [" + (expectedFailure != null ? expectedFailure.failure() : "null") +
                            "], but was [" + e.getClass() + "] - [" + e.getMessage() + "]");
            }
        }
        Assert.fail("Expected Failure [" + (expectedFailure != null ? expectedFailure.failure() : "null") +
                "], but was [no failure recorded]");
        return returnValue;
    }

}
