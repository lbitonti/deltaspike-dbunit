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

import com.github.deltaspikedbunit.annotation.DatabaseOperation;
import com.github.deltaspikedbunit.annotation.DatabaseSetup;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.annotation.DbUnitConfiguration;
import com.github.deltaspikedbunit.annotation.ExpectedDatabase;
import com.github.deltaspikedbunit.assertion.DatabaseAssertion;
import com.github.deltaspikedbunit.dataset.DataSetLoader;
import com.github.deltaspikedbunit.dataset.DataSetModifier;
import com.github.deltaspikedbunit.dataset.ContentGuessingDatasetLoader;
import com.github.deltaspikedbunit.dataset.FlatXmlDataSetLoader;
import com.github.deltaspikedbunit.operation.DatabaseOperationLookup;
import com.github.deltaspikedbunit.operation.DefaultDatabaseOperationLookup;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.*;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Exposes methods to execute before and after test runs using {@link DatabaseSetup DatabaseSetup},
 * {@link DatabaseTearDown DatabaseTearDown} and {@link ExpectedDatabase ExpectedDatabase}
 * annotations.
 *
 * @author Luigi Bitonti
 */
public class DbUnitRunner {

    private static final Logger LOG = Logger.getLogger(DbUnitRunner.class.getName());

    private IDatabaseConnection dbUnitConnection;


    public DbUnitRunner(IDatabaseConnection dbUnitConnection) {
        this.dbUnitConnection = dbUnitConnection;
    }

