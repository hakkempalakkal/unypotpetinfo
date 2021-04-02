package com.samyotech.petstand.models;

/**
 * Created by welcome pc on 28-12-2017.
 */

public class ReminderRepeat
{
    String name;
    int value;

    public ReminderRepeat(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
