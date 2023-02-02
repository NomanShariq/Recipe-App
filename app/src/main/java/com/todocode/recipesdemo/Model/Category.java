package com.todocode.recipesdemo.Model;

public class Category {
    public String id, name, image, popular, veg;
    public int recipes_num;

    public Category(String id, String name, String image, String popular, String veg, int recipes_num) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.popular = popular;
        this.veg = veg;
        this.recipes_num = recipes_num;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVeg() {
        return veg;
    }

    public void setVeg(String veg) {
        this.veg = veg;
    }

    public int getRecipes_num() {
        return recipes_num;
    }

    public void setRecipes_num(int recipes_num) {
        this.recipes_num = recipes_num;
    }
}
