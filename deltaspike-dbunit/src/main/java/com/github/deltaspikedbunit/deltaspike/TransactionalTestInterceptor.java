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

import org.apache.deltaspike.core.util.AnnotationUtils;
import org.apache.deltaspike.jpa.spi.transaction.TransactionStrategy;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.io.Serializable;


/**
 * Intercepts test method calls and executes them within a  transaction
 * (i.e. the test method demarcates the transaction boundaries). The transaction
 * can be rolled back at the end of the test method run (default is to rollback).
 *
 * @author Luigi Bitonti
 */
@Interceptor
@TransactionalTest
public class TransactionalTestInterceptor implements Serializable {

    @Inject
    private TransactionStrategy transactionStrategy;

    @Inject
    private BeanManager beanManager;


    @AroundInvoke
    public Object executeInTransaction(InvocationContext invocationContext) throws Exception {
        boolean rollbackOnly = isRollbackOnly(invocationContext);
        if (transactionStrategy instanceof TestAwareTransactionStrategy) {
            ((TestAwareTransactionStrategy)transactionStrategy).setRollbackOnly(rollbackOnly);
        }

        return transactionStrategy.execute(invocationContext);
    }

    protected boolean isRollbackOnly(InvocationContext context) {
        Class targetClass = (context.getTarget() != null ? context.getTarget().getClass() : context.getMethod().getDeclaringClass());
        DatabaseNoRollbackTest databaseNoRollbackTest =
                AnnotationUtils.extractAnnotationFromMethodOrClass(beanManager, context.getMethod(), targetClass, DatabaseNoRollbackTest.class);
        if (databaseNoRollbackTest != null) {
            return false;
        }
        TransactionalTest transactionalTest =
            AnnotationUtils.extractAnnotationFromMethodOrClass(beanManager, context.getMethod(), targetClass, TransactionalTest.class);
        if (transactionalTest != null) {
            return transactionalTest.rollback();
        }
        // default is true
        return  true;
    }

}
