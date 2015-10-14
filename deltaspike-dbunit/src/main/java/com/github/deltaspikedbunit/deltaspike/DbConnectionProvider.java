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

import javax.persistence.EntityManager;
import java.sql.Connection;

import org.hibernate.Session;
import org.hibernate.internal.SessionImpl;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Provides utility methods related to DB connections.
 *
 * @author Luigi Bitonti
 */
public class DbConnectionProvider {

    private static final Logger LOG = Logger.getLogger(DbConnectionProvider.class.getName());

    /**
     * Unwraps db connection from entity manager
     *
     * @param em
     * @return connection
     */
    public static Connection unwrap(EntityManager em) {
        Connection connection = null;
        if (em == null) {
            throw new IllegalStateException("Entity Manager == null");
        }

        // Try to unwrap connection from entity manager (Eclipselink and OpenJPA)
        try {
            connection = em.unwrap(Connection.class);
        } catch (Exception e) {
            if (LOG.isLoggable(Level.FINER)) {
                LOG.log(Level.FINER, e.getMessage(), e);
            }
            connection = null;
        }
        // Otherwise try Hibernate 4.x
        if (connection == null) {
            try {
                Session session = em.unwrap(Session.class);
                SessionImpl sessionImpl = (SessionImpl)session;
                connection = sessionImpl.connection();
            } catch (Exception e) {
                if (LOG.isLoggable(Level.FINER)) {
                    LOG.log(Level.FINER, e.getMessage(), e);
                }
                connection = null;
            }
        }

        return connection;
    }

}
