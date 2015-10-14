package com.github.deltaspikedbunit.sample.inventory.service;

import com.github.deltaspikedbunit.annotation.DatabaseSetup;
import com.github.deltaspikedbunit.annotation.DatabaseTearDown;
import com.github.deltaspikedbunit.annotation.ExpectedDatabase;
import com.github.deltaspikedbunit.assertion.DatabaseAssertionMode;
import com.github.deltaspikedbunit.deltaspike.DatabaseNoRollbackTest;
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryCategory;
import com.github.deltaspikedbunit.sample.inventory.entity.InventoryItem;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;


@RunWith(CdiTestRunner.class)
@DatabaseNoRollbackTest
@DatabaseSetup("../setup-db.xml")
public class InventoryServiceNoRollbackTest {

    @Inject
    private InventoryService inventoryService;
    

    @Test
    @ExpectedDatabase(value = "../setup-db.xml", assertionMode = DatabaseAssertionMode.NON_STRICT)
    @DatabaseTearDown("../reset-db.xml")
    public void getCategoryShouldFindDefaultOne() {
	    InventoryCategory category = inventoryService.getCategory("default_category");
	    Assert.assertEquals("default description", category.getCategoryDescription());
    }

    @Test
    @DatabaseSetup("extra_category_with_items.xml")
    @ExpectedDatabase(value = "extra_and_default_category-expected.xml", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED)
    @DatabaseTearDown("../reset-db.xml")
    public void getCategoryAfterExtraSetupShouldFindBoth() {
        InventoryCategory category = inventoryService.getCategory("default_category");
        Assert.assertEquals("default description", category.getCategoryDescription());
        category = inventoryService.getCategory("extra_category");
        Assert.assertEquals("extra description", category.getCategoryDescription());
    }

    @Test
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
