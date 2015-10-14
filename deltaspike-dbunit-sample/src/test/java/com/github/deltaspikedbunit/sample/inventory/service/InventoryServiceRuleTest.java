package com.github.deltaspikedbunit.sample.inventory.service;

import com.github.deltaspikedbunit.annotation.DatabaseOperation;
import com.github.deltaspikedbunit.annotation.DatabaseSetup;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.annotation.ExpectedDatabase;
import com.github.deltaspikedbunit.assertion.DatabaseAssertionMode;
import com.github.deltaspikedbunit.dbunit.DbUnitTestRule;
import com.github.deltaspikedbunit.sample.inventory.Constants;
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryCategory;
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryItem;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;


@RunWith(CdiTestRunner.class)
public class InventoryServiceRuleTest {

    @Inject
    private InventoryService inventoryService;

    @Rule
    public DbUnitTestRule dbUnit =
	    new DbUnitTestRule(this, Constants.JDBC_DRIVER_CLASS, Constants.DB_CONNECTION_URL,
                           Constants.DB_USER, Constants.DB_PASSWORD);

    @Test
    @DatabaseSetup("../setup-db.xml")
    @ExpectedDatabase(value = "../setup-db.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("../reset-db.xml")
    public void getCategoryShouldFindDefaultOne() {
	    InventoryCategory category = inventoryService.getCategory("default_category");
        Assert.assertEquals("default description", category.getCategoryDescription());
    }

    @Test
    @DatabaseSetup(value = {"../setup-db.xml","extra_category_with_items.xml"}, type = DatabaseOperation.INSERT)
    @ExpectedDatabase(value = "extra_and_default_category-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("../reset-db.xml")
    public void getCategoryAfterExtraSetupShouldFindBoth() {
        InventoryCategory category = inventoryService.getCategory("default_category");
        Assert.assertEquals("default description", category.getCategoryDescription());
        category = inventoryService.getCategory("extra_category");
        Assert.assertEquals("extra description", category.getCategoryDescription());
    }

    @Test
    @DatabaseSetup(value = "../setup-db.xml", type = DatabaseOperation.CLEAN_INSERT)
    @ExpectedDatabase(value = "extra_and_default_category-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("../reset-db.xml")
    public void getCategoriesAfterExtraAddShouldFindBoth() {
        List<InventoryCategory> categories = inventoryService.getAllCategories();
        Assert.assertEquals(1, categories.size());
        Assert.assertEquals("default description", categories.get(0).getCategoryDescription());

        InventoryCategory category = new InventoryCategory("extra_category", "extra description");
        InventoryItem item1 = new InventoryItem("extra_item_1", "extra desc 1");
        InventoryItem item2 = new InventoryItem("extra_item_2", "extra desc 2");
        category.addItems(item1, item2);
        inventoryService.addCategory(category);

        categories = inventoryService.getAllCategories();
        Assert.assertEquals(2, categories.size());
    }

}
