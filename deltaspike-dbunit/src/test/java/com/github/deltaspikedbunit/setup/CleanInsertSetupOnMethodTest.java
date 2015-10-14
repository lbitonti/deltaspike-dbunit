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
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryCategory;
import com.github.deltaspikedbunit.sample.inventory.repository.InventoryCategoryRepository;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


@RunWith(CdiTestRunner.class)
@DatabaseTest
public class CleanInsertSetupOnMethodTest {

	@Inject
	private InventoryCategoryRepository inventoryRepository;

	@Test
	@DatabaseSetup(value = "../setup-db.xml", type = DatabaseOperation.CLEAN_INSERT)
	public void test() throws Exception {
		List<String> names = inventoryRepository.findAllNames();
		assertThat(names, contains("default_category"));
	}

	@Test
	@DatabaseSetup(value = {"../setup-db.xml","../extra_category_with_items.xml"}, type = DatabaseOperation.CLEAN_INSERT)
	public void testAgain() throws Exception {
		List<String> names = inventoryRepository.findAllNames();
		assertThat(names, contains("default_category", "extra_category"));
		InventoryCategory category = inventoryRepository.findByName("default_category");
		Assert.assertEquals(2, category.getItems().size());
		InventoryCategory extraCategory = inventoryRepository.findByName("extra_category");
		Assert.assertEquals(2, extraCategory.getItems().size());
	}

}
