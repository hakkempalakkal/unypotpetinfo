package com.samyotech.petstand.models;

/**
 * Created by varunverma on 25/9/17.
 */

public class DummyFilterDTO {
    String id = "";
    String name = "";
    boolean checked = false;

    public DummyFilterDTO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public DummyFilterDTO(String name) {
        this.name = name;
    }

    public DummyFilterDTO(boolean checked) {
        this.checked = checked;
    }

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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
