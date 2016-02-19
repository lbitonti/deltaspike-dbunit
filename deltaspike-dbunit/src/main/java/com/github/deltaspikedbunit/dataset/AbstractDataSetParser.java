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

import org.dbunit.dataset.*;
import org.dbunit.dataset.datatype.DataType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;


/**
 * DBUnit DataSet parser for non-xml based datasets. It is similar to the flat XML
 * layout, but has some improvements (columns are calculated by parsing the
 * entire dataset, not just the first row).
 *
 * @author Luigi Bitonti
 * @author Lieven DOCLO
 */
abstract class AbstractDataSetParser {

    /**
     * Parses a dataset file and returns the list of DBUnit tables
     * contained in that file
     *
     * @param file A dataset file
     * @return A list of DBUnit tables
     */
    public List<ITable> getTables(File file) {
        try {
            return getTables(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * Parses a dataset input stream and returns the list of DBUnit
     * tables contained in that input stream
     *
     * @param iStream A dataset input stream
     * @return A list of DBUnit tables
     */
    public abstract List<ITable> getTables(InputStream iStream);

    /**
     * Parses a dataset input stream and returns the list of DBUnit
     * tables contained in that input stream
     *
     * @param rows A dataset rows represented as a list of maps.
     *  Each map in the list should have a field name as key and field value as value
     * @return A list of DBUnit tables
     */
    @SuppressWarnings("unchecked")
    public ITable processTable(String tableName, List<Map<String, Object>> rows) {
        ITableMetaData meta = getMetaData(tableName, rows);
        // create a table based on the metadata
        DefaultTable table = new DefaultTable(meta);
        int rowIndex = 0;
        // iterate through the rows and fill the table
        for (Map<String, Object> row : rows) {
            fillRow(table, row, rowIndex++);
        }
        return table;
    }

    /**
     * Gets the table meta data based on the rows for a table
     *
     * @param tableName
     *            The name of the table
     * @param rows
     *            The rows of the table
     * @return The table metadata for the table
     */
    private ITableMetaData getMetaData(String tableName,
                                       List<Map<String, Object>> rows) {
        Set<String> columns = new LinkedHashSet<String>();
        // iterate through the dataset and add the column names to a set
        for (Map<String, Object> row : rows) {
            for (Map.Entry<String, Object> column : row.entrySet()) {
                columns.add(column.getKey());
            }
        }
        List<Column> list = new ArrayList<Column>(columns.size());
        // create a list of DBUnit columns based on the column name set
        for (String s : columns) {
            list.add(new Column(s, DataType.UNKNOWN));
        }
        return new DefaultTableMetaData(tableName,
                list.toArray(new Column[list.size()]));
    }

    /**
     * Fill a table row
     *
     * @param table
     *            The table to be filled
     * @param row
     *            A map containing the column values
     * @param rowIndex
     *            The index of the row to te filled
     */
    private void fillRow(DefaultTable table, Map<String, Object> row,
                         int rowIndex) {
        try {
            table.addRow();
            // set the column values for the current row
            for (Map.Entry<String, Object> column : row.entrySet()) {
                table.setValue(rowIndex, column.getKey(), column.getValue());

            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
