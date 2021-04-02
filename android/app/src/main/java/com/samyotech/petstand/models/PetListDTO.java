package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mayank on 14/2/18.
 */

public class PetListDTO implements Serializable {
    String id = "";
    String user_id = "";
    String petName = "";
    String breed_id = "";
    String sex = "";
    String age = "";
    String city = "";
    String state = "";
    String country = "";
    String postcode = "";
    String lati = "";
    String longi = "";
    String created_stamp = "";
    String active_status = "";
    String fathers_breed = "";
    String mothers_breed = "";
    String current_height = "";
    String current_weight = "";
    String microchip_id = "";
    String pet_type = "";
    String active_area = "";
    String lifestyle = "";
    String neutered = "";
    String trained = "";
    String temparement_ok_dog = "";
    String temparement_ok_cat = "";
    String temparement_ok_people = "";
    String temparement_ok_child = "";
    String ins_provider = "";
    String ins_policy_no = "";
    String ins_upload = "";
    String swimmer = "";
    String petpic = "";
    String birth_date = "";
    String adoption_date = "";
    String allergies = "";
    String medical_conditions = "";
    String medicines = "";
    String likes = "";
    String dislikes = "";
    String ins_renewal_date = "";
    String incidents = "";
    String spayed = "";
    String pet_img_path = "";
    String pet_ins_path = "";
    String ins_user_email = "";
    String ins_id = "";
    String long_value = "";
    String deleted = "";
    String public_private = "";
    String sel_notsel = "";
    String adopt = "";
    String breed_name = "";
    int breed_target = 0;
    String percent = "";
    String pet_type_name = "";
    String updated_stamp = "";
    String view_profile = "";
    String rating = "";
    String is_follow = "";
    String followerCount = "";
    String total_rating_user = "";
    String msg = "";
    String distance = "";
    String verified = "";
    ArrayList<Chart> chart = new ArrayList<>();

    public String getAdopt() {
        return adopt;
    }

