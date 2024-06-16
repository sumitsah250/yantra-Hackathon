package com.example.messagewithme;

public class Food_details {
    String food_name;
    String food_expire;
    String food_location;

    public String getFood_quantity() {
        return food_quantity;
    }

    public void setFood_quantity(String food_quantity) {
        this.food_quantity = food_quantity;
    }

    public Food_details() {
    }

    public Food_details(Food_details foodDetails1) {
        this.food_name =foodDetails1.food_name;
        this.food_expire=foodDetails1.food_expire;
        this.food_image_name=foodDetails1.food_image_name;

    }

    public Food_details(String food_name, String food_expire, String food_location, String food_quantity, String food_image_name, String user_id) {
        this.food_name = food_name;
        this.food_expire = food_expire;
        this.food_location = food_location;
        this.food_quantity = food_quantity;
        this.food_image_name = food_image_name;
        this.user_id = user_id;
    }

    String food_quantity;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String food_image_name;
    String user_id;

    public Food_details(String food_name, String food_expire, String food_location, String food_image_name, String user_id) {
        this.food_name = food_name;
        this.food_expire = food_expire;
        this.food_location = food_location;
        this.food_image_name = food_image_name;
        this.user_id = user_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_expire() {
        return food_expire;
    }

    public void setFood_expire(String food_expire) {
        this.food_expire = food_expire;
    }

    public String getFood_location() {
        return food_location;
    }

    public void setFood_location(String food_location) {
        this.food_location = food_location;
    }

    public String getFood_image_name() {
        return food_image_name;
    }

    public void setFood_image_name(String food_image_name) {
        this.food_image_name = food_image_name;
    }

    public Food_details(String food_name, String food_expire, String food_location, String food_image_name) {
        this.food_name = food_name;
        this.food_expire = food_expire;
        this.food_location = food_location;
        this.food_image_name = food_image_name;
    }
}
