package com.github.deltaspikedbunit.dataset;

import org.dbunit.dataset.IDataSet;

import java.net.URL;

/**
 * Created by pestano on 16/02/16.
 */
public class YamlDataSetLoader extends AbstractDataSetLoader {

    @Override
    protected IDataSet createDataSet(URL resourceUrl) throws Exception {
        return new YamlDataSet(resourceUrl.openStream());
    }
}