    public void setAdopt(String adopt) {
        this.adopt = adopt;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public PetListDTO(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed_id() {
        return breed_id;
    }

    public void setBreed_id(String breed_id) {
        this.breed_id = breed_id;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCreated_stamp() {
        return created_stamp;
    }

    public void setCreated_stamp(String created_stamp) {
        this.created_stamp = created_stamp;
    }

    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }

    public String getFathers_breed() {
        return fathers_breed;
    }

    public void setFathers_breed(String fathers_breed) {
        this.fathers_breed = fathers_breed;
    }

    public String getMothers_breed() {
        return mothers_breed;
    }

    public void setMothers_breed(String mothers_breed) {
        this.mothers_breed = mothers_breed;
    }

    public String getCurrent_height() {
        return current_height;
    }

    public void setCurrent_height(String current_height) {
        this.current_height = current_height;
    }

    public String getCurrent_weight() {
        return current_weight;
    }

    public void setCurrent_weight(String current_weight) {
        this.current_weight = current_weight;
    }

    public String getMicrochip_id() {
        return microchip_id;
    }

    public void setMicrochip_id(String microchip_id) {
        this.microchip_id = microchip_id;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }


    public String getActive_area() {
        return active_area;
    }

    public void setActive_area(String active_area) {
        this.active_area = active_area;
    }

    public String getLifestyle() {
        return lifestyle;
    }

    public void setLifestyle(String lifestyle) {
        this.lifestyle = lifestyle;
    }

    public String getNeutered() {
        return neutered;
    }

    public void setNeutered(String neutered) {
        this.neutered = neutered;
    }

    public String getTrained() {
        return trained;
    }

    public void setTrained(String trained) {
        this.trained = trained;
    }

    public String getTemparement_ok_dog() {
        return temparement_ok_dog;
    }

    public void setTemparement_ok_dog(String temparement_ok_dog) {
        this.temparement_ok_dog = temparement_ok_dog;
    }

    public String getTemparement_ok_cat() {
        return temparement_ok_cat;
    }

    public void setTemparement_ok_cat(String temparement_ok_cat) {
        this.temparement_ok_cat = temparement_ok_cat;
    }

    public String getTemparement_ok_people() {
        return temparement_ok_people;
    }

    public void setTemparement_ok_people(String temparement_ok_people) {
        this.temparement_ok_people = temparement_ok_people;
    }

    public String getTemparement_ok_child() {
        return temparement_ok_child;
    }

    public void setTemparement_ok_child(String temparement_ok_child) {
        this.temparement_ok_child = temparement_ok_child;
    }

    public String getIns_provider() {
        return ins_provider;
    }

    public void setIns_provider(String ins_provider) {
        this.ins_provider = ins_provider;
    }

    public String getIns_policy_no() {
        return ins_policy_no;
    }

    public void setIns_policy_no(String ins_policy_no) {
        this.ins_policy_no = ins_policy_no;
    }

    public String getIns_upload() {
        return ins_upload;
    }

    public void setIns_upload(String ins_upload) {
        this.ins_upload = ins_upload;
    }

    public String getSwimmer() {
        return swimmer;
    }

    public void setSwimmer(String swimmer) {
        this.swimmer = swimmer;
    }

    public String getPetpic() {
        return petpic;
    }

    public void setPetpic(String petpic) {
        this.petpic = petpic;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getAdoption_date() {
        return adoption_date;
    }

    public void setAdoption_date(String adoption_date) {
        this.adoption_date = adoption_date;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedical_conditions() {
        return medical_conditions;
    }

    public void setMedical_conditions(String medical_conditions) {
        this.medical_conditions = medical_conditions;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getDislikes() {
        return dislikes;
    }

    public void setDislikes(String dislikes) {
        this.dislikes = dislikes;
    }

    public String getIns_renewal_date() {
        return ins_renewal_date;
    }

    public void setIns_renewal_date(String ins_renewal_date) {
        this.ins_renewal_date = ins_renewal_date;
    }

    public String getIncidents() {
        return incidents;
    }

    public void setIncidents(String incidents) {
        this.incidents = incidents;
    }

    public String getSpayed() {
        return spayed;
    }

    public void setSpayed(String spayed) {
        this.spayed = spayed;
    }

    public String getPet_img_path() {
        return pet_img_path;
    }

    public void setPet_img_path(String pet_img_path) {
        this.pet_img_path = pet_img_path;
    }

    public String getPet_ins_path() {
        return pet_ins_path;
    }

    public void setPet_ins_path(String pet_ins_path) {
        this.pet_ins_path = pet_ins_path;
    }

    public String getIns_user_email() {
        return ins_user_email;
    }

    public void setIns_user_email(String ins_user_email) {
        this.ins_user_email = ins_user_email;
    }

    public String getIns_id() {
        return ins_id;
    }

    public void setIns_id(String ins_id) {
        this.ins_id = ins_id;
    }

    public String getLong_value() {
        return long_value;
    }

    public void setLong_value(String long_value) {
        this.long_value = long_value;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getPublic_private() {
        return public_private;
    }

    public void setPublic_private(String public_private) {
        this.public_private = public_private;
    }

    public String getSel_notsel() {
        return sel_notsel;
    }

    public void setSel_notsel(String sel_notsel) {
        this.sel_notsel = sel_notsel;
    }

    public String getBreed_name() {
        return breed_name;
    }

    public void setBreed_name(String breed_name) {
        this.breed_name = breed_name;
    }

    public String getPet_type_name() {
        return pet_type_name;
    }

    public int getBreed_target() {
        return breed_target;
    }

    public void setBreed_target(int breed_target) {
        this.breed_target = breed_target;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public void setPet_type_name(String pet_type_name) {
        this.pet_type_name = pet_type_name;
    }

    public String getUpdated_stamp() {
        return updated_stamp;
    }

    public void setUpdated_stamp(String updated_stamp) {
        this.updated_stamp = updated_stamp;
    }

    public String getView_profile() {
        return view_profile;
    }

    public void setView_profile(String view_profile) {
        this.view_profile = view_profile;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTotal_rating_user() {
        return total_rating_user;
    }

    public void setTotal_rating_user(String total_rating_user) {
        this.total_rating_user = total_rating_user;
    }

    public ArrayList<Chart> getChart() {
        return chart;
    }

    public void setChart(ArrayList<Chart> chart) {
        this.chart = chart;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public class Chart implements Serializable {


        String id = "";
        String pet_id = "";
        String user_id = "";
        String time_stamp = "";
        int manualactivity = 0;
        String unit = "";
        String date = "";
        String target = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPet_id() {
            return pet_id;
        }

        public void setPet_id(String pet_id) {
            this.pet_id = pet_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
        }

        public int getManualactivity() {
            return manualactivity;
        }

        public void setManualactivity(int manualactivity) {
            this.manualactivity = manualactivity;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }
    }

}
