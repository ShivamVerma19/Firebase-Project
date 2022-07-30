package com.example.firebaseproject;

public class Notes {

    String title ;
    String subtitle ;

    Notes(){

    }


    Notes(String title , String subtitle){
        this.title = title ;
        this.subtitle = subtitle ;
    }


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
