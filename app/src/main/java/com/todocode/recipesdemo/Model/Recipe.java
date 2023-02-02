package com.todocode.recipesdemo.Model;

public class Recipe {
    public String id, title, description, review, time, servings, calories, image_url, video_url, rating, category_id, category_name, category_image, chef_id, chef_username, chef_image, chef_trusted, chef_paypal, chef_email, vegetarian_or_not, meal_id, meal_name, meal_image, cuisine_id, cuisine_name, cuisine_image;
    public int views;
    public String chef_gender, chef_vegetarian, chef_facebook, chef_twitter, chef_instagram, chef_member_since;


    public Recipe(String id, String title, String description, String review, String time, String servings, String calories, String image_url, String video_url, String rating, String category_id, String category_name, String category_image, String chef_id, String chef_username, String chef_image, String chef_trusted, String chef_paypal, String chef_email, String vegetarian_or_not, String meal_id, String meal_name, String meal_image, String cuisine_id, String cuisine_name, String cuisine_image, int views, String chef_gender, String chef_vegetarian, String chef_facebook, String chef_twitter, String chef_instagram, String chef_member_since) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.review = review;
        this.time = time;
        this.servings = servings;
        this.calories = calories;
        this.image_url = image_url;
        this.video_url = video_url;
        this.rating = rating;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_image = category_image;
        this.chef_id = chef_id;
        this.chef_username = chef_username;
        this.chef_image = chef_image;
        this.chef_trusted = chef_trusted;
        this.chef_paypal = chef_paypal;
        this.chef_email = chef_email;
        this.vegetarian_or_not = vegetarian_or_not;
        this.meal_id = meal_id;
        this.meal_name = meal_name;
        this.meal_image = meal_image;
        this.cuisine_id = cuisine_id;
        this.cuisine_name = cuisine_name;
        this.cuisine_image = cuisine_image;
        this.views = views;
        this.chef_gender = chef_gender;
        this.chef_vegetarian = chef_vegetarian;
        this.chef_facebook = chef_facebook;
        this.chef_twitter = chef_twitter;
        this.chef_instagram = chef_instagram;
        this.chef_member_since = chef_member_since;
    }

    public String getChef_gender() {
        return chef_gender;
    }

    public void setChef_gender(String chef_gender) {
        this.chef_gender = chef_gender;
    }

    public String getChef_vegetarian() {
        return chef_vegetarian;
    }

    public void setChef_vegetarian(String chef_vegetarian) {
        this.chef_vegetarian = chef_vegetarian;
    }

    public String getChef_facebook() {
        return chef_facebook;
    }

    public void setChef_facebook(String chef_facebook) {
        this.chef_facebook = chef_facebook;
    }

    public String getChef_twitter() {
        return chef_twitter;
    }

    public void setChef_twitter(String chef_twitter) {
        this.chef_twitter = chef_twitter;
    }

    public String getChef_instagram() {
        return chef_instagram;
    }

    public void setChef_instagram(String chef_instagram) {
        this.chef_instagram = chef_instagram;
    }

    public String getChef_member_since() {
        return chef_member_since;
    }

    public void setChef_member_since(String chef_member_since) {
        this.chef_member_since = chef_member_since;
    }

    public String getChef_paypal() {
        return chef_paypal;
    }

    public void setChef_paypal(String chef_paypal) {
        this.chef_paypal = chef_paypal;
    }

    public String getChef_email() {
        return chef_email;
    }

    public void setChef_email(String chef_email) {
        this.chef_email = chef_email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getServings() {
        return servings;
    }

    public void setServings(String servings) {
        this.servings = servings;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public String getChef_id() {
        return chef_id;
    }

    public void setChef_id(String chef_id) {
        this.chef_id = chef_id;
    }

    public String getChef_username() {
        return chef_username;
    }

    public void setChef_username(String chef_username) {
        this.chef_username = chef_username;
    }

    public String getChef_image() {
        return chef_image;
    }

    public void setChef_image(String chef_image) {
        this.chef_image = chef_image;
    }

    public String getChef_trusted() {
        return chef_trusted;
    }

    public void setChef_trusted(String chef_trusted) {
        this.chef_trusted = chef_trusted;
    }

    public String getVegetarian_or_not() {
        return vegetarian_or_not;
    }

    public void setVegetarian_or_not(String vegetarian_or_not) {
        this.vegetarian_or_not = vegetarian_or_not;
    }

    public String getMeal_id() {
        return meal_id;
    }

    public void setMeal_id(String meal_id) {
        this.meal_id = meal_id;
    }

    public String getMeal_name() {
        return meal_name;
    }

    public void setMeal_name(String meal_name) {
        this.meal_name = meal_name;
    }

    public String getMeal_image() {
        return meal_image;
    }

    public void setMeal_image(String meal_image) {
        this.meal_image = meal_image;
    }

    public String getCuisine_id() {
        return cuisine_id;
    }

    public void setCuisine_id(String cuisine_id) {
        this.cuisine_id = cuisine_id;
    }

    public String getCuisine_name() {
        return cuisine_name;
    }

    public void setCuisine_name(String cuisine_name) {
        this.cuisine_name = cuisine_name;
    }

    public String getCuisine_image() {
        return cuisine_image;
    }

    public void setCuisine_image(String cuisine_image) {
        this.cuisine_image = cuisine_image;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
