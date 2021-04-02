package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeDTO implements Serializable {

    public ArrayList<FoodListDTO> popular_products;
    public ArrayList<FoodListDTO> New_Fresh;
    public ArrayList<OffersDTO> offer;
    public ArrayList<AnimalsDTO> category;
    public ArrayList<BrandsDTO> brand;
    public ArrayList<BannerDTO> banner;
    public Breed breed;
    public Contact contact;

    public ArrayList<FoodListDTO> getPopular_products() {
        return popular_products;
    }

    public void setPopular_products(ArrayList<FoodListDTO> popular_products) {
        this.popular_products = popular_products;
    }

    public ArrayList<OffersDTO> getOffer() {
        return offer;
    }

    public void setOffer(ArrayList<OffersDTO> offer) {
        this.offer = offer;
    }

    public ArrayList<AnimalsDTO> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<AnimalsDTO> category) {
        this.category = category;
    }

    public ArrayList<BrandsDTO> getBrand() {
        return brand;
    }

    public void setBrand(ArrayList<BrandsDTO> brand) {
        this.brand = brand;
    }

    public ArrayList<BannerDTO> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<BannerDTO> banner) {
        this.banner = banner;
    }

    public ArrayList<FoodListDTO> getNew_Fresh() {
        return New_Fresh;
    }

    public void setNew_Fresh(ArrayList<FoodListDTO> new_Fresh) {
        New_Fresh = new_Fresh;
    }


    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public class Breed implements Serializable {

        String id = "";
        String breed_name = "";
        String b_description = "";
        String image = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBreed_name() {
            return breed_name;
        }

        public void setBreed_name(String breed_name) {
            this.breed_name = breed_name;
        }

        public String getB_description() {
            return b_description;
        }

        public void setB_description(String b_description) {
            this.b_description = b_description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class Contact implements Serializable {

        String id = "";
        String name = "";
        String mobile_no = "";
        String email = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMobile_no() {
            return mobile_no;
        }

        public void setMobile_no(String mobile_no) {
            this.mobile_no = mobile_no;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}