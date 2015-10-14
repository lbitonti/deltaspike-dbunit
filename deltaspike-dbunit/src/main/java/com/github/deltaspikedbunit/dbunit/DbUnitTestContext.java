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

import java.lang.reflect.Method;

import com.github.deltaspikedbunit.dataset.DataSetLoader;
import com.github.deltaspikedbunit.operation.DatabaseOperationLookup;
import org.dbunit.dataset.IDataSet;


/**
 * Provides context for a test.
 *
 * @author Luigi Bitonti
 * @author Phillip Webb
 */
public class DbUnitTestContext {

	private DataSetLoader dataSetLoader;
	private Class<?> testClass;
	private Method testMethod;
	private Object testInstance;
	private Throwable testException;


	public DbUnitTestContext() {
	}
	public DbUnitTestContext(Class<?> testClass, Method testMethod) {
		this.testClass = testClass;
		this.testMethod = testMethod;
	}


	/**
	 * Returns the {@link DataSetLoader} that should be used to load {@link IDataSet}s.
	 * @return The dataset loader
	 */
	public DataSetLoader getDataSetLoader() {
		return dataSetLoader;
	}
	public void setDataSetLoader(DataSetLoader dataSetLoader) {
		this.dataSetLoader = dataSetLoader;
	}

	/**
	 * Returns the {@link DatabaseOperationLookup} that should be used to lookup database operations.
	 * @return the database operation lookup
	 */
//	DatabaseOperationLookup getDatabaseOperationLookup();

	/**
	 * Returns the class that is under test.
	 * @return The class under test
	 */
	public Class<?> getTestClass() {
		return testClass;
	}
	public void setTestClass(Class<?> testClass) {
		this.testClass = testClass;
	}

	/**
	 * Returns the instance that is under test.
	 * @return The instance under test
	 */
	public Object getTestInstance() {
		return testInstance;
	}
	public void setTestInstance(Object testInstance) {
		this.testInstance = testInstance;
	}

	/**
	 * Returns the method that is under test.
	 * @return The method under test
	 */
	public Method getTestMethod() {
		return testMethod;
	}
	public void setTestMethod(Method testMethod) {
		this.testMethod = testMethod;
	}

	/**
	 * Returns any exception that was thrown during the test or <tt>null</tt> if no test exception occurred.
	 * @return the test exception or <tt>null</tt>
	 */
	public Throwable getTestException() {
		return testException;
	}
	public void setTestException(Throwable testException) {
		this.testException = testException;
	}

}
