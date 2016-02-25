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
 * Created by pestano on 19/02/16.
 */
@RunWith(CdiTestRunner.class)
@DatabaseTest
public class DefaultDataSetLoaderTest {

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
