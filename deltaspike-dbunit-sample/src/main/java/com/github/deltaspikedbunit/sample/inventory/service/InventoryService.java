package com.github.deltaspikedbunit.sample.inventory.service;

import com.github.deltaspikedbunit.sample.inventory.entity.*;
import com.github.deltaspikedbunit.sample.inventory.repository.*;

import javax.inject.Inject;

import org.apache.deltaspike.jpa.api.transaction.Transactional;

import java.util.List;


@Transactional
public class InventoryService {

    @Inject
    InventoryCategoryRepository inventoryRepository;


    public InventoryCategory getCategory(String name) {
		if (name == null) {
			return null;
		}
		return inventoryRepository.findByName(name);
    }

    public List<InventoryCategory> getAllCategories() {
        return inventoryRepository.findAll();
    }

    public void addCategory(InventoryCategory category) {
		if (category != null) {
			if (category.getCategoryName() == null || category.getCategoryName().trim().length() == 0) {
                throw new IllegalArgumentException("Category name cannot be blank.");
			}
            InventoryCategory existingCategory = inventoryRepository.findByName(category.getCategoryName());
            if (existingCategory != null) {
                throw new IllegalArgumentException("Category name is a duplicate.");
            }
			inventoryRepository.save(category);
		}
    }

}