    /**
     * Extracts a DBUnit database operation from a given configuration and internal database operation.
     *
     * @param dbUnitConfiguration current configuration
     * @param databaseOperation internal database operation
     * @return a DBUnit database operation. If no internal operation is selected,
     *  a CLEAN_INSERT default operation is returned
     */
    protected org.dbunit.operation.DatabaseOperation dbUnitOperation(DbUnitConfiguration dbUnitConfiguration, DatabaseOperation databaseOperation) {
        if (databaseOperation != null) {
            Class<? extends DatabaseOperationLookup> databaseOperationLookupClass = DefaultDatabaseOperationLookup.class;
            if (dbUnitConfiguration != null) {
                databaseOperationLookupClass = dbUnitConfiguration.databaseOperationLookup();
            }
            DatabaseOperationLookup databaseOperationLookup;
            try {
                databaseOperationLookup = databaseOperationLookupClass.newInstance();
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to create database operation lookup instance for "
                        + databaseOperationLookupClass, e);
            }
            return databaseOperationLookup.get(databaseOperation);
        }
        return org.dbunit.operation.DatabaseOperation.CLEAN_INSERT;
    }

    /**
     * Selects a dataset loader, given a configuration.
     *
     * @param dbUnitConfiguration
     * @return a dataset loader. Default is {@link FlatXmlDataSetLoader} if no configuration available
     */
    protected DataSetLoader dataSetLoader(DbUnitConfiguration dbUnitConfiguration) {
        if (dbUnitConfiguration == null || dbUnitConfiguration.dataSetLoader() == null) {
            return new ContentGuessingDatasetLoader();
        }
        try {
            return dbUnitConfiguration.dataSetLoader().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Loads all datasets related to the db setup stage. Dataset location is relative to the test class.
     *
     * @param testContext current test context
     * @param dbSetup database setup annotation (can be at class or method level)
     * @return the datasets list
     */
    protected List<IDataSet> loadDataSets(DbUnitTestContext testContext, DatabaseSetup dbSetup) {
        return loadDataSets(testContext.getDataSetLoader(), dbSetup.value(), testContext.getTestClass());
    }

    /**
     * Loads all datasets related to the db tear down stage. Dataset location is relative to the test class.
     *
     * @param testContext current test context
     * @param dbTearDown database tear down annotation (can be at class or method level)
     * @return the datasets list
     */
    protected List<IDataSet> loadDataSets(DbUnitTestContext testContext, DatabaseTearDown dbTearDown) {
        return loadDataSets(testContext.getDataSetLoader(), dbTearDown.value(), testContext.getTestClass());
    }

    /**
     * Loads all given datasets relative to their test class location using the selected loader.
     *
     * @param loader
     * @param dataSetLocations
     * @param resourceBase
     * @return the datasets list
     */
    private List<IDataSet> loadDataSets(DataSetLoader loader, String[] dataSetLocations, Class<?> resourceBase) {
        List<IDataSet> datasets = new ArrayList<>();
        if (dataSetLocations != null) {
            for (String value : dataSetLocations) {
                if (value != null) {
                    IDataSet ds = null;
                    try {
                        ds = loader.loadDataSet(resourceBase, value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    datasets.add(ds);
                }
            }
        }
        return datasets;
    }

    /**
     * Verifies expected db content.
     *
     * @param testContext current test context to lookup class and method level annotations
     * @throws Exception
     */
    public void verifyExpected(DbUnitTestContext testContext) throws Exception {
        List<com.github.deltaspikedbunit.annotation.ExpectedDatabase> expectedDatabases = new ArrayList<>();
        com.github.deltaspikedbunit.annotation.ExpectedDatabase classExpectedDb = testContext.getTestClass().getAnnotation(com.github.deltaspikedbunit.annotation.ExpectedDatabase.class);
        if (classExpectedDb != null) {
            expectedDatabases.add(classExpectedDb);
        }
        com.github.deltaspikedbunit.annotation.ExpectedDatabase expectedDb = testContext.getTestMethod().getAnnotation(com.github.deltaspikedbunit.annotation.ExpectedDatabase.class);
        if (expectedDb != null) {
            expectedDatabases.add(expectedDb);
        }
        verifyExpected(testContext, expectedDatabases);
    }

    /**
     * Loads the given dataset relative to its test class location using the selected modifier.
     *
     * @param testContext
     * @param dataSetLocation
     * @param modifier
     * @return
     * @throws Exception
     */
    private IDataSet loadDataset(DbUnitTestContext testContext, String dataSetLocation, DataSetModifier modifier)
            throws Exception {
        DataSetLoader dataSetLoader = testContext.getDataSetLoader();
        if (dataSetLocation != null && dataSetLocation.length() > 0) {
            IDataSet dataSet = dataSetLoader.loadDataSet(testContext.getTestClass(), dataSetLocation);
            dataSet = modifier.modify(dataSet);
            if (dataSet == null) {
                throw new IllegalArgumentException("Unable to load dataset from \"" + dataSetLocation + "\" using " + dataSetLoader.getClass());
            }
            return dataSet;
        }
        return null;
    }

    /**
     * Verifies expected db content. If an exception is found in the test context, from one of the
     * preceding stages, verification is skipped.
     *
     * @param testContext current test context
     * @param annotations expected database annotations. These are verified from last to first and
     *  all the following checks are skipped once an <quote>override</quote> value of true is found.
     * @throws Exception
     */
    protected void verifyExpected(DbUnitTestContext testContext, List<com.github.deltaspikedbunit.annotation.ExpectedDatabase> annotations) throws Exception {
        if (testContext.getTestException() != null) {
            if (LOG.isLoggable(Level.INFO)) {
                LOG.info("Skipping @DatabaseTest expectation due to test exception "
                        + testContext.getTestException().getClass());
            }
            return;
        }
//        DatabaseConnections connections = testContext.getConnections();
        DataSetModifier modifier = getModifier(testContext, annotations);
//        DataSetModifiers modifiers = new DataSetModifiers();
//        for (Class<? extends DataSetModifier> modifierClass : annotation.modifiers()) {
//            modifiers.add(testContext.getTestInstance(), modifierClass);
//        }

        for (int i = annotations.size() - 1; i >= 0; i--) {
            com.github.deltaspikedbunit.annotation.ExpectedDatabase annotation = annotations.get(i);
            String query = annotation.query();
            String table = annotation.table();
            IDataSet expectedDataSet = loadDataset(testContext, annotation.value(), modifier);
            if (expectedDataSet != null) {
                if (LOG.isLoggable(Level.FINE)) {
                    LOG.info("Verifying @DatabaseTest expectation using " + annotation.value());
                }
                DatabaseAssertion assertion = annotation.assertionMode().getDatabaseAssertion();
                if (query != null && query.length() > 0) {
                    if ( table == null || table.length() == 0 ) {
                        throw new IllegalArgumentException("The table name must be specified when using a SQL query");
                    }
                    ITable expectedTable = expectedDataSet.getTable(table);
                    ITable actualTable = dbUnitConnection.createQueryTable(table, query);
                    assertion.assertEquals(expectedTable, actualTable);
                } else if (table != null && table.length() > 0) {
                    ITable actualTable = dbUnitConnection.createTable(table);
                    ITable expectedTable = expectedDataSet.getTable(table);
                    assertion.assertEquals(expectedTable, actualTable);
                } else {
                    IDataSet actualDataSet = dbUnitConnection.createDataSet();
                    assertion.assertEquals(expectedDataSet, actualDataSet);
                }
            }
            if (annotation.override()) {
                // No need to test any more
                return;
            }
        }
    }

    /**
     * Extracts all modifiers given a test context and a list of expectations.
     *
     * @param testContext
     * @param annotations
     * @return
     */
    private DataSetModifier getModifier(DbUnitTestContext testContext, List<com.github.deltaspikedbunit.annotation.ExpectedDatabase> annotations) {
        DataSetModifiers modifiers = new DataSetModifiers();
        for (com.github.deltaspikedbunit.annotation.ExpectedDatabase annotation : annotations) {
            for (Class<? extends DataSetModifier> modifierClass : annotation.modifiers()) {
                modifiers.add(testContext.getTestInstance(), modifierClass);
            }
        }
        return modifiers;
    }

    /**
     * Adds all declared database setup related datasets to a DBUnit database tester.
     *
     * @param testContext the current test context
     * @param dbUnitConfiguration the current test configuration
     * @param databaseTester the DBUnit database tester
     */
    public void prepareTesterSetup(DbUnitTestContext testContext, DbUnitConfiguration dbUnitConfiguration, IDatabaseTester databaseTester) {
        DataSetLoader dataSetLoader = dataSetLoader(dbUnitConfiguration);
        testContext.setDataSetLoader(dataSetLoader);
        List<IDataSet> dataSets = new ArrayList<>();
        DatabaseSetup classDbSetup = testContext.getTestClass().getAnnotation(DatabaseSetup.class);
        databaseTester.setSetUpOperation(org.dbunit.operation.DatabaseOperation.NONE);
        if (classDbSetup != null) {
            dataSets.addAll(loadDataSets(testContext, classDbSetup));
            databaseTester.setSetUpOperation(dbUnitOperation(dbUnitConfiguration, classDbSetup.type()));
        }
        DatabaseSetup dbSetup = testContext.getTestMethod().getAnnotation(DatabaseSetup.class);
        if (dbSetup != null) {
            dataSets.addAll(loadDataSets(testContext, dbSetup));
            databaseTester.setSetUpOperation(dbUnitOperation(dbUnitConfiguration, dbSetup.type()));
        }
        if (dataSets != null && !dataSets.isEmpty()) {
            IDataSet ds = null;
            try {
                ds = new CompositeDataSet(dataSets.toArray(new IDataSet[dataSets.size()]));
            } catch (DataSetException e) {
                throw new RuntimeException(e);
            }
            databaseTester.setDataSet(ds);
        }
    }

    /**
     * Adds all declared database tear down related datasets to a DBUnit database tester.
     *
     * @param testContext the current test context
     * @param dbUnitConfiguration the current test configuration
     * @param databaseTester the DBUnit database tester
     */
    public void prepareTesterTearDown(DbUnitTestContext testContext, DbUnitConfiguration dbUnitConfiguration, IDatabaseTester databaseTester) {
        DataSetLoader tearDownDataSetLoader = dataSetLoader(dbUnitConfiguration);
        testContext.setDataSetLoader(tearDownDataSetLoader);
        List<IDataSet> tearDownDataSets = new ArrayList<>();
        DatabaseTearDown classDbTearDown = testContext.getTestClass().getAnnotation(DatabaseTearDown.class);
        if (classDbTearDown != null) {
            tearDownDataSets.addAll(loadDataSets(testContext, classDbTearDown));
            databaseTester.setTearDownOperation(dbUnitOperation(dbUnitConfiguration, classDbTearDown.type()));
        }
        DatabaseTearDown dbTearDown = testContext.getTestMethod().getAnnotation(DatabaseTearDown.class);
        if (dbTearDown != null) {
            tearDownDataSets.addAll(loadDataSets(testContext, dbTearDown));
            databaseTester.setTearDownOperation(dbUnitOperation(dbUnitConfiguration, dbTearDown.type()));
        }
        if (tearDownDataSets != null && !tearDownDataSets.isEmpty()) {
            IDataSet ds = null;
            try {
                ds = new CompositeDataSet(tearDownDataSets.toArray(new IDataSet[tearDownDataSets.size()]));
            } catch (DataSetException e) {
                throw new RuntimeException(e);
            }
            databaseTester.setDataSet(ds);
        }
    }

}
