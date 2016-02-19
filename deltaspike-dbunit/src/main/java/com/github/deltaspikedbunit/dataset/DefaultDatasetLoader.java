package com.github.deltaspikedbunit.dataset;
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
import org.dbunit.dataset.IDataSet;

import java.net.URL;

/**
 * This loader tried to infer dataset loder using dataset name extension.
 *
 * Created by pestano on 19/02/16.
 */
public class DefaultDatasetLoader extends AbstractDataSetLoader {

    @Override
    protected IDataSet createDataSet(URL resourceUrl) throws Exception {
        String uri = resourceUrl.toString();
        String extension = uri.substring(uri.lastIndexOf(".")+1).toLowerCase();
        IDataSet iDataSet = null;
        switch (extension) {
            case "xml": {
                iDataSet = new FlatXmlDataSetLoader().createDataSet(resourceUrl);
                break;
            }
            case "yml": {
                iDataSet = new YamlDataSetLoader().createDataSet(resourceUrl);
                break;
            }
            case "json": {
                iDataSet = new JsonDataSetLoader().createDataSet(resourceUrl);
                break;
            }
            default: {
                throw new RuntimeException(String.format("Dataset format %s not supported",extension));
            }
        }
        return iDataSet;

    }
}
