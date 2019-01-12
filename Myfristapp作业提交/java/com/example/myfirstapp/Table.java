package com.example.myfirstapp;

public class Table {
    private String icon, name, vicinity, placeid;

    public Table(){
    }

    public Table(String icon, String name, String vicinity, String placeid){
        this.icon = icon;
        this.name = name;
        this.vicinity = vicinity;
        this.placeid = placeid;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setVicinity(String vicinity){
        this.vicinity = vicinity;
    }

    public void setPlaceid(String placeid) {this.placeid = placeid; }

    public String getIcon(){
        return icon;
    }

    public String getName(){
        return name;
    }

    public String getVicinity(){
        return vicinity;
    }

    public String getPlaceid() { return placeid; }
}
