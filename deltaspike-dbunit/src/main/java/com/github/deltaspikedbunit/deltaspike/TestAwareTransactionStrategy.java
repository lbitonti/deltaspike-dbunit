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

import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.jpa.impl.transaction.EnvironmentAwareTransactionStrategy;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Alternative;


/**
 * Deltaspike transaction strategy that allows rollback policy configuration.
 *
 * @author Luigi Bitonti
 */
@Dependent
@Alternative
public class TestAwareTransactionStrategy extends EnvironmentAwareTransactionStrategy {

    protected boolean rollbackOnly = false;


    public void setRollbackOnly(boolean rollbackOnly) {
        if (!this.rollbackOnly) {
            this.rollbackOnly = rollbackOnly;
        }
    }

    @Override
    protected boolean isRollbackOnly(Transactional transactionalAnnotation) {
        return this.rollbackOnly;
    }

}
