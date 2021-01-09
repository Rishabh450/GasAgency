package com.example.sudikshagasagency.ModalClass;

public class Record {
    private String name;
    private String Date;
    private String totalcylinder;
    private String cylinderreturned;
    private int rate;
    private String amountgiven;
    private String cylindertype;
    private String time;
    private long code;
    private int total_amount;
    public Record(){

    }
    public Record(String Date,int amount,String amount_given,String cylinder_type,String name,String number_of_cylinder,String returned_cylinder,String time, long code,int total_amount){
        this.name = name;
        this.code = code;
        this.Date =Date;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTotalcylinder() {
        return totalcylinder;
    }

    public void setTotalcylinder(String totalcylinder) {
        this.totalcylinder = totalcylinder;
    }

    public String getCylinderreturned() {
        return cylinderreturned;
    }

    public void setCylinderreturned(String cylinderreturned) {
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

    public String getAmountgiven() {
        return amountgiven;
    }

    public void setAmountgiven(String amountgiven) {
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
