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

import org.codehaus.jackson.map.ObjectMapper;
import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * DBUnit DataSet format for JSON based datasets. It is similar to the flat XML
 * layout, but has some improvements (columns are calculated by parsing the
 * entire dataset, not just the first row). It uses Jackson, a fast JSON
 * processor.
 *
 * @author Luigi Bitonti
 * @author Lieven DOCLO
 */
public class JsonDataSet extends AbstractDataSet {
	// The parser for the dataset JSON file
	private JSONITableParser tableParser = new JSONITableParser();

	private final ObjectMapper mapper = new ObjectMapper();

	// The tables after parsing
	private List<ITable> tables;

	/**
	 * Creates a JSON dataset based on a file
	 *
	 * @param file A JSON dataset file
	 */
	public JsonDataSet(File file) {
		tables = tableParser.getTables(file);
	}

	/**
	 * Creates a JSON dataset based on an inputstream
	 *
	 * @param is An inputstream pointing to a JSON dataset
	 */
	public JsonDataSet(InputStream is) {
		tables = tableParser.getTables(is);
	}

	@Override
	protected ITableIterator createIterator(boolean reverse)
			throws DataSetException {
		return new DefaultTableIterator(
				tables.toArray(new ITable[tables.size()]));
	}


	private class JSONITableParser extends AbstractDataSetParser{

		/**
		 * Parses a JSON dataset input stream and returns the list of DBUnit
		 * tables contained in that input stream
		 *
		 * @param iStream A JSON dataset input stream
		 * @return A list of DBUnit tables
		 */
		@SuppressWarnings("unchecked")
		@Override
		public List<ITable> getTables(InputStream iStream) {
			List<ITable> tables = new ArrayList<ITable>();

			try {
				// get the base object tree from the JSON stream
				Map<String, Object> dataset = mapper.readValue(iStream, Map.class);
				// iterate over the tables in the object tree
				for (Map.Entry<String, Object> entry : dataset.entrySet()) {
					// get the rows for the table
					List<Map<String, Object>> rows = (List<Map<String, Object>>) entry.getValue();
					ITable table = processTable(entry.getKey(), rows);
					// add the table to the list of DBUnit tables
					tables.add(table);
				}

			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
			return tables;
		}
	}

}
