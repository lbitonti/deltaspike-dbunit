/*
 * Copyright 2002-2016 the original author or authors
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

import org.dbunit.dataset.*;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.*;


/**
 * DBUnit DataSet format for Yaml based datasets. It is similar to the flat XML
 * layout, but has some improvements (columns are calculated by parsing the
 * entire dataset, not just the first row). It uses Snakeyaml as yaml processor.
 *
 * @author Luigi Bitonti
 * @author Lieven DOCLO
 */
public class YamlDataSet extends AbstractDataSet {
    // The parser for the dataset Yaml file
    private YamlITableParser tableParser = new YamlITableParser();

    // The tables after parsing
    private List<ITable> tables;

    /**
     * Creates a Yaml dataset based on a file
     *
     * @param file A Yaml dataset file
     */
    public YamlDataSet(File file) {
        tables = tableParser.getTables(file);
    }

    /**
     * Creates a Yaml dataset based on an inputstream
     *
     * @param is An inputstream pointing to a Yaml dataset
     */
    public YamlDataSet(InputStream is) {
        tables = tableParser.getTables(is);
    }

    @Override
    protected ITableIterator createIterator(boolean reverse)
            throws DataSetException {
        return new DefaultTableIterator(
                tables.toArray(new ITable[tables.size()]));
    }

    class YamlITableParser extends AbstractDataSetParser {
        @Override
        public List<ITable> getTables(InputStream iStream) {
            List<ITable> tables = new ArrayList<>();
            Map<String, List<Map<String, Object>>> data =
                    (Map<String, List<Map<String, Object>>>) new Yaml().load(iStream);

            for (Map.Entry<String, List<Map<String, Object>>> entry : data.entrySet()) {
                ITable table = processTable(entry.getKey(), entry.getValue());
                tables.add(table);
            }
            return tables;
        }
    }

}
