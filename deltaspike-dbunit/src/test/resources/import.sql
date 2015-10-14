insert into inventory_category(id, version, category_name, category_description) values(1000, 1, 'existing_category', 'existing description');
-- insert into inventory_item(id, version, item_name, item_description, category_id) values(2000, 1, 'existing_item', 'existing item desc', 1000);
insert into inventory_item(id, version, item_name, item_description, category_id) values(1900, 1, 'existing_item', 'existing item desc', 1000);
insert into inventory_item(id, version, item_name, item_description, category_id) values(1901, 1, 'existing_item_other', 'existing item other desc', 1000);
