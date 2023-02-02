package com.todocode.recipesdemo.Model;

public class Chef {
    public String id, email, username, gender, vegetarian, avatar_url, paypal, trusted, facebook, twitter, instagram, member_since, numberOfRecipes;

    public Chef(String id, String email, String username, String gender, String vegetarian, String avatar_url, String paypal, String trusted, String facebook, String twitter, String instagram, String member_since, String numberOfRecipes) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.vegetarian = vegetarian;
        this.avatar_url = avatar_url;
        this.paypal = paypal;
        this.trusted = trusted;
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.member_since = member_since;
        this.numberOfRecipes = numberOfRecipes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(String vegetarian) {
        this.vegetarian = vegetarian;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getPaypal() {
        return paypal;
    }

    public void setPaypal(String paypal) {
        this.paypal = paypal;
    }

    public String getTrusted() {
        return trusted;
    }

    public void setTrusted(String trusted) {
        this.trusted = trusted;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getMember_since() {
        return member_since;
    }

    public void setMember_since(String member_since) {
        this.member_since = member_since;
    }

    public String getNumberOfRecipes() {
        return numberOfRecipes;
    }

    public void setNumberOfRecipes(String numberOfRecipes) {
        this.numberOfRecipes = numberOfRecipes;
    }
}
