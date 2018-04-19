package com.sourcey.smartshopping;

/**
 * Created by yashas on 06-06-2017.
 */

public class Products {
    private long id;
    private String pid;
    private String pname;
    private String pquantity;
    private String pweight;
    private String pprice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public void setPquantity(String pquantity) {
        this.pquantity = pquantity;
    }

    public void setPweight(String pweight) {
        this.pweight = pweight;
    }

    public void setPprice(String pprice) {
        this.pprice = pprice;
    }

    public String getPid() {
        return pid;
    }

    public String getPname() {
        return pname;
    }

    public String getPquantity() {
        return pquantity;
    }

    public String getPweight() {
        return pweight;
    }

    public String getPprice() {
        return pprice;
    }



    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return pid;
    }
}