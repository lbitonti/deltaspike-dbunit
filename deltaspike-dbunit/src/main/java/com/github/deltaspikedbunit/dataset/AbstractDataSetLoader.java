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

package com.github.deltaspikedbunit.dataset;

import org.apache.deltaspike.core.util.ClassUtils;
import org.dbunit.dataset.IDataSet;

import java.io.IOException;
import java.net.URL;


/**
 * Abstract data set loader, which provides a basis for concrete implementations of the {@link DataSetLoader} strategy.
 * Provides a <em>Template Method</em> based approach for {@link #loadDataSet(Class, String) loading} data.
 *
 * @author Luigi Bitonti
 * @author Phillip Webb
 *
 */
public abstract class AbstractDataSetLoader implements DataSetLoader {

	/**
	 * Loads a {@link IDataSet dataset} from {@link URL}s obtained from the specified <tt>location</tt>.
	 * <p>
	 * If no resource can be found then <tt>null</tt> will be returned.
	 *
	 * @see #createDataSet(URL)
	 */
	public IDataSet loadDataSet(Class<?> testClass, String location) throws Exception {
        URL resourceUrl = readClassPath(testClass, location);
        if (resourceUrl != null) {
            return createDataSet(resourceUrl);
        }
        return null;
	}


	/**
	 * Creates the {@link IDataSet dataset}
     *
	 * @param resourceUrl an existing resource URL that contains the dataset data
	 * @return a dataset
	 * @throws Exception if the dataset could not be loaded
	 */
	protected abstract IDataSet createDataSet(URL resourceUrl) throws Exception;


	/**
	 * Reads a classpath entry for the given name.
	 *
	 * @param name
	 * @return a URL
	 * @throws IllegalArgumentException
	 */
	private URL readClassPath(final Class<?> testClass, final String name) throws IllegalStateException, IOException {
        URL url = testClass.getResource(name);
        if (url == null) {
            url = getClassLoader(testClass).getResource(name);
        }
        if (url == null) {
            throw new IllegalArgumentException("Resource [" + name + "] relative to [" + testClass.getName() + "] not found.");
        }
        return url;
	}

    /**
     * Gets a classloader suitable to read <quote>resources</quote> for a class.
     *
     * @param testClass
     * @return a ClassLoader
     */
    private ClassLoader getClassLoader(final Class<?> testClass) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        if (loader == null && testClass != null) {
            loader = testClass.getClassLoader();
        }

        if (loader == null) {
            loader = ClassUtils.class.getClassLoader();
        }
        return loader;
    }

}
