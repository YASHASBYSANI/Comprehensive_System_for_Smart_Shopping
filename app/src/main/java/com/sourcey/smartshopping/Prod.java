package com.sourcey.smartshopping;

/**
 * Created by yashas on 06-06-2017.
 */

public class Prod {
    private String Prodname;
    private String prodquantity;
    private String prodprice;
    private String prodid;

    public Prod(String pName, String pQuant, String pPrice,String ppid) {
        Prodname = pName;
        prodquantity = pQuant;
        prodprice = pPrice;
        prodid = ppid;

    }

    public String getProdname() {
        return Prodname;
    }

    public String getProdquantity() {
        return prodquantity;
    }

    public String getProdprice() {
        return prodprice;
    }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getProdid() {
        return prodid;

    }

    public void setProdname(String prodname) {
        Prodname = prodname;
    }

    public void setProdquantity(String prodquantity) {
        this.prodquantity = prodquantity;
    }

    public void setProdprice(String prodprice) {
        this.prodprice = prodprice;
    }
}


