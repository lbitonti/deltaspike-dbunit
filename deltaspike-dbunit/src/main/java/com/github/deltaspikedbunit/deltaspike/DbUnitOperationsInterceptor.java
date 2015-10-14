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
import com.github.deltaspikedbunit.annotation.DbUnitConfiguration;
import com.github.deltaspikedbunit.annotation.ExpectedDatabase;
import com.github.deltaspikedbunit.dbunit.*;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.deltaspike.core.util.ProxyUtils;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.junit.Test;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.persistence.EntityManager;
import java.io.Serializable;
import java.sql.Connection;
import java.util.*;


/**
 * Intercepts test method calls and executes (if requested) database setup, teardown and
 * verification operations.
 * These operations are specified using the {@link DatabaseSetup DatabaseSetup},
 * {@link DatabaseTearDown DatabaseTearDown} and {@link ExpectedDatabase ExpectedDatabase}
 * annotations.
 *
 * @author Luigi Bitonti
 */
@Interceptor
@DbUnitOperations
public class DbUnitOperationsInterceptor implements Serializable {

    private IDatabaseTester databaseTester;
    private IDatabaseConnection dbUnitConnection;

    private Connection connection;
    private DbUnitRunner dbUnitRunner;

    private EntityManager em;


    protected void dbUnitSetup() {
        List<EntityManager> entityManagerList =
                BeanProvider.getContextualReferences(EntityManager.class, true, false);
        for (EntityManager entityManager : entityManagerList) {
            if (entityManager.getTransaction().isActive()) {
                em = entityManager;
                break;
            }
        }
        if (em == null) {
            throw new IllegalStateException("Entity Manager == null");
        }

        Connection conn = DbConnectionProvider.unwrap(em);
        if (conn == null) {
            throw new IllegalStateException("Connection == null");
        }
        this.connection = conn;

        try {
            databaseTester = new JdbcConnectionDatabaseTester(connection);
            databaseTester.setOperationListener(new NoOpOperationListener());
            dbUnitConnection = databaseTester.getConnection();
            dbUnitRunner = new DbUnitRunner(dbUnitConnection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @AroundInvoke
    public Object execute(InvocationContext invocationContext) throws Exception {

        // Execute following logic only if @Test method
        Test testAnnotation = invocationContext.getMethod().getAnnotation(Test.class);
        if (testAnnotation == null) {
            return invocationContext.proceed();
        }

        dbUnitSetup();
        DbUnitTestContext testContext = new DbUnitTestContext();

        Object returnValue = null;
        Class<?> testClass = ProxyUtils.getUnproxiedClass(invocationContext.getTarget().getClass());
        testContext.setTestClass(testClass);
        testContext.setTestInstance(invocationContext.getTarget());
        testContext.setTestMethod(invocationContext.getMethod());

        DbUnitConfiguration dbUnitConfiguration = testClass.getAnnotation(DbUnitConfiguration.class);
        try {
            dbUnitRunner.prepareTesterSetup(testContext, dbUnitConfiguration, databaseTester);

//            try {
                databaseTester.onSetup();
//            } catch (Throwable e) { // do not propagate db setup exception or transaction will not rollback
//                e.printStackTrace();
//            }

            returnValue = invocationContext.proceed();

            dbUnitRunner.verifyExpected(testContext);
        } finally {
            dbUnitRunner.prepareTesterTearDown(testContext, dbUnitConfiguration, databaseTester);
            databaseTester.onTearDown();
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                connection = null;
            }
        }
        return returnValue;
    }

}
