package com.example.sudikshagasagency.ModalClass;

public class Record {
    private String name;
    private String Date;
    private int totalcylinder;
    private int cylinderreturned;
    private int rate;
    private int amountgiven;
    private String cylindertype;
    private String time;
    private long code;
    private int total_amount;
    private String picture ;
    private String timeStamp;
    public Record(){

    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Record(String Date, int amount, int amount_given, String cylinder_type, String name, int number_of_cylinder, int returned_cylinder, String time, long code, int total_amount, String picture, String timeStamp){
        this.name = name;
        this.code = code;
        this.Date =Date;
        this.picture = picture;
        this.timeStamp = timeStamp ;
        this.total_amount = total_amount;
        this.rate = amount;
        this.cylindertype = cylinder_type;
        this.totalcylinder = number_of_cylinder;
        this.cylinderreturned = returned_cylinder;
        this.amountgiven = amount_given;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getTotalcylinder() {
        return totalcylinder;
    }

    public void setTotalcylinder(int totalcylinder) {
        this.totalcylinder = totalcylinder;
    }

    public int getCylinderreturned() {
        return cylinderreturned;
    }

    public void setCylinderreturned(int cylinderreturned) {
        this.cylinderreturned = cylinderreturned;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getAmountgiven() {
        return amountgiven;
    }

    public void setAmountgiven(int amountgiven) {
        this.amountgiven = amountgiven;
    }

    public String getCylindertype() {
        return cylindertype;
    }

    public void setCylindertype(String cylindertype) {
        this.cylindertype = cylindertype;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
