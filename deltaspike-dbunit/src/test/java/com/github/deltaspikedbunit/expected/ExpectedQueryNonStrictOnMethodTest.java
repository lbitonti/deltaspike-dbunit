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

package com.github.deltaspikedbunit.expected;

import com.github.deltaspikedbunit.annotation.ExpectedDatabase;
import com.github.deltaspikedbunit.assertion.DatabaseAssertionMode;
import com.github.deltaspikedbunit.deltaspike.DatabaseTest;
import com.github.deltaspikedbunit.sample.inventory.repository.InventoryCategoryRepository;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;


@RunWith(CdiTestRunner.class)
@DatabaseTest
public class ExpectedQueryNonStrictOnMethodTest {

	@Test
	@ExpectedDatabase(value = "../expected_query_nonstrict.xml", assertionMode = DatabaseAssertionMode.NON_STRICT,
					  query = "select * from inventory_item where id in (1900,1901)", table = "inventory_item")
	public void test() throws Exception {
	}

}
