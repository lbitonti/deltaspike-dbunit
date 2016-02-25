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

package com.github.deltaspikedbunit.setup;

import com.github.deltaspikedbunit.annotation.DatabaseOperation;
import com.github.deltaspikedbunit.annotation.DatabaseSetup;
import com.github.deltaspikedbunit.deltaspike.DatabaseTest;
import com.github.deltaspikedbunit.sample.inventory.repository.InventoryCategoryRepository;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


/**
 * @author Rafael Pestano
 */
@RunWith(CdiTestRunner.class)
@DatabaseTest
public class ContentGuessingDataSetLoaderTest {

    @Inject
    private InventoryCategoryRepository inventoryRepository;

    @Test
    @DatabaseSetup(value = "../setup-db.yml", type = DatabaseOperation.CLEAN_INSERT)
    public void shouldUseYamlDataSet(){
        List<String> names = inventoryRepository.findAllNames();
        assertThat(names, contains("default_category"));
    }

    @Test
    @DatabaseSetup(value = "../setup-db.xml", type = DatabaseOperation.CLEAN_INSERT)
    public void shouldUseFlatXmlDataSet(){
        List<String> names = inventoryRepository.findAllNames();
        assertThat(names, contains("default_category"));
    }

    @Test
    @DatabaseSetup(value = "../setup-db.json", type = DatabaseOperation.CLEAN_INSERT)
    public void shouldUseJsonDataSet(){
        List<String> names = inventoryRepository.findAllNames();
        assertThat(names, contains("default_category"));
    }
}
