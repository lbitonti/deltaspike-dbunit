package com.github.deltaspikedbunit.sample.inventory.entity;

import java.util.*;

import javax.persistence.*;


/**
 * Inventory category. A category can include multiple {@link InventoryItem}s.
 *
 * @author Luigi Bitonti
 */
@Entity
@Table(name = "inventory_category")
public class InventoryCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Version
    @Column(name = "version")
    private int version;
    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;
    @Column(name = "category_description")
    private String categoryDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<InventoryItem> items = new HashSet<>();


    public InventoryCategory() {
    }

    public InventoryCategory(String categoryName, String categoryDescription) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }


    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String name) {
        this.categoryName = name;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }
    public void setCategoryDescription(String description) {
        this.categoryDescription = description;
    }

    int getVersion() {
        return version;
    }
    void setVersion(int version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }
    void setId(int id) {
        this.id = id;
    }

    public Set<InventoryItem> getItems() {
        return items;
    }
    void setItems(Set<InventoryItem> items) {
        this.items = items;
    }

    public void addItem(InventoryItem item) {
        if (item != null) {
            this.items.add(item);
            item.setCategory(this);
        }
    }

    public void addItems(InventoryItem... items) {
        if (items != null) {
            for (InventoryItem ii : items) {
                addItem(ii);
            }
        }
    }

}
