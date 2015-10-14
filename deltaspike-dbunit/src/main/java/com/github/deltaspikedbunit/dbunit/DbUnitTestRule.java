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

package com.github.deltaspikedbunit.dbunit;

import com.github.deltaspikedbunit.annotation.DbUnitConfiguration;
import com.github.deltaspikedbunit.annotation.DatabaseSetup;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.annotation.ExpectedDatabase;
import org.dbunit.*;
import org.dbunit.database.IDatabaseConnection;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.Connection;


/**
 * JUnit rule to:
 * <ul>
 *  <li>set up a database before test execution through {@link DatabaseSetup}</li>
 *  <li>verify database content after test execution through {@link ExpectedDatabase}</li>
 *  <li>leave a database in a known state after test execution through {@link DatabaseTearDown}</li>
 * </ul>
 *
 * This is meant to replace {@link com.github.deltaspikedbunit.deltaspike.DatabaseTest} and
 * {@link com.github.deltaspikedbunit.deltaspike.DatabaseNoRollbackTest} when a test cannot
 * run in the same transaction, or only just wrapped by the same interceptor.
 *
 * NOTE: this uses a separate connection (and transaction), from the one used by the test itself
 * for setup, tear down and checking expected database state. I consider it experimental for now,
 * even if it works in my tests.
 *
 * @author Luigi Bitonti
 */
public class DbUnitTestRule implements TestRule {

    private IDatabaseTester databaseTester;
    private IDatabaseConnection dbUnitConnection;

    private Connection connection;
    private DbUnitRunner dbUnitRunner;
    private Object testInstance;


    public DbUnitTestRule(Object testInstance, DataSource dataSource) {
        try {
            databaseTester = new DataSourceDatabaseTester(dataSource);
            dbUnitConnection = databaseTester.getConnection();
            connection = dbUnitConnection.getConnection();
            dbUnitRunner = new DbUnitRunner(dbUnitConnection);
            this.testInstance = testInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public DbUnitTestRule(Object testInstance, Class<?> driver, String url, String user, String password) {
        try {
            databaseTester = new JdbcDatabaseTester(driver.getName(), url, user, password);
            databaseTester.setOperationListener(new DefaultOperationListener());
            dbUnitConnection = databaseTester.getConnection();
            connection = dbUnitConnection.getConnection();
            dbUnitRunner = new DbUnitRunner(dbUnitConnection);
            this.testInstance = testInstance;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Statement apply(final Statement base, final Description description) {

        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Class<?> testClass = description.getTestClass();

                DbUnitTestContext testContext = new DbUnitTestContext();
                testContext.setTestClass(testClass);
                testContext.setTestInstance(testInstance);
                testContext.setTestMethod(getMethod(testClass, description.getMethodName()));

                DbUnitConfiguration dbUnitConfiguration = testClass.getAnnotation(DbUnitConfiguration.class);
                try {
                    dbUnitRunner.prepareTesterSetup(testContext, dbUnitConfiguration, databaseTester);
                    databaseTester.onSetup();
                    if (!connection.getAutoCommit()) {
                        connection.commit();
                    }
                    base.evaluate();

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
            }
        };

    }


    private Method getMethod(Class<?> testClass, String methodName) {
        Method method = null;
        if (testClass != null && methodName != null) {
            try {
                method = testClass.getMethod(methodName);
            } catch (SecurityException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return method;
    }

}
