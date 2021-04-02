package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

public class GeneralDTO implements Serializable {

    ArrayList<Breed> breeds;
    ArrayList<Country> country;
    ArrayList<State> state;
    ArrayList<City> city;
    ArrayList<PetType> pet_type;

    public ArrayList<Breed> getBreeds() {
        return breeds;
    }

    public void setBreeds(ArrayList<Breed> breeds) {
        this.breeds = breeds;
    }

    public ArrayList<Country> getCountry() {
        return country;
    }

    public void setCountry(ArrayList<Country> country) {
        this.country = country;
    }

    public ArrayList<State> getState() {
        return state;
    }

    public void setState(ArrayList<State> state) {
        this.state = state;
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

    public class Breed implements Serializable {
        String id;
        String breed_name;

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

    public class State implements Serializable {
        String state;

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
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


