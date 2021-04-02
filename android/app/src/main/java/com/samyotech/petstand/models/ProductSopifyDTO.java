package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductSopifyDTO implements Serializable {

    String id = "";
    String title = "";
    String body_html = "";
    String vendor = "";
    String product_type = "";
    String created_at = "";
    String handle = "";
    String updated_at = "";
    String published_at = "";
    String template_suffix = "";
    String tags = "";
    String published_scope = "";
    ArrayList<Variants> variants = new ArrayList<>();
    ArrayList<Options> options = new ArrayList<>();
    ArrayList<Images> images = new ArrayList<>();
    Image image = new Image();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody_html() {
        return body_html;
    }

    public void setBody_html(String body_html) {
        this.body_html = body_html;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getTemplate_suffix() {
        return template_suffix;
    }

    public void setTemplate_suffix(String template_suffix) {
        this.template_suffix = template_suffix;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPublished_scope() {
        return published_scope;
    }

    public void setPublished_scope(String published_scope) {
        this.published_scope = published_scope;
    }

    public ArrayList<Variants> getVariants() {
        return variants;
    }

    public void setVariants(ArrayList<Variants> variants) {
        this.variants = variants;
    }

    public ArrayList<Options> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<Options> options) {
        this.options = options;
    }

    public ArrayList<Images> getImages() {
        return images;
    }

    public void setImages(ArrayList<Images> images) {
        this.images = images;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public class Variants implements Serializable {

        String id = "";
        String product_id = "";
        String title = "";
        String price = "";
        String sku = "";
        String position = "";
        String inventory_policy = "";
        String compare_at_price = "";
        String fulfillment_service = "";
        String inventory_management = "";
        String option1 = "";
        String option2 = "";
        String option3 = "";
        String created_at = "";
        String updated_at = "";
        String taxable = "";
        String barcode = "";
        String grams = "";
        String image_id = "";
        String weight = "";
        String weight_unit = "";
        String inventory_item_id = "";
        String inventory_quantity = "";
        String old_inventory_quantity = "";
        String requires_shipping = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getInventory_policy() {
            return inventory_policy;
        }

        public void setInventory_policy(String inventory_policy) {
            this.inventory_policy = inventory_policy;
        }

        public String getCompare_at_price() {
            return compare_at_price;
        }

        public void setCompare_at_price(String compare_at_price) {
            this.compare_at_price = compare_at_price;
        }

        public String getFulfillment_service() {
            return fulfillment_service;
        }

        public void setFulfillment_service(String fulfillment_service) {
            this.fulfillment_service = fulfillment_service;
        }

        public String getInventory_management() {
            return inventory_management;
        }

        public void setInventory_management(String inventory_management) {
            this.inventory_management = inventory_management;
        }

        public String getOption1() {
            return option1;
        }

        public void setOption1(String option1) {
            this.option1 = option1;
        }

        public String getOption2() {
            return option2;
        }

        public void setOption2(String option2) {
            this.option2 = option2;
        }

        public String getOption3() {
            return option3;
        }

        public void setOption3(String option3) {
            this.option3 = option3;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getTaxable() {
            return taxable;
        }

        public void setTaxable(String taxable) {
            this.taxable = taxable;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getGrams() {
            return grams;
        }

        public void setGrams(String grams) {
            this.grams = grams;
        }

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getWeight_unit() {
            return weight_unit;
        }

        public void setWeight_unit(String weight_unit) {
            this.weight_unit = weight_unit;
        }

        public String getInventory_item_id() {
            return inventory_item_id;
        }

        public void setInventory_item_id(String inventory_item_id) {
            this.inventory_item_id = inventory_item_id;
        }

        public String getInventory_quantity() {
            return inventory_quantity;
        }

        public void setInventory_quantity(String inventory_quantity) {
            this.inventory_quantity = inventory_quantity;
        }

        public String getOld_inventory_quantity() {
            return old_inventory_quantity;
        }

        public void setOld_inventory_quantity(String old_inventory_quantity) {
            this.old_inventory_quantity = old_inventory_quantity;
        }

        public String getRequires_shipping() {
            return requires_shipping;
        }

        public void setRequires_shipping(String requires_shipping) {
            this.requires_shipping = requires_shipping;
        }
    }

    public class Options implements Serializable {
        String id = "";
        String product_id = "";
        String name = "";
        String position = "";
        ArrayList<String> values = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public ArrayList<String> getValues() {
            return values;
        }

        public void setValues(ArrayList<String> values) {
            this.values = values;
        }
    }

    public class Images implements Serializable {

        String id = "";
        String product_id = "";
        String position = "";
        String created_at = "";
        String updated_at = "";
        String alt = "";
        String width = "";
        String height = "";
        String src = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }

    public class Image implements Serializable {

        String id = "";
        String product_id = "";
        String position = "";
        String created_at = "";
        String updated_at = "";
        String alt = "";
        String width = "";
        String height = "";
        String src = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getAlt() {
            return alt;
        }

        public void setAlt(String alt) {
            this.alt = alt;
        }

        public String getWidth() {
            return width;
        }

        public void setWidth(String width) {
            this.width = width;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }
    }
}
