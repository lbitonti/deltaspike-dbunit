package com.github.deltaspikedbunit.sample.inventory.repository;

import com.github.deltaspikedbunit.sample.inventory.entity.*;
import org.apache.deltaspike.data.api.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Inventory category repository used for integration testing.
 *
 * @author Luigi Bitonti
 */
@Repository
public abstract class InventoryCategoryRepository extends AbstractEntityRepository<InventoryCategory, Integer> {

    @Query(value = "select ic from InventoryCategory ic where ic.categoryName = :categoryName", 
	   singleResult = SingleResultType.OPTIONAL)
    public abstract InventoryCategory findByName(@QueryParam("categoryName") String categoryName);

    @Query(value = "select ic from InventoryCategory ic join fetch ic.items where ic.categoryName = :categoryName",
            singleResult = SingleResultType.OPTIONAL)
    public abstract InventoryCategory findByNameWithItems(@QueryParam("categoryName") String categoryName);

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();
        List<InventoryCategory> categories = findAll();
        if (categories != null) {
            for (InventoryCategory ic : categories) {
                names.add(ic.getCategoryName());
            }
        }
        return names;
    }

}
