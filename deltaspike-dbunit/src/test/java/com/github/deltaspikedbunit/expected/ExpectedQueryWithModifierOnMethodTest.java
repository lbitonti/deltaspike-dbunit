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
import com.github.deltaspikedbunit.dataset.ReplacementDataSetModifier;
import com.github.deltaspikedbunit.deltaspike.DatabaseTest;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.dbunit.dataset.ReplacementDataSet;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(CdiTestRunner.class)
@DatabaseTest
@ExpectedDatabase(modifiers = ExpectedQueryWithModifierOnMethodTest.OuterModifier.class)
public class ExpectedQueryWithModifierOnMethodTest {

	@Test
	@ExpectedDatabase(value = "../expected_query_modified.xml", modifiers = InnerModifier.class,
					  query = "select * from inventory_item where id in (1900,1901)", table = "inventory_item")
	public void test() throws Exception {
	}

	private class InnerModifier extends ReplacementDataSetModifier {

		@Override
		protected void addReplacements(ReplacementDataSet dataSet) {
			dataSet.addReplacementSubstring("#", "");
		}

	}

	static class OuterModifier extends ReplacementDataSetModifier {

		@Override
		protected void addReplacements(ReplacementDataSet dataSet) {
			dataSet.addReplacementSubstring("!", "");
		}

	}

}
