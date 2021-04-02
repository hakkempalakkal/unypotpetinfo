package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneralPetMarketDTO implements Serializable {


    ArrayList<Country> country;
    ArrayList<City> city;
    ArrayList<PetType> pet_type;


    public ArrayList<Country> getCountry() {
        return country;
    }

    public void setCountry(ArrayList<Country> country) {
        this.country = country;
    }


    public ArrayList<City> getCity() {
        return city;
    }

    public void setCity(ArrayList<City> city) {
        this.city = city;
    }

    public ArrayList<PetType> getPet_type() {
        return pet_type;
    }

    public void setPet_type(ArrayList<PetType> pet_type) {
        this.pet_type = pet_type;
    }

    public class PetType implements Serializable {
        String id;
        String pet_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPet_name() {
            return pet_name;
        }

        public void setPet_name(String pet_name) {
            this.pet_name = pet_name;
        }
    }

    public class City implements Serializable {
        String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }


    public class Country implements Serializable {
        String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

}


