package com.github.deltaspikedbunit.sample.inventory.entity;

import javax.persistence.*;


/**
 * Inventory item entity used for testing. An {@link InventoryCategory}
 * has an arbitrary number of items.
 *
 * @author Luigi Bitonti
 */
@Entity
@Table(name = "inventory_item")
public class InventoryItem {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;
   @Version
   @Column(name = "version")
   private int version;
   @Column(name = "item_name")
   private String itemName;
   @Column(name = "item_description")
   private String itemDescription;
   @ManyToOne
   @JoinColumn(name = "category_id")
   InventoryCategory category;

   public InventoryItem(){}

   public InventoryItem(String itemName, String itemDescription) {
      this.itemName = itemName;
      this.itemDescription = itemDescription;
   }

   public String getItemName() {
      return itemName;
   }
   public void setItemName(String name) {
      this.itemName = name;
   }

   public String getItemDescription() {
      return itemDescription;
   }
   public void setItemDescription(String description) {
      this.itemDescription = description;
   }

   public int getVersion() {
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

   public InventoryCategory getCategory() {
      return category;
   }
   public void setCategory(InventoryCategory category) {
      this.category = category;
   }

}
