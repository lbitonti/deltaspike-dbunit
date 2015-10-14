package com.github.deltaspikedbunit.sample.inventory.repository;

import com.github.deltaspikedbunit.sample.inventory.entity.*;
import org.apache.deltaspike.data.api.*;


@Repository
public abstract class InventoryCategoryRepository extends AbstractEntityRepository<InventoryCategory, Integer> {

    @Query(value = "select ic from InventoryCategory ic where ic.categoryName = :categoryName", 
	   singleResult = SingleResultType.OPTIONAL)
    public abstract InventoryCategory findByName(@QueryParam("categoryName") String categoryName);

}
