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

package com.github.deltaspikedbunit.teardown;

import com.github.deltaspikedbunit.annotation.DatabaseOperation;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.deltaspike.DatabaseNoRollbackTest;
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryCategory;
import com.github.deltaspikedbunit.sample.inventory.repository.InventoryCategoryRepository;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.*;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


@RunWith(CdiTestRunner.class)
@DatabaseNoRollbackTest
@DatabaseTearDown(value = "../setup-db.xml", type = DatabaseOperation.INSERT)
public class InsertTearDownOnClassTest {

    @Inject
    private InventoryCategoryRepository inventoryRepository;

    @Test
    public void test() throws Exception {
        List<InventoryCategory> categories = inventoryRepository.findAll();
        Assert.assertEquals(1, categories.size());
        Assert.assertEquals("existing_category", categories.get(0).getCategoryName());
    }

    @After
    public void afterTest() throws Exception {
        List<String> names = inventoryRepository.findAllNames();
        Assert.assertEquals(2, names.size());
        assertThat(names, contains("existing_category", "default_category"));
    }

}
