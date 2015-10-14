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

import org.dbunit.IOperationListener;
import org.dbunit.database.IDatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Listener for {@link IDatabaseConnection} events which does not close the
 * jdbc connection after setup or teardown. This is useful when the
 * connection is managed somewhere higher up the invocation stack.
 *
 * @author Luigi Bitonti
 */
public class NoOpOperationListener implements IOperationListener {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(NoOpOperationListener.class);


    public void connectionRetrieved(IDatabaseConnection connection) {
        logger.debug("connectionCreated(connection={}) - start", connection);
        // Is by default a no-op
    }

    public void operationSetUpFinished(IDatabaseConnection connection) {
        logger.debug("operationSetUpFinished(connection={}) - start", connection);
        // Is a no-op since the connection is managed by the caller
    }

    public void operationTearDownFinished(IDatabaseConnection connection) {
        logger.debug("operationTearDownFinished(connection={}) - start", connection);
        // Is a no-op since the connection is managed by the caller
    }

}
