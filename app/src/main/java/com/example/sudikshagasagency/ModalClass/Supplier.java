package com.example.sudikshagasagency.ModalClass;

public class Supplier {
    String area;
    String code;
    String name;
    String number;
    String picture;

    public Supplier() {
    }

    public Supplier(String area, String code, String name, String number, String picture) {
        this.area = area;
        this.code = code;
        this.name = name;
        this.number = number;
        this.picture = picture;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
