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

package com.github.deltaspikedbunit.annotation;

import com.github.deltaspikedbunit.dataset.DataSetLoader;
import com.github.deltaspikedbunit.dataset.DefaultDatasetLoader;
import com.github.deltaspikedbunit.operation.DatabaseOperationLookup;
import com.github.deltaspikedbunit.operation.DefaultDatabaseOperationLookup;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.dbunit.dataset.IDataSet;


/**
 * Configures a test run.
 *
 * @author Luigi Bitonti
 * @author Phillip Webb
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DbUnitConfiguration {

	/**
	 * Returns the class that will be used to load {@link IDataSet} resources. The specified class must implement
	 * {@link DataSetLoader} and must have a default constructor.
	 * @return the data set loader class
	 */
	Class<? extends DataSetLoader> dataSetLoader() default DefaultDatasetLoader.class;

	/**
	 * Returns the class that will be used to lookup DBUnit database operations. The specific class must implement
	 * {@link DatabaseOperationLookup} and must have a default constructor.
	 * @return the database operation lookup
	 */
	Class<? extends DatabaseOperationLookup> databaseOperationLookup() default DefaultDatabaseOperationLookup.class;

}
