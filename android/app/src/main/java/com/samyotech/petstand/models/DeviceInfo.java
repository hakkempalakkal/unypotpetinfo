package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 17-10-2016.
 */
public class DeviceInfo implements Serializable {

    String ipAddress="";
    String name="";


    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
