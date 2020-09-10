package com.kajal.ym;

public class Places {

    public String name, latitude, longitude;

    public Places() {
    }

    public Places(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Places(String name, String link, String description) {
        this.name = name;
    }
}
