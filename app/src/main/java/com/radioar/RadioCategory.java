package com.radioar;

import java.util.ArrayList;

public class RadioCategory {

    private String name;
    private ArrayList<RadioStation> list = new ArrayList<RadioStation>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<RadioStation> getProductList() {
        return list;
    }

    public void setProductList(ArrayList<RadioStation> productList) {
        this.list = productList;
    }

}